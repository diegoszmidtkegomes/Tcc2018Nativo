package ifrs.com.tcc2018.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class RastreamentoService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("MyService", "Job rastreamento");
        processaRastreamento();
        return true;
    }

    private void processaRastreamento() {
        try {
            new RastreamentoHelper().buscaPosicao(getApplicationContext());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MyService", "myService destroyed");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MyService", "myService created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags,
                              int startId) {
        Log.i("MyService", "myService onStartCommand");
        Messenger callback = intent.getParcelableExtra("messenger");
        Message m = Message.obtain();
        m.what = 2;
        m.obj = this;
        try {
            callback.send(m);
        } catch (RemoteException e) {
            Log.e("MyService", "Error passing service object back to activity.");
        }
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("MyService", "Job Stopped");
        return false;
    }
}
