package com.pedroimai.previsaotempo.data.localizacao;


import com.google.android.gms.maps.GoogleMap;
import com.pedroimai.previsaotempo.data.previsao.model.Coordenadas;

public interface LocalizacaoServiceApi {

    interface LocalizacaoServiceCallback<T> {
        void onCoordenadasReady(T coordenadas);
        void onCoordenadasFail();
    }

    void getMinhasCoordenadas(LocalizacaoServiceCallback<Coordenadas> callback);
}
