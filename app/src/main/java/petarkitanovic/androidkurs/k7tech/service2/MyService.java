package petarkitanovic.androidkurs.k7tech.service2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyService {

    private static Retrofit getRetrofitInstance() {

        return new Retrofit.Builder()
                .baseUrl(MyServiceContract.WEATHERURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static MyApiEndpointInterface apiInterface() {

        return getRetrofitInstance().create(MyApiEndpointInterface.class);
    }
}
