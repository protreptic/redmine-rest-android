package name.peterbukhal.android.redmine.account.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by
 *      petronic on 10.05.16.
 */
public final class RedmineAuthenticatorService extends Service {

    private RedmineAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new RedmineAuthenticator(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

}
