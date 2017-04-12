package name.peterbukhal.android.redmine;

import android.app.Application;
import android.content.Context;

import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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

        initRealm(this);
        initPicasso(this);
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

}
