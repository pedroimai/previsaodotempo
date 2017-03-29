package com.pedroimai.previsaotempo;


import com.pedroimai.previsaotempo.data.Constantes;
import com.pedroimai.previsaotempo.data.localizacao.LocalizacaoServiceApi;
import com.pedroimai.previsaotempo.data.previsao.PrevisaoServiceApi;
import com.pedroimai.previsaotempo.data.previsao.model.Coordenadas;
import com.pedroimai.previsaotempo.data.previsao.model.PrevisaoCompleta;
import com.pedroimai.previsaotempo.data.previsao.model.PrevisaoCompletaResultado;
import com.pedroimai.previsaotempo.previsao.PrevisaoContract;
import com.pedroimai.previsaotempo.previsao.PrevisaoPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class PrevisaoPresenterTest {
    private static ArrayList<PrevisaoCompleta> PREVISOES = new ArrayList<PrevisaoCompleta>()
    {{
        add(new PrevisaoCompleta("Suzano",-23.614113,-46.4601027,20,20,18,"Sol","10d",100));
    }};

    private static Coordenadas COORDENADAS = new Coordenadas(-23.614113,-46.4601027);

    private static PrevisaoCompletaResultado PREVISOES_RESULTADO = new PrevisaoCompletaResultado(PREVISOES);

    @Mock
    private FakePrevisaoServiceImpl mPrevisaoApi;

    @Mock
    private FakeLocalizacaoServiceImpl mLocalizacaoApi;

    @Mock
    private PrevisaoContract.View mPrevisaoView;

    @Captor
    private ArgumentCaptor<PrevisaoServiceApi.PrevisaoServiceCallback> mCarregaPrevisoesCallbackCaptor;
    @Captor
    private ArgumentCaptor<LocalizacaoServiceApi.LocalizacaoServiceCallback> mCarregaCoordenadasCallbackCaptor;

    private PrevisaoPresenter mPrevisaoPresenter;

    private int mModoExibicao = Constantes.EXIBICAO_LISTA;
    private String mUnidade = Constantes.UNIDADE_CELSIUS;

    @Before
    public void setupPrevisaoPresenter() {
        MockitoAnnotations.initMocks(this);
        mPrevisaoPresenter = new PrevisaoPresenter(mModoExibicao,mLocalizacaoApi,mPrevisaoApi,mPrevisaoView);
    }

    @Test
    public void carregaPrevisoesEPopulaATela(){
        mPrevisaoPresenter.carregarPrevisoes(mUnidade);

        verify(mPrevisaoApi).getPrevisoesCidadesProximas( any(Coordenadas.class) ,eq(mUnidade),mCarregaPrevisoesCallbackCaptor.capture());
        mCarregaPrevisoesCallbackCaptor.getValue().onLoaded(PREVISOES_RESULTADO);

        verify(mPrevisaoView).exibirCarregando(false);
        verify(mPrevisaoView).atualizarListaPrevisoes(PREVISOES_RESULTADO.previsoes);
    }

}
