package com.pedroimai.previsaotempo;

import com.pedroimai.previsaotempo.data.previsao.PrevisaoServiceApi;
import com.pedroimai.previsaotempo.data.previsao.model.Coordenadas;
import com.pedroimai.previsaotempo.data.previsao.model.PrevisaoCompletaResultado;

public class FakePrevisaoServiceImpl implements PrevisaoServiceApi {
    private static final PrevisaoCompletaResultado PREVISAO_COMPLETA_RESULTADO = new PrevisaoCompletaResultado();
    @Override
    public void getPrevisoesCidadesProximas(Coordenadas coordenadas, String unidade, PrevisaoServiceCallback<PrevisaoCompletaResultado> callback) {
        callback.onLoaded(PREVISAO_COMPLETA_RESULTADO);
    }
}
