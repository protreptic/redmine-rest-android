package name.peterbukhal.android.redmine.service.redmine;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

final class RedmineAuthenticator implements Authenticator {

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        return response.request()
                .newBuilder()
                .header("Authorization", Credentials.basic("", ""))
                .build();
    }

}