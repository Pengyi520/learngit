package com.qihoo360.replugin.sample.service;

import com.qihoo360.replugin.sample.service.params.BillParams;
import com.qihoo360.replugin.sample.service.response.BillResponse;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import static com.py.library.network.manager.BaseUrlMappingManager.DOMAIN_NAME_HEADER;

/**
 * Created by pengyi on 2017/7/25.
 */

public interface MyService {
  /**
   * private String KEY_ACTION = "action";
   * private String KEY_PUID = "puid";
   * private String KEY_BILL_TYPE = "billType";
   * private String KEY_PAGE_SIZE = "pageSize";
   * private String KEY_PAGE_INDEX = "pageIndex";
   * private String KEY_GROUP_ID = "groupId";
   * 
   * @return
   */
  @POST("ffan/v1/smartLife/paidBills")
  Observable<BillResponse> post(@Body BillParams params);


  @POST("ffan/v1/smartLife/paidBills")
  @Headers({DOMAIN_NAME_HEADER + "service1"})
  @FormUrlEncoded
  Observable<BillResponse> postByField(@Field("action") String action,
      @Field("billType") String billType, @Field("pageSize") String pageSize,
      @Field("pageIndex") String pageIndex);

  @GET("ffan/v1/smartLife/paidBills")
  @Headers({"Accept: application/vnd.github.v3.full+json",
      "User-Agent: Retrofit-Sample-App"})
  Observable<BillResponse> get(@Header("auth") String header, @Query("id") String id,
      @QueryMap(encoded = true) HashMap paramMap);

  @POST("ffan/v1/smartLife/paidBills")
  @FormUrlEncoded
  Observable<BillResponse> post2(@Field("id") String id, @FieldMap HashMap paramMap);
}
