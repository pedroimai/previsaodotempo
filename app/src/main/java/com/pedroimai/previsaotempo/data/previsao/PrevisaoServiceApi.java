package com.pedroimai.previsaotempo.data.previsao;

import com.pedroimai.previsaotempo.data.previsao.model.Coordenadas;
import com.pedroimai.previsaotempo.data.previsao.model.PrevisaoCompletaResultado;

public interface PrevisaoServiceApi {

    interface PrevisaoServiceCallback<T> {
        void onLoaded(T previsoes);
        void onError(String mensagem);
    }

    void getPrevisoesCidadesProximas(Coordenadas coordenadas, String unidade, PrevisaoServiceCallback<PrevisaoCompletaResultado> callback);
}
