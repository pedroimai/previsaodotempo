package com.pedroimai.previsaotempo.previsao;

import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.pedroimai.previsaotempo.data.Constantes;
import com.pedroimai.previsaotempo.data.localizacao.LocalizacaoServiceApi;
import com.pedroimai.previsaotempo.data.previsao.PrevisaoServiceApi;
import com.pedroimai.previsaotempo.data.previsao.model.Coordenadas;
import com.pedroimai.previsaotempo.data.previsao.model.PrevisaoCompleta;
import com.pedroimai.previsaotempo.data.previsao.model.PrevisaoCompletaResultado;
import com.pedroimai.previsaotempo.util.GeoUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrevisaoPresenter implements PrevisaoContract.UserActionsListener {
    private final PrevisaoServiceApi mPrevisaoApi;
    private final LocalizacaoServiceApi mLocalizacaoApi;
    private final PrevisaoContract.View mPrevisoesView;
    private Coordenadas mCoordenadas;
    private int mModoExibicao;
    private ArrayList<PrevisaoCompleta> mPrevisoes;

    public PrevisaoPresenter(int modoExibicao,LocalizacaoServiceApi localizacaoApi, PrevisaoServiceApi previsaoApi, PrevisaoContract.View previsoesView) {
        mLocalizacaoApi = localizacaoApi;
        mPrevisaoApi = previsaoApi;
        mPrevisoesView = previsoesView;
    }

    @Override
    public void carregarPrevisoes(String unidade) {
        mPrevisoesView.exibirCarregando(true);

        mPrevisaoApi.getPrevisoesCidadesProximas(mCoordenadas, unidade, new PrevisaoServiceApi.PrevisaoServiceCallback<PrevisaoCompletaResultado>() {
            @Override
            public void onLoaded(PrevisaoCompletaResultado resultado) {
                mPrevisoes = (ArrayList<PrevisaoCompleta>) resultado.previsoes;
                atualizarDistancias();
                Collections.sort(mPrevisoes);
                mPrevisoesView.exibirCarregando(false);
                mPrevisoesView.atualizarListaPrevisoes(resultado.previsoes);
            }

            @Override
            public void onError(String mensagem) {
                mPrevisoesView.exibirErroDeCarregamentoDePrevisao(mensagem);
                mPrevisoesView.exibirCarregando(false);
            }
        });
    }

    @Override
    public void carregarCoordenadas(final LocalizacaoServiceApi.LocalizacaoServiceCallback<Coordenadas> callback) {
        mLocalizacaoApi.getMinhasCoordenadas(new LocalizacaoServiceApi.LocalizacaoServiceCallback<Coordenadas>() {
            @Override
            public void onCoordenadasReady(Coordenadas coordenadas) {
                callback.onCoordenadasReady(coordenadas);
            }

            @Override
            public void onCoordenadasFail() {
                callback.onCoordenadasFail();
            }
        });
    }

    @Override
    public void atualizarCoordenadas(Coordenadas coordenadas) {
        mCoordenadas = coordenadas;
    }

    @Override
    public void atualizarDistancias() {
        for(PrevisaoCompleta previsao:mPrevisoes){
            previsao.distancia = GeoUtil.getDistanceBetween2LatLng(mCoordenadas, previsao.coordenadas);
        }
    }

}
