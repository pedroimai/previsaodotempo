package com.pedroimai.previsaotempo.data.previsao.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PrevisaoCompletaResultado {
    @SerializedName("list")
    public List<PrevisaoCompleta> previsoes;

    public PrevisaoCompletaResultado(){}

    public PrevisaoCompletaResultado(List<PrevisaoCompleta> previsoes) {
        this.previsoes = previsoes;
    }
}
