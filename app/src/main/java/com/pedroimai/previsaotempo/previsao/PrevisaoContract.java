package com.pedroimai.previsaotempo.previsao;

import com.google.android.gms.maps.GoogleMap;
import com.pedroimai.previsaotempo.data.localizacao.LocalizacaoServiceApi;
import com.pedroimai.previsaotempo.data.previsao.model.Coordenadas;
import com.pedroimai.previsaotempo.data.previsao.model.PrevisaoCompleta;

import java.util.List;

public interface PrevisaoContract {
    interface View {
        void exibirCarregando(boolean isAtivo);
        void exibirErroDeCarregamentoDePrevisao(String mensagem);
        void atualizarListaPrevisoes(List<PrevisaoCompleta> previsoes);
    }

    interface UserActionsListener {
        void carregarPrevisoes(String unidade);
        void carregarCoordenadas(LocalizacaoServiceApi.LocalizacaoServiceCallback<Coordenadas> callback);
        void atualizarCoordenadas(Coordenadas coordenadas);
        void atualizarDistancias();
    }

}
