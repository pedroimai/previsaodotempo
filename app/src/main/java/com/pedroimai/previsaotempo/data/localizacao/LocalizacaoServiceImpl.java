package com.pedroimai.previsaotempo.data.localizacao;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.pedroimai.previsaotempo.data.previsao.model.Coordenadas;


public class LocalizacaoServiceImpl implements LocalizacaoServiceApi, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;
    Context mContext;
    Location mLocationServices;
    LocalizacaoServiceCallback<Coordenadas> mCallbackCoordenadas;

    public LocalizacaoServiceImpl() {
    }

    public LocalizacaoServiceImpl(Context context) {
        mContext = context;
    }

    private synchronized void connectGoogleApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void getMinhasCoordenadas(LocalizacaoServiceCallback<Coordenadas> callback) {
        mCallbackCoordenadas = callback;
        connectGoogleApi();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationServices = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if(mLocationServices != null){
            mCallbackCoordenadas.onCoordenadasReady(new Coordenadas(mLocationServices.getLatitude(),mLocationServices.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
