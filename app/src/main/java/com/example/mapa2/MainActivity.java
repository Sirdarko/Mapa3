package com.example.mapa2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Marker markerSeleccionado;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
                        .snippet(
                                "Lat: " + puntoPrincipal.latitude +
                                        "\nLon: " + puntoPrincipal.longitude
                        )
        );

        map.addMarker(
                new MarkerOptions()
                        .position(puntoRepartidor)
                        .title("Repartidor")
                        .snippet(
                                "Lat: " + puntoRepartidor.latitude +
                                        "\nLon: " + puntoRepartidor.longitude
                        )
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
                        .snippet(
                                "Lat: " + puntoPolicia.latitude +
                                        "\nLon: " + puntoPolicia.longitude
                        )
                        .icon(
                                BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_GREEN
                                )
                        )
        );


        habilitarGeolocalizacion();

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

                        if (markerSeleccionado != null) {
                            markerSeleccionado.remove();
                        }

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

    private void habilitarGeolocalizacion() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );

            return;
        }

        map.setMyLocationEnabled(true);

        obtenerUbicacionActual();
    }


    private void obtenerUbicacionActual() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    if (location != null) {

                        double latitud = location.getLatitude();
                        double longitud = location.getLongitude();

                        Log.d(
                                "GEOLOCALIZACION",
                                "Latitud: " + latitud +
                                        " Longitud: " + longitud
                        );

                        Toast.makeText(
                                this,
                                "Ubicación encontrada",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                this,
                                "No se pudo obtener la ubicación",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                habilitarGeolocalizacion();

            } else {

                Toast.makeText(
                        this,
                        "Permiso de ubicación denegado",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}