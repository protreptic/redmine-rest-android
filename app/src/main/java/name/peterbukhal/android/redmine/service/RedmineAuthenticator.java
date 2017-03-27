package name.peterbukhal.android.redmine.service;

import android.content.Context;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
final class RedmineAuthenticator implements Authenticator {

    RedmineAuthenticator(final Context context) {

    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        return response.request()
                .newBuilder()
                .header("Authorization", Credentials.basic("b9bdab9aaf22aa9b5cbfc9071a34da8538913629", ""))
                .build();
    }

}
