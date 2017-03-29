package com.pedroimai.previsaotempo;

import com.pedroimai.previsaotempo.data.localizacao.LocalizacaoServiceApi;
import com.pedroimai.previsaotempo.data.previsao.model.Coordenadas;

public class FakeLocalizacaoServiceImpl implements LocalizacaoServiceApi {
    private static final Coordenadas COORDENADAS_FAKE = new Coordenadas();

    @Override
    public void getMinhasCoordenadas(LocalizacaoServiceCallback<Coordenadas> callback) {
        callback.onCoordenadasReady(COORDENADAS_FAKE);
    }
}
