package name.peterbukhal.android.redmine.service.redmine;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

final class RedmineAuthenticatorInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(
                chain.request()
                        .newBuilder()
                        .addHeader("X-Redmine-API-Key", "74507d333a0b64320af80d32a45ddd94adf65684")
                        .build());
    }

}
