package com.qihoo360.replugin.sample.service.intercepter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by pengyi on 2017/7/26.
 */

public class CommonParamsIntercepter implements Interceptor {
  @Override
  public Response intercept(Chain chain) throws IOException {
    Request originRequest = chain.request();
    Request.Builder newRequest = originRequest.newBuilder();
    if ("GET".equals(originRequest.method())) {
      HttpUrl.Builder newUrlBuilder = originRequest.url().newBuilder();
      Map<String, String> queryParamMap = getCommonParamMap();
      if (queryParamMap != null && !queryParamMap.isEmpty()) {
        for (Map.Entry<String, String> entry : queryParamMap.entrySet()) {
          newUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        newRequest.url(newUrlBuilder.build());
      }
    } else if ("POST".equals(originRequest.method())) {
      RequestBody body = originRequest.body();
      if (body != null && body instanceof FormBody) {
        // POST Param x-www-form-urlencoded
        FormBody formBody = (FormBody) body;
        Map<String, String> formBodyParamMap = new HashMap<>();
        int bodySize = formBody.size();
        for (int i = 0; i < bodySize; i++) {
          formBodyParamMap.put(formBody.name(i), formBody.value(i));
        }

        Map<String, String> newFormBodyParamMap = getCommonParamMap();
        if (newFormBodyParamMap != null) {
          formBodyParamMap.putAll(newFormBodyParamMap);
          FormBody.Builder bodyBuilder = new FormBody.Builder();
          for (Map.Entry<String, String> entry : formBodyParamMap.entrySet()) {
            bodyBuilder.add(entry.getKey(), entry.getValue());
          }
          newRequest.method(originRequest.method(), bodyBuilder.build());
        }
      }
    }
    return chain.proceed(newRequest.build());
  }

  public Map<String, String> getCommonParamMap() {
    HashMap<String, String> params = new HashMap<>();
    params.put("wdId", "523fe6d993785bec2d4cd0814a7718cf");
    params.put("FFClientType", "2");
    params.put("imei", "000000000000000");
    params.put("puid", "E8F84D1AE88849DF90BBF03F2B3FE024");
    params.put("clientInfo", "{\"clientType\":\"3\"}");
    params.put("version", "1");
    params.put("FFClientVersion", "420000000");
    params.put("ddId", "bf5d7e0cd5ab4e83b761789f2f1295f07b7cdb1e");
    params.put("siedc", "af0444e9df21cbcb8681369611692e910180e115d05226d296b726704350b478");
    params.put("devInfo",
        "{\"IMEI\":\"000000000000000\",\"IP\":\"10.0.3.15\",\"cell_id\":\"{\\\"cells\\\":[{\\\"cid\\\":0,\\\"lac\\\":0,\\\"mnc\\\":260,\\\"mcc\\\":310,\\\"strength\\\":0}]}\",\"device_desc\":\"Google Nexus 6P - 6.0.0 - API 23 - 1440x2560\",\"device_id\":\"59184ad5e4bf9d94\",\"gmtTime\":\"2017-07-26 21:19:04\",\"imsi\":\"310260000000000\",\"mac\":\"02:00:00:00:00:00\",\"merchantAppId\":\"feifan001\",\"network\":\"Wifi\",\"os_type\":\"android\",\"os_version\":\"Android6.0\",\"phoneModel\":\"Google Nexus 6P - 6.0.0 - API 23 - 1440x2560\",\"phone_wifi_mac\":\"02:00:00:00:00:00\",\"router_mac\":\"01:80:c2:00:00:03\",\"size\":\"1440 * 2392\",\"sourceFrom\":\"APP\",\"wifi\":\"WiredSSID\",\"wifiMac\":\"01:80:c2:00:00:03\"}");
    return params;
  }
}
