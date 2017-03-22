package name.peterbukhal.android.redmine.rest.service;

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
                .header("Authorization", Credentials.basic("dd6521eabc613b13d01061ee02ac8b60bb5ecbbb", ""))
                .build();
    }

}
