package ifrs.com.tcc2018.app;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Tcc2018App  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("tcc2018.realm")
                .schemaVersion(11)
                //.migration(new Migration())
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getInstance(realmConfiguration);
        realm.close();
    }

    @Override
    public void onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }
}
