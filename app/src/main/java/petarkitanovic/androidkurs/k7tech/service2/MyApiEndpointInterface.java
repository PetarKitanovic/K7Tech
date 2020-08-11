package petarkitanovic.androidkurs.k7tech.service2;


import petarkitanovic.androidkurs.k7tech.models.TodaysWeather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyApiEndpointInterface {


    @GET("onecall")
    Call<TodaysWeather> getWeatherData(@Query("lat") String lat,
                                             @Query("lon") String lng,
                                             @Query("units") String units,
                                             @Query("exclude") String exclude,
                                             @Query("appid") String appid);

}
