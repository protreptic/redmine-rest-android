package name.peterbukhal.android.redmine.service;

import android.content.Context;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import name.peterbukhal.android.redmine.account.RedmineAccount;
import name.peterbukhal.android.redmine.realm.Author;
import name.peterbukhal.android.redmine.realm.AuthorDeserializer;
import name.peterbukhal.android.redmine.realm.CustomField;
import name.peterbukhal.android.redmine.realm.CustomFieldDeserializer;
import name.peterbukhal.android.redmine.realm.Issue;
import name.peterbukhal.android.redmine.realm.IssueDeserializer;
import name.peterbukhal.android.redmine.realm.Priority;
import name.peterbukhal.android.redmine.realm.PriorityDeserializer;
import name.peterbukhal.android.redmine.realm.Project;
import name.peterbukhal.android.redmine.realm.ProjectDeserializer;
import name.peterbukhal.android.redmine.realm.Status;
import name.peterbukhal.android.redmine.realm.StatusDeserializer;
import name.peterbukhal.android.redmine.realm.Tracker;
import name.peterbukhal.android.redmine.realm.TrackerDeserializer;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class RedmineProvider {

    private static Redmine sRedmine;

    public static void init(final Context context, final String serverName) {
        sRedmine = new Retrofit.Builder()
                .baseUrl(serverName)
                .client(new OkHttpClient.Builder()
                        .authenticator(new RedmineAuthenticator(context, new RedmineAccount("petr.bukhal@doconcall.ru", "9gvM3050")))
                        .addInterceptor(new Interceptor() {

                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                return chain.proceed(
                                        chain.request()
                                                .newBuilder()
                                                .addHeader("X-Redmine-API-Key", RedmineAuthenticator.sToken)
                                                .build());
                            }

                        })
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(BODY))
                        .build())
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder()
                                        .registerTypeAdapter(CustomField.class, new CustomFieldDeserializer())
                                        .registerTypeAdapter(Author.class, new AuthorDeserializer())
                                        .registerTypeAdapter(Priority.class, new PriorityDeserializer())
                                        .registerTypeAdapter(Status.class, new StatusDeserializer())
                                        .registerTypeAdapter(Tracker.class, new TrackerDeserializer())
                                        .registerTypeAdapter(Project.class, new ProjectDeserializer())
                                        .registerTypeAdapter(Issue.class, new IssueDeserializer())
                                        .create()))
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
