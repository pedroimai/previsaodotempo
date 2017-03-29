package com.pedroimai.previsaotempo.data.previsao.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PrevisaoCompleta implements Comparable{
    @SerializedName("name")
    public String cidade;

    @SerializedName("coord")
    public Coordenadas coordenadas;

    @SerializedName("main")
    public Temperatura temperatura;

    @SerializedName("weather")
    public ArrayList<Previsao> previsoes;

    public int distancia;

    public PrevisaoCompleta() {
    }

    public PrevisaoCompleta(String cidade, double lat, double lon, double tempAtual, double tempMax, double tempMin, final String descricao, String icone, int distancia) {
        this.cidade = cidade;
        this.coordenadas = new Coordenadas();
        this.coordenadas.latitude = lat;
        this.coordenadas.longitude = lon;
        this.temperatura = new Temperatura();
        this.temperatura.atual = tempAtual;
        this.temperatura.maxima = tempMax;
        this.temperatura.minima = tempMin;
        this.previsoes = new ArrayList<>();
        this.previsoes.add(new Previsao(descricao,icone));
        this.distancia = distancia;
    }



    @Override
    public int compareTo(Object another) {
        int compareDistancia=((PrevisaoCompleta)another).distancia;
        /* For Ascending order*/
        return this.distancia-compareDistancia;
    }
}
