package com.pedroimai.previsaotempo.previsao;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedroimai.previsaotempo.R;
import com.pedroimai.previsaotempo.data.previsao.model.Coordenadas;
import com.pedroimai.previsaotempo.data.previsao.model.PrevisaoCompleta;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;
import com.pedroimai.previsaotempo.data.Constantes;

import java.util.ArrayList;
import java.util.List;

public class PrevisaoActivity extends AppCompatActivity implements PrevisaoFragment.Callback, OnMapReadyCallback {
    GoogleMap mMap;
    SupportMapFragment mMapFragment;
    PrevisaoFragment mPrevisaoFragment;
    CameraPosition mMyCameraPosition;
    ArrayList<Marker> mMarkers;
    ArrayList<PrevisaoCompleta> mPrevisoes;
    FragmentManager mFragmentManager;
    Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previsao_activity);

        if (null == savedInstanceState) {
            List<String> permissoesParaAprovacao = getPermissoesParaAprovacao();

            while (permissoesParaAprovacao.size() > 0) {
                requestPermissoes(permissoesParaAprovacao);
                permissoesParaAprovacao = getPermissoesParaAprovacao();
            }
            mFragmentManager = getSupportFragmentManager();
            mPrevisaoFragment = PrevisaoFragment.newInstance(Constantes.UNIDADE_CELSIUS);
            mMapFragment = SupportMapFragment.newInstance();
            alterarModoExibicaoLista();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.previsao_menu, menu);
        setMenuLista();
        setMenuCelsius();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_lista:
                setMenuLista();
                alterarModoExibicaoLista();
                break;
            case R.id.menu_mapa:
                setMenuMapa();
                alterarModoExibicaoMapa();
                break;
            case R.id.menu_Farenheit:
                setMenuFarenheit();
                setMenuLista();
                alterarFarenheit();
                break;
            case R.id.menu_Celsius:
                setMenuCelsius();
                setMenuLista();
                alterarCelsius();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void alterarCelsius() {
        removeFragmentsIfExists();
        mPrevisaoFragment = PrevisaoFragment.newInstance(Constantes.UNIDADE_CELSIUS);
        initFragment(mPrevisaoFragment);
    }

    private void alterarFarenheit() {
        removeFragmentsIfExists();
        mPrevisaoFragment = PrevisaoFragment.newInstance(Constantes.UNIDADE_FARENHEIT);
        initFragment(mPrevisaoFragment);
    }

    private void initFragment(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }


    @TargetApi(23)
    private List<String> getPermissoesParaAprovacao() {
        List<String> permissoes = new ArrayList<>();
        List<String> permissoesSolicitadas = new ArrayList<>();

        permissoes.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissoes.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissoes.add(Manifest.permission.INTERNET);

        for (String permissao : permissoes) {
            if (getApplicationContext().checkSelfPermission(permissao) != PackageManager.PERMISSION_GRANTED) {
                permissoesSolicitadas.add(permissao);
            }
        }
        return permissoesSolicitadas;
    }

    @TargetApi(23)
    private void requestPermissoes(List<String> permissoes) {

        if (!permissoes.isEmpty()) {
            String[] params = permissoes.toArray(new String[permissoes.size()]);
            requestPermissions(params, 1);
        }
    }

    private void updateMarkers() {
        if (mMap != null) {
            mMarkers = new ArrayList<>();
            if (mPrevisoes != null && mPrevisoes.size() > 0) {
                for (PrevisaoCompleta previsao : mPrevisoes) {
                    mMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(previsao.coordenadas.latitude, previsao.coordenadas.longitude))));
                }
            }
        }
    }

    private void markerClick(Marker marker) {
        int position = getPositionByMarker(marker);
        marker.setTitle(mPrevisoes.get(position).cidade);
        marker.showInfoWindow();
    }

    private int getPositionByMarker(Marker marker) {
        int position = 0;
        for (int i = 0; i < mMarkers.size(); i++) {
            if (mMarkers.get(i).getId().equals(marker.getId())) {
                position = i;
                break;
            }
        }
        return position;
    }


    @Override
    public void atualizarPrevisoes(ArrayList<PrevisaoCompleta> previsoes) {
        mPrevisoes = previsoes;
        if (mPrevisoes != null) {
            updateMarkers();
        }
    }

    @Override
    public void atualizarMinhasCoordenadas(Coordenadas coordenadas) {
        mMyCameraPosition = CameraPosition.builder()
                .target(new LatLng(coordenadas.latitude, coordenadas.longitude))
                .zoom(10)
                .bearing(0)
                .tilt(0)
                .build();
    }

    @Override
    public void alterarModoExibicaoMapa() {
        removeFragmentsIfExists();
        initFragment(mMapFragment);
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void alterarModoExibicaoLista() {
        removeFragmentsIfExists();
        initFragment(mPrevisaoFragment);
    }

    private void removeFragmentsIfExists() {
        if (mFragmentManager.getFragments() != null && mFragmentManager.getFragments().size() > 0) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            for (Fragment fragment : mFragmentManager.getFragments()) {
                if (fragment != null) {
                    transaction.remove(mFragmentManager.findFragmentById(fragment.getId()));
                }
            }
            transaction.commit();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mMyCameraPosition));
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker arg0) {
                View v = getLayoutInflater().inflate(R.layout.previsao_map_info_window, null);
                ImageView imgIcone = (ImageView) v.findViewById(R.id.icone);
                TextView tvTemperatura = (TextView) v.findViewById(R.id.temperatura);

                String temperatura = String.format("%.0f°", (mPrevisoes.get(getPositionByMarker(arg0)).temperatura.atual));
                String icone = String.valueOf(mPrevisoes.get(getPositionByMarker(arg0)).previsoes.get(0).icone);
                tvTemperatura.setText(temperatura);

                String iconeUrl = String.format("http://openweathermap.org/img/w/%s.png", icone);

                Picasso.with(PrevisaoActivity.this)
                        .load(iconeUrl)
                        .into(imgIcone, new MarkerCallback(arg0));


                return v;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markerClick(marker);
                return true;
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        updateMarkers();
    }


    private void setMenuMapa() {
        mMenu.findItem(R.id.menu_lista).setVisible(true);
        mMenu.findItem(R.id.menu_mapa).setVisible(false);
    }

    private void setMenuLista() {
        mMenu.findItem(R.id.menu_lista).setVisible(false);
        mMenu.findItem(R.id.menu_mapa).setVisible(true);
    }

    private void setMenuFarenheit() {
        mMenu.findItem(R.id.menu_Celsius).setVisible(true);
        mMenu.findItem(R.id.menu_Farenheit).setVisible(false);
    }

    private void setMenuCelsius() {
        mMenu.findItem(R.id.menu_Celsius).setVisible(false);
        mMenu.findItem(R.id.menu_Farenheit).setVisible(true);
    }

    private class MarkerCallback implements Callback {
        Marker marker = null;

        MarkerCallback(Marker marker) {
            this.marker = marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }

}
