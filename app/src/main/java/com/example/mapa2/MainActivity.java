package com.example.mapa2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap map;
    private Marker markerSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        Toast.makeText(
                this,
                "Mapa de Google cargado",
                Toast.LENGTH_SHORT
        ).show();

        LatLng puntoPrincipal =
                new LatLng(-33.498895, -70.616617);

        LatLng puntoRepartidor =
                new LatLng(-33.498720, -70.616130);

        LatLng puntoPolicia =
                new LatLng(-33.498561, -70.615666);


        map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        puntoPrincipal,
                        17
                )
        );

        map.addMarker(
                new MarkerOptions()
                        .position(puntoPrincipal)
                        .title("Punto principal")
                        .snippet("Ubicación principal")
        );


        map.addMarker(
                new MarkerOptions()
                        .position(puntoRepartidor)
                        .title("Repartidor")
                        .snippet("Repartidor cerca")
                        .icon(
                                BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_BLUE
                                )
                        )
        );


        map.addMarker(
                new MarkerOptions()
                        .position(puntoPolicia)
                        .title("Policía")
                        .snippet("Punto de referencia")
                        .icon(
                                BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_GREEN
                                )
                        )
        );



        map.setOnMapClickListener(
                new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng punto) {

                        double latitud = punto.latitude;
                        double longitud = punto.longitude;

                        Log.d(
                                "MAPA",
                                "Latitud: " + latitud +
                                        " Longitud: " + longitud
                        );


                        // Eliminar marcador seleccionado anterior

                        if (markerSeleccionado != null) {
                            markerSeleccionado.remove();
                        }


                        // Crear nuevo marcador

                        markerSeleccionado =
                                map.addMarker(
                                        new MarkerOptions()
                                                .position(punto)
                                                .title("Punto seleccionado")
                                                .snippet(
                                                        "Lat: " + latitud +
                                                                "\nLon: " + longitud
                                                )
                                );
                    }
                }
        );
    }
}