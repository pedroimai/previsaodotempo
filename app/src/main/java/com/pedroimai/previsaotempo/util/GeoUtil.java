package com.pedroimai.previsaotempo.util;

import com.google.android.gms.maps.model.LatLng;
import com.pedroimai.previsaotempo.data.previsao.model.Coordenadas;

/**
 * Created by Pedro on 29/03/2017.
 */

public class GeoUtil {
    public static int getDistanceBetween2LatLng(Coordenadas pontoA, Coordenadas pontoB) {
        if(pontoA != null && pontoB !=null) {
            String distancia = "";
            int Radius = 6371;// radius of earth in Km
            double lat1 = pontoA.latitude;
            double lat2 = pontoB.latitude;
            double lon1 = pontoA.longitude;
            double lon2 = pontoB.longitude;
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1))
                    * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                    * Math.sin(dLon / 2);
            double c = 2 * Math.asin(Math.sqrt(a));
            double valueResult = Radius * c;

            return (int) Math.floor(valueResult * 1000);
        }
        else{
            return -1;
        }
    }
}
