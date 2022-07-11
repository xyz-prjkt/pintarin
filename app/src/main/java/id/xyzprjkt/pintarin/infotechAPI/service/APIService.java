package id.xyzprjkt.pintarin.infotechAPI.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    @POST("student")
    @FormUrlEncoded
    Call<Object> authLogin(@Field("username") String username,
                         @Field("password") String password);

}
