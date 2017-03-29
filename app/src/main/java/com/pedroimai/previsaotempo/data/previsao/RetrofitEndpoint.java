package com.pedroimai.previsaotempo.data.previsao;


import com.pedroimai.previsaotempo.data.previsao.model.*;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RetrofitEndpoint {
    @GET("find?cnt=50&lang=pt&appid=139a41a9eae3120549ef5ac8ecf15f5c")
    Call<PrevisaoCompletaResultado> cidadesProximas(@Query("lat") double latitude,
                                                    @Query("lon") double longitude,
                                                    @Query("units") String unidade
    );
}
