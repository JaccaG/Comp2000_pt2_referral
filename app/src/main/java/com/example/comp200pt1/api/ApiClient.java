package com.example.comp200pt1.api;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    // Base URL for the API
    private static final String BASE_URL = "http://10.240.72.69/comp2000/library/";

    private static volatile LibraryApi INSTANCE;

    private ApiClient() {}

    public static LibraryApi get() {
        if (INSTANCE == null) {
            synchronized (ApiClient.class) {
                if (INSTANCE == null) {
                    // Logs full request/response bodies to Logcat (super helpful when I was debugging)
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY); // was BASIC

                    // stops app hanging indefinitely on bad network
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .addInterceptor(logging)
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build();

                    INSTANCE = retrofit.create(LibraryApi.class);
                }
            }
        }
        return INSTANCE;
    }
}
