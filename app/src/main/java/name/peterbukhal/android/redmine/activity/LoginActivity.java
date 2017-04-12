package name.peterbukhal.android.redmine.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.account.RedmineAccount;
import name.peterbukhal.android.redmine.account.RedmineAccountManager;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 23.03.2017.
 */
public final class LoginActivity extends AppCompatActivity {

    private RedmineAccountManager mAccountManager;
    private RegistrationTask mAuthTask;

    @BindView(R.id.servername)
    EditText mEtServerName;

    @BindView(R.id.username)
    EditText mEtLogin;

    @BindView(R.id.password)
    EditText mEtPassword;

    private Unbinder mUnbinder;

    private String mUserName;
    private String mPassword;

    private AccountAuthenticatorResponse mAccountAuthenticatorResponse;
    private Bundle mResultBundle = null;

    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

    public void finish() {
        if (mAccountAuthenticatorResponse != null) {
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(
                        AccountManager.ERROR_CODE_CANCELED, "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }

        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccountAuthenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }

        setContentView(R.layout.a_login);

        mUnbinder = ButterKnife.bind(this);

        mAccountManager = RedmineAccountManager.get(getApplicationContext());

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mUserName = mEtLogin.getText().toString();
                mPassword = mEtPassword.getText().toString();

                mAuthTask = new RegistrationTask();
                mAuthTask.execute();
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
    }

    private void finishLogin(String authToken) {
        final RedmineAccount account = new RedmineAccount(mUserName, "9gvM3050");

        if (mAccountManager.isExists(account)) {
            mAccountManager.setPassword(account, mPassword);
        } else {
            mAccountManager.addAccountExplicitly(account, mPassword);
            mAccountManager.setToken(account, authToken);
        }

        mAccountManager.setServer(account, mEtServerName.getText().toString());
        mAccountManager.setDefaultAccount(account);

        final Intent intent = new Intent()
                .putExtra(AccountManager.KEY_ACCOUNT_NAME, mUserName)
                .putExtra(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.account_type));

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);

        finish();
    }

    public void onAuthenticationResult(String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            finishLogin(authToken);
        } else {
            mEtLogin.requestFocus();
            mEtPassword.setText("");
        }

        mAuthTask = null;
    }

    public void onAuthenticationCancel() {
        mAuthTask = null;
    }

    private class RegistrationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return "#";
        }

        @Override
        protected void onPostExecute(String authToken) {
            onAuthenticationResult(authToken);
        }

        @Override
        protected void onCancelled() {
            onAuthenticationCancel();
        }

    }

}
