package name.peterbukhal.android.redmine.account.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import name.peterbukhal.android.redmine.account.RedmineAccountManager;
import name.peterbukhal.android.redmine.activity.LoginActivity;
import name.peterbukhal.android.redmine.service.RedmineProvider;
import name.peterbukhal.android.redmine.service.model.User;
import name.peterbukhal.android.redmine.service.response.CurrentResponse;
import okhttp3.Credentials;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by
 *      petronic on 10.05.16.
 */
public final class RedmineAuthenticator extends AbstractAccountAuthenticator {

    private final Context mContext;
    private final RedmineAccountManager mAccountManager;

    public RedmineAuthenticator(Context context) {
        super(context);

        mContext = context;
        mAccountManager = RedmineAccountManager.get(context);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
                             String authTokenType, String[] requiredFeatures, Bundle options)
            throws NetworkErrorException {
        final Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);

        final Bundle bundle = new Bundle();
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
        final String serverName = mAccountManager.getServer(account);
        final String token = mAccountManager.peekAuthToken(account);

        if (!token.equals("#")) {
            Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, token);

            return result;
        }

        RedmineProvider.init(mContext, serverName);

        try {
            final Response<CurrentResponse> response =
                    RedmineProvider.provide().currentUser(Credentials.basic(account.name, mAccountManager.getPassword(account))).execute();

            if (response.isSuccessful()) {
                final User currentUser = response.body().getCurrentUser();

                Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                result.putString(AccountManager.KEY_AUTHTOKEN, currentUser.getApiKey());

                mAccountManager.setDefaultAccount(account);
                mAccountManager.setToken(account, currentUser.getApiKey());

                name.peterbukhal.android.redmine.service.RedmineAuthenticator.sToken = currentUser.getApiKey();

                return result;
            }
        } catch (Exception e) {
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
