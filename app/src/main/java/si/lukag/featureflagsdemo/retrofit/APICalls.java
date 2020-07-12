package si.lukag.featureflagsdemo.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import si.lukag.featureflagsdemo.models.RuleDto;

public interface APICalls {
    @POST("user")
    Call<Response<String>> registerUser(@Query("client_id") String clientId,
                                        @Query("app_id") String appId);

    @GET("user")
    Call<List<RuleDto>> getRulesByClientID(@Query("client_id") String clientId);
}
