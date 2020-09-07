package si.lukag.featureflagsmodule.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import si.lukag.featureflagsmodule.models.RuleDto;

public interface APICalls {
    @POST("user")
    Call<Response<String>> registerUser(@Query("client_id") String clientId,
                                        @Query("app_name") String appId);

    @GET("rules")
    Call<List<RuleDto>> getRulesByClientID(@Query("client_id") String clientId);
}
