package com.py.library.network.manager;

import android.text.TextUtils;
import android.util.Log;
import com.py.library.network.exception.InvalidUrlException;
import com.py.library.network.http.DefaultUrlParser;
import com.py.library.network.http.UrlParser;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pengyi on 2017/7/27.
 */

public class BaseUrlMappingManager {
  private static final String DOMAIN_NAME = "Domain-Name";
  private static final String GLOBAL_DOMAIN_NAME = "globalDomainName";
  public static final String DOMAIN_NAME_HEADER = DOMAIN_NAME + ": ";
  private final static String TAG = "BaseUrlMappingManager";
  private HashMap<String, HttpUrl> serviceHostMap = new HashMap<>();
  private Interceptor mserviceHostIntecepter;



  boolean isRun = true;
  private UrlParser mUrlParser;

  private BaseUrlMappingManager() {
    mUrlParser = new DefaultUrlParser();
    mserviceHostIntecepter = new Interceptor() {
      @Override
      public Response intercept(Chain chain) throws IOException {
        if (!isRun) {
            return chain.proceed(chain.request());
        } else {
            return chain.proceed(processRequest(chain.request()));
        }
      }
    };
  }

  private static class Holder {
    private static BaseUrlMappingManager singleInstance = new BaseUrlMappingManager();
  }

  public static BaseUrlMappingManager getInstance() {
    return Holder.singleInstance;
  }

  public OkHttpClient.Builder with(OkHttpClient.Builder builder) {
    builder.addInterceptor(mserviceHostIntecepter);
    return builder;
  }

  /**
   * 对 {@link Request} 进行一些必要的加工
   *
   * @param request
   * @return
   */
  public Request processRequest(Request request) {

    Request.Builder newBuilder = request.newBuilder();

    String domainName = obtainDomainNameFromHeaders(request);

    HttpUrl httpUrl;

    // 如果有 header，获取 header 中配置的url，否则检查全局的 BaseUrl，未找到则为null
    if (!TextUtils.isEmpty(domainName)) {
      httpUrl = fetchDomain(domainName);
      newBuilder.removeHeader(DOMAIN_NAME);
    } else {
      httpUrl = fetchDomain(GLOBAL_DOMAIN_NAME);
    }

    if (null != httpUrl) {
      HttpUrl newUrl = mUrlParser.parseUrl(httpUrl, request.url());
      Log.d(BaseUrlMappingManager.TAG, "New Url is { " + newUrl.toString() + " } , Old Url is { "
          + request.url().toString() + " }");

      return newBuilder
          .url(newUrl)
          .build();
    }

    return newBuilder.build();

  }

  /**
   * 全局动态替换 BaseUrl，优先级： Header中配置的url > 全局配置的url
   * 除了作为备用的 BaseUrl ,当你项目中只有一个 BaseUrl ,但需要动态改变
   * 这种方式不用在每个接口方法上加 Header,也是个很好的选择
   *
   * @param url
   */
  public void setGlobalDomain(String url) {
    synchronized (serviceHostMap) {
      serviceHostMap.put(GLOBAL_DOMAIN_NAME, checkUrl(url));
    }
  }

  /**
   * 获取全局 BaseUrl
   */
  public HttpUrl getGlobalDomain() {
    return serviceHostMap.get(GLOBAL_DOMAIN_NAME);
  }

  /**
   * 移除全局 BaseUrl
   */
  public void removeGlobalDomain() {
    synchronized (serviceHostMap) {
      serviceHostMap.remove(GLOBAL_DOMAIN_NAME);
    }
  }

  /**
   * 存放 Domain 的映射关系
   *
   * @param domainName
   * @param domainUrl
   */
  public void putDomain(String domainName, String domainUrl) {
    synchronized (serviceHostMap) {
      serviceHostMap.put(domainName, checkUrl(domainUrl));
    }
  }

  /**
   * 取出对应 DomainName 的 Url
   *
   * @param domainName
   * @return
   */
  public HttpUrl fetchDomain(String domainName) {
    return serviceHostMap.get(domainName);
  }

  public void removeDomain(String domainName) {
    synchronized (serviceHostMap) {
      serviceHostMap.remove(domainName);
    }
  }

  public void clearAllDomain() {
    serviceHostMap.clear();
  }

  public boolean haveDomain(String domainName) {
    return serviceHostMap.containsKey(domainName);
  }

  public int domainSize() {
    return serviceHostMap.size();
  }

  private String obtainDomainNameFromHeaders(Request request) {
    List<String> headers = request.headers(DOMAIN_NAME);
    if (headers == null || headers.size() == 0)
      return null;
    if (headers.size() > 1)
      throw new IllegalArgumentException("Only one Domain-Name in the headers");
    return request.header(DOMAIN_NAME);
  }


  private HttpUrl checkUrl(String url) {
    HttpUrl parseUrl = HttpUrl.parse(url);
    if (null == parseUrl) {
      throw new InvalidUrlException(url);
    } else {
      return parseUrl;
    }
  }

  public void setmUrlParser(UrlParser mUrlParser) {
    this.mUrlParser = mUrlParser;
  }

  public void setRun(boolean run) {
    isRun = run;
  }
}
