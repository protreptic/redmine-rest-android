package name.peterbukhal.android.redmine.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import name.peterbukhal.android.redmine.R;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 23.03.2017.
 */
public final class LoginActivity extends AppCompatActivity {

    @BindView(R.id.servername)
    EditText mEtServerName;

    @BindView(R.id.username)
    EditText mEtLogin;

    @BindView(R.id.password)
    EditText mEtPassword;

    private Unbinder mUnbinder;

    private String mServer;
    private String mUser;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_login);

        mUnbinder = ButterKnife.bind(this);

        findViewById(R.id.submit)
                .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mServer = mEtServerName.getText().toString();
                mUser = mEtLogin.getText().toString();
                mPassword = mEtPassword.getText().toString();
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
    }

}
