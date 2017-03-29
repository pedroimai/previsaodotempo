package com.pedroimai.previsaotempo.data.previsao;

import com.pedroimai.previsaotempo.data.previsao.model.Coordenadas;
import com.pedroimai.previsaotempo.data.previsao.model.PrevisaoCompleta;
import com.pedroimai.previsaotempo.data.previsao.model.PrevisaoCompletaResultado;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrevisaoServiceImpl implements PrevisaoServiceApi {
    RetrofitEndpoint mRetrofit;

    public PrevisaoServiceImpl() {
        mRetrofit = RetrofitClient.getClient().create(RetrofitEndpoint.class);
    }

    @Override
    public void getPrevisoesCidadesProximas(final Coordenadas coordenadas, final String unidade, final PrevisaoServiceCallback<PrevisaoCompletaResultado> callback) {
        Call<PrevisaoCompletaResultado> callPrevisao = mRetrofit.cidadesProximas(coordenadas.latitude, coordenadas.longitude, unidade);
        callPrevisao.enqueue(new Callback<PrevisaoCompletaResultado>() {
            @Override
            public void onResponse(Call<PrevisaoCompletaResultado> call, Response<PrevisaoCompletaResultado> response) {
                if (response.code() == 200) {
                    PrevisaoCompletaResultado resultado = response.body();
                    callback.onLoaded(resultado);
                }

            }

            @Override
            public void onFailure(Call<PrevisaoCompletaResultado> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });

    }
}
