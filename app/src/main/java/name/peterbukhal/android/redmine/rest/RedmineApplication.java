package name.peterbukhal.android.redmine.rest;

import android.app.Application;

import name.peterbukhal.android.redmine.rest.service.RedmineProvider;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public class RedmineApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RedmineProvider.init(this);
    }

}
