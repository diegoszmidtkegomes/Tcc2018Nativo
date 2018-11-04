package ifrs.com.tcc2018.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

import ifrs.com.tcc2018.R;
import ifrs.com.tcc2018.model.iAsyncResponseObj;

/**
 * Created by diego on 11/12/2016.
 */
public class Localizacao implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        iAsyncResponseObj {

    private GoogleApiClient mGoogleApiClient;
    private Context contexto;
    private iAsyncResponseObj delegate = null;
    private android.location.Location l;
    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    public static boolean locationServicesEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean net_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            //Log.e(TAG,"Exception gps_enabled");
        }

        try {
            net_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            //Log.e(TAG,"Exception network_enabled");
        }
        return gps_enabled || net_enabled;
    }

    @SuppressLint("MissingPermission")
    public void buscaLocalizacao(Context context, final iAsyncResponseObj delegate) {
        this.delegate = delegate;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mLastLocation = location;
                            Log.d("diego", String.valueOf(mLastLocation.getLatitude() + " - " + mLastLocation.getLongitude()));
                            delegate.processoEncerrado(mLastLocation);
                        } else {
                            Log.d("diego", "sem lat");
                        }
                    }
                });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(contexto,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(contexto,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        l = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (l != null) {
            Log.i("LOG", "latitude: " + l.getLatitude());
            Log.i("LOG", "longitude: " + l.getLongitude());
            if (this.delegate != null) {
                //delegate.processoEncerrado(new Localizacao(l.getLatitude(), l.getLongitude()));
                delegate.processoEncerrado(l);
            }
            //new ConfiguracaoRepositorio().atualizaLocalizacao(l.getLatitude(), l.getLongitude());

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("diego", "erro");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("diego", "erro");
    }

    /**
     * Realiza conexao.
     *
     * @param context  the context
     * @param activity the activity
     * @param delegate the delegate
     */
    public synchronized void realizaConexao(Context context, Activity activity, iAsyncResponseObj delegate) {
        //new Permissao().permissaoLocation(activity);
        this.delegate = delegate;
        this.contexto = context;
        mGoogleApiClient = new GoogleApiClient.Builder(contexto)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void processoEncerrado(Object obj) {
        if (delegate != null) {
            delegate.processoEncerrado(l);
        }
    }

}
