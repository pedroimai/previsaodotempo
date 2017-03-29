package com.pedroimai.previsaotempo.data.previsao.model;

import com.google.gson.annotations.SerializedName;

public class Temperatura {
    public static final String CELSIUS = "metric";
    public static final String FARENHEIT = "imperial";

    @SerializedName("temp")
    public double atual;

    @SerializedName("temp_min")
    public double minima;

    @SerializedName("temp_max")
    public double maxima;

    public Temperatura() {
    }

}
