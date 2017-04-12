package name.peterbukhal.android.redmine.service;

import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;

import java.io.IOException;

import name.peterbukhal.android.redmine.account.RedmineAccount;
import name.peterbukhal.android.redmine.account.RedmineAccountManager;
import name.peterbukhal.android.redmine.service.model.User;
import name.peterbukhal.android.redmine.service.response.CurrentResponse;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public final class RedmineAuthenticator implements Authenticator {

    private Context mContext;
    private RedmineAccount mAccount;
    public static String sToken = "abc";

    RedmineAuthenticator(final Context context, final RedmineAccount account) {
        mContext = context;
        mAccount = account;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        try {
            final retrofit2.Response<CurrentResponse> response1 =
                    RedmineProvider.provide().currentUser(Credentials.basic(mAccount.getName(), mAccount.getPassword())).execute();

            if (response1.isSuccessful()) {
                final User currentUser = response1.body().getCurrentUser();

                sToken = currentUser.getApiKey();
            }
        } catch (Exception e) {
            //
        }

        return response.request()
                .newBuilder()
                .header("Authorization", Credentials.basic(mAccount.getName(), mAccount.getPassword()))
                .build();
    }

}
