package com.pedroimai.previsaotempo.previsao;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pedroimai.previsaotempo.R;
import com.pedroimai.previsaotempo.data.Constantes;
import com.pedroimai.previsaotempo.data.localizacao.LocalizacaoServiceApi;
import com.pedroimai.previsaotempo.data.localizacao.LocalizacaoServiceImpl;
import com.pedroimai.previsaotempo.data.previsao.PrevisaoServiceImpl;
import com.pedroimai.previsaotempo.data.previsao.model.Coordenadas;
import com.pedroimai.previsaotempo.data.previsao.model.PrevisaoCompleta;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PrevisaoFragment extends Fragment implements PrevisaoContract.View {
    PrevisaoContract.UserActionsListener mActionsListener;
    PrevisaoAdapter mListAdapter;
    static String mUnidade;

    public interface Callback {
        void atualizarPrevisoes(ArrayList<PrevisaoCompleta> previsoes);
        void atualizarMinhasCoordenadas(Coordenadas coordenadas);
        void alterarModoExibicaoMapa();
        void alterarModoExibicaoLista();
    }

    public PrevisaoFragment() {
    }

    public static PrevisaoFragment newInstance(String unidade) {
        mUnidade = unidade;
        return new PrevisaoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new PrevisaoAdapter(new ArrayList<PrevisaoCompleta>(0));
        mActionsListener = new PrevisaoPresenter(Constantes.EXIBICAO_LISTA, new LocalizacaoServiceImpl(getContext()), new PrevisaoServiceImpl(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.carregarCoordenadas(new LocalizacaoServiceApi.LocalizacaoServiceCallback<Coordenadas>() {
            @Override
            public void onCoordenadasReady(Coordenadas coordenadas) {
                mActionsListener.atualizarCoordenadas(coordenadas);
                mActionsListener.carregarPrevisoes(mUnidade);
                ((PrevisaoActivity) getActivity()).atualizarMinhasCoordenadas(coordenadas);
            }

            @Override
            public void onCoordenadasFail() {
                Toast.makeText(getContext(), "Erro ao obter coordenadas", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.previsao_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.previsoes_list);
        recyclerView.setAdapter(mListAdapter);

        int numColumns = 1;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mActionsListener.carregarPrevisoes(mUnidade);
            }
        });
        return root;
    }

    @Override
    public void exibirCarregando(final boolean ativo) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(ativo);
            }
        });
    }

    @Override
    public void exibirErroDeCarregamentoDePrevisao(String mensagem) {
        Toast.makeText(getContext(),mensagem,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void atualizarListaPrevisoes(List<PrevisaoCompleta> previsoes) {
        mListAdapter.replaceData(previsoes);
        ((PrevisaoActivity) getActivity()).atualizarPrevisoes((ArrayList<PrevisaoCompleta>) previsoes);
    }


    private static class PrevisaoAdapter extends RecyclerView.Adapter<PrevisaoAdapter.ViewHolder> {
        private List<PrevisaoCompleta> mPrevisoes;

        public PrevisaoAdapter(List<PrevisaoCompleta> filmes) {
            setList(filmes);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.previsao_item, parent, false);

            return new ViewHolder(noteView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            PrevisaoCompleta previsaoCompleta = mPrevisoes.get(position);
            String iconeUrl = String.format("http://openweathermap.org/img/w/%s.png", previsaoCompleta.previsoes.get(0).icone);
            Picasso.with(viewHolder.icone.getContext())
                    .load(iconeUrl)
                    .fit().centerCrop()
                    .into(viewHolder.icone);

            viewHolder.cidade.setText(previsaoCompleta.cidade);
            viewHolder.descricao.setText(previsaoCompleta.previsoes.get(0).descricao);
            viewHolder.temperatura.setText(String.format("%.0f°", previsaoCompleta.temperatura.atual));
            viewHolder.temperaturaMinima.setText(String.format("%.0f°", previsaoCompleta.temperatura.minima));
            viewHolder.temperaturaMaxima.setText(String.format("%.0f°", previsaoCompleta.temperatura.maxima));
        }

        public void replaceData(List<PrevisaoCompleta> previsoes) {
            setList(previsoes);
            notifyDataSetChanged();
        }

        private void setList(List<PrevisaoCompleta> previsoes) {
            mPrevisoes = previsoes;
        }

        @Override
        public int getItemCount() {
            return mPrevisoes.size();
        }

        public PrevisaoCompleta getItem(int position) {
            return mPrevisoes.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView icone;
            public TextView cidade;
            public TextView descricao;
            public TextView temperatura;
            public TextView temperaturaMinima;
            public TextView temperaturaMaxima;

            public ViewHolder(View itemView) {
                super(itemView);
                cidade = (TextView) itemView.findViewById(R.id.cidade);
                icone = (ImageView) itemView.findViewById(R.id.icone);
                descricao = (TextView) itemView.findViewById(R.id.descricao);
                temperatura = (TextView) itemView.findViewById(R.id.temperatura);
                temperaturaMinima = (TextView) itemView.findViewById(R.id.temperaturaMinima);
                temperaturaMaxima = (TextView) itemView.findViewById(R.id.temperaturaMaxima);
            }
                    }
    }

}
