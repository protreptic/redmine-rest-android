package name.peterbukhal.android.redmine.service;

import android.content.Context;

import name.peterbukhal.android.redmine.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class RedmineProvider {

    private static Redmine sRedmine;

    public static void init(final Context context) {
        sRedmine = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.redmine))
                .client(new OkHttpClient.Builder()
                        .authenticator(new RedmineAuthenticator(context))
                        .addInterceptor(new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Redmine.class);
    }

    public static Redmine provide() {
        if (sRedmine == null) {
            throw new RuntimeException("Fruits not initialized!");
        }

        return sRedmine;
    }

}
