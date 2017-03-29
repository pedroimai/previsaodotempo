package com.pedroimai.previsaotempo.data.previsao.model;

import com.google.gson.annotations.SerializedName;

public class Previsao {
    @SerializedName("description")
    public String descricao ;

    @SerializedName("icon")
    public String icone;


    public Previsao() {
    }

    public Previsao(String descricao, String icone) {
        this.descricao = descricao;
        this.icone = icone;
    }

}
