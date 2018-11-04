package ifrs.com.tcc2018.service;

import android.content.Context;
import android.location.Location;
import android.util.Log;


import ifrs.com.tcc2018.model.iAsyncResponseObj;
import ifrs.com.tcc2018.util.Localizacao;

/**
 * Created by diego on 30/07/2017.
 */

public class RastreamentoHelper implements iAsyncResponseObj {

    private Context _context;
    private long startTime;

    public void buscaPosicao(Context context) {
        _context = context;
        buscaLatLong(context);

    }

    private void buscaLatLong(Context context) {
        startTime = System.currentTimeMillis();
        //new Localizacao().realizaConexao(context, null, this);
        new Localizacao().buscaLocalizacao(context, this);
    }

    @Override
    public void processoEncerrado(Object obj) {
        if (obj instanceof android.location.Location) {
            double latitude = ((Location) obj).getLatitude();
            double longitude = ((Location) obj).getLongitude();
            Log.d("diego", "processoEncerrado: " + latitude + " - " + longitude);
            long estimatedTime = (System.currentTimeMillis() - startTime);
            Log.v("diegotempo", String.valueOf(estimatedTime));
        }
    }

}
