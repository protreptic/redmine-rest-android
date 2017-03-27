package name.peterbukhal.android.redmine.rest.account.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;

import name.peterbukhal.android.redmine.rest.account.RedmineAccountManager;
import name.peterbukhal.android.redmine.rest.activity.LoginActivity;
import name.peterbukhal.android.redmine.rest.service.Redmine;
import name.peterbukhal.android.redmine.rest.service.RedmineProvider;
import name.peterbukhal.android.redmine.rest.service.model.User;
import name.peterbukhal.android.redmine.rest.service.response.CurrentResponse;
import retrofit2.Response;

/**
 * Created by
 *      petronic on 10.05.16.
 */
final class RedmineAuthenticator extends AbstractAccountAuthenticator {

    private final Context mContext;
    private final RedmineAccountManager mAccountManager;
    private final Redmine mRedmine;

    RedmineAuthenticator(Context context) {
        super(context);

        mContext = context;
        mAccountManager = RedmineAccountManager.get(context);
        mRedmine = RedmineProvider.provide();
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
                             String authTokenType, String[] requiredFeatures, Bundle options)
            throws NetworkErrorException {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account,
                                     Bundle options) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse authResponse, final Account account,
                               String authTokenType, Bundle options) throws NetworkErrorException {
        final String token = mAccountManager.peekAuthToken(account);

        if (!token.equals("#")) {
            Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, token);

            return result;
        }

        try {
            final Response<CurrentResponse> response = mRedmine.currentUser().execute();

            if (response.isSuccessful()) {
                final User currentUser = response.body().getCurrentUser();

                Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                result.putString(AccountManager.KEY_AUTHTOKEN, currentUser.getApiKey());

                mAccountManager.setToken(account, currentUser.getApiKey());

                return result;
            }
        } catch (IOException e) {
            //
        }

        return Bundle.EMPTY;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
                                    String authTokenType, Bundle options) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
                              String[] features) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

}
