package name.peterbukhal.android.redmine;

import android.app.Application;
import android.content.Context;

import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import name.peterbukhal.android.redmine.service.redmine.RedmineProvider;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class RedmineApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //noinspection ConstantConditions
        //if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            //return;
        //}

        //if (!BuildConfig.DEBUG) {
            //initYandexMetrica(this);
            //initFabric(this);
        //}

        initRealm(this);
        initPicasso(this);

        RedmineProvider.init(this, getString(R.string.redmine));
    }

    private void initRealm(final Context context) {
        Realm.init(context);
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build());
    }

    private void initPicasso(final Context context) {
        Picasso.setSingletonInstance(
                new Picasso.Builder(context)
                        .loggingEnabled(BuildConfig.DEBUG)
                        .indicatorsEnabled(BuildConfig.DEBUG)
                        .build());
    }

//    private void initFabric(final Context context) {
//        Fabric.with(context, new Crashlytics(), new Answers());
//    }
//
//    private void initYodaTime(Context context) {
//        JodaTimeAndroid.init(context);
//    }
//
//    private void initLeakCanary(final Application application) {
//        LeakCanary.install(application);
//    }


}
