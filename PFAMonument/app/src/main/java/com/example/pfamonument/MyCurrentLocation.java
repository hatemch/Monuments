package com.example.pfamonument;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MyCurrentLocation implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private OnLocationChangedListener onLocationChangedListener;

    // passe l'interface OnLocationChangedListener dans le constructeur de l'organisation
    // écoute l'événement de changement de lieu
    public MyCurrentLocation(OnLocationChangedListener onLocationChangedListener) {
        this. onLocationChangedListener = onLocationChangedListener;
    }

    /**
     * Crée GoogleApiClient. Utilise la méthode {@code #addApi} pour interroger
     * API LocationServices.
     */
    protected synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks( this)
                .addOnConnectionFailedListener( this )
                .addApi(LocationServices. API)
                .build();

        // crée une demande et définit l'intervalle d'envoi
        mLocationRequest = LocationRequest. create()
                .setPriority(LocationRequest. PRIORITY_HIGH_ACCURACY )
                .setInterval( 10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000 ); // 1 second, in milliseconds
    }

    public void start(){
        // Connecte le client aux services Google Play.
        mGoogleApiClient.connect();
    }

    public void stop(){
        // Ferme la connexion aux services Google Play.
        mGoogleApiClient.disconnect();
    }

    // Après l'appel de connect (), cette méthode sera appelée de manière asynchrone une fois la demande de connexion complétée.
    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi .requestLocationUpdates( mGoogleApiClient, mLocationRequest , this );
        mLastLocation = LocationServices.FusedLocationApi .getLastLocation(
                mGoogleApiClient);
        if ( mLastLocation != null ) {
            onLocationChangedListener.onLocationChanged( mLastLocation );
        }
    }

    // Appelé lorsque le client est temporairement hors ligne.
    @Override
    public void onConnectionSuspended( int i) {

    }

    // Appelé lorsqu'une erreur survient lors de la connexion du client au service.
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e( "MyApp" , "Location services connection failed with code " + connectionResult.getErrorCode());
    }

    /*
     * Implémentez la méthode onLocationChanged de l'interface LocationListener. Rappel qui se produit lorsque l'emplacement change.
     * Ici, nous créons un objet mLastLocation qui stocke le dernier emplacement et le transmettons à la méthode d’interface.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if ( mLastLocation != null ) {
            onLocationChangedListener.onLocationChanged( mLastLocation );
        }
    }
}
