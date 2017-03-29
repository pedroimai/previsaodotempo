package com.pedroimai.previsaotempo.data.previsao.model;

import com.google.gson.annotations.SerializedName;

public class Coordenadas {
    @SerializedName("lat")
    public double latitude;

    @SerializedName("lon")
    public double longitude;

    public Coordenadas() {
    }

    public Coordenadas(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
