package name.peterbukhal.android.redmine.service.redmine;

import android.content.Context;

import com.google.gson.GsonBuilder;

import java.lang.ref.WeakReference;

import name.peterbukhal.android.redmine.BuildConfig;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.realm.Author;
import name.peterbukhal.android.redmine.realm.CustomField;
import name.peterbukhal.android.redmine.realm.Issue;
import name.peterbukhal.android.redmine.realm.Priority;
import name.peterbukhal.android.redmine.realm.Project;
import name.peterbukhal.android.redmine.realm.StatusRealm;
import name.peterbukhal.android.redmine.realm.Tracker;
import name.peterbukhal.android.redmine.realm.User;
import name.peterbukhal.android.redmine.realm.deserializer.AuthorDeserializer;
import name.peterbukhal.android.redmine.realm.deserializer.CustomFieldDeserializer;
import name.peterbukhal.android.redmine.realm.deserializer.IssueDeserializer;
import name.peterbukhal.android.redmine.realm.deserializer.PriorityDeserializer;
import name.peterbukhal.android.redmine.realm.deserializer.ProjectDeserializer;
import name.peterbukhal.android.redmine.realm.deserializer.StatusDeserializer;
import name.peterbukhal.android.redmine.realm.deserializer.TrackerDeserializer;
import name.peterbukhal.android.redmine.realm.deserializer.UserDeserializer;
import name.peterbukhal.android.redmine.util.ContextProvider;
import okhttp3.OkHttpClient;
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

    public static void init() {
        WeakReference<Context> context =
                new WeakReference<>(ContextProvider.context);

        sRedmine = new Retrofit.Builder()
                .baseUrl(context.get().getString(R.string.redmine))
                .validateEagerly(BuildConfig.DEBUG)
                .client(new OkHttpClient.Builder()
                        .authenticator(new RedmineAuthenticator())
                        .addInterceptor(new RedmineAuthenticatorInterceptor())
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(BODY))
                        .build())
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder()
                                        .registerTypeAdapter(User.class, new UserDeserializer())
                                        .registerTypeAdapter(CustomField.class, new CustomFieldDeserializer())
                                        .registerTypeAdapter(Author.class, new AuthorDeserializer())
                                        .registerTypeAdapter(Priority.class, new PriorityDeserializer())
                                        .registerTypeAdapter(StatusRealm.class, new StatusDeserializer())
                                        .registerTypeAdapter(Tracker.class, new TrackerDeserializer())
                                        .registerTypeAdapter(Project.class, new ProjectDeserializer())
                                        .registerTypeAdapter(Issue.class, new IssueDeserializer())
                                        .create()))
                .build()
                .create(Redmine.class);
    }

    public static Redmine provide() {
        if (sRedmine == null) {
            throw new RuntimeException("Redmine not initialized!");
        }

        return sRedmine;
    }

}
