package name.peterbukhal.android.redmine;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import name.peterbukhal.android.redmine.service.RedmineProvider;

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

        RedmineProvider.init(this);

        Realm.init(this);
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build());
    }

}
