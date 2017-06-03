package name.peterbukhal.android.redmine.service.redmine;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public final class RedmineAuthenticator implements Authenticator {

    private OkHttpClient mHttpClient;

    public RedmineAuthenticator() {
        mHttpClient = new OkHttpClient.Builder().build();
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {


        return response.request()
                .newBuilder()
                .header("Authorization", Credentials.basic("", ""))
                .build();
    }

}
