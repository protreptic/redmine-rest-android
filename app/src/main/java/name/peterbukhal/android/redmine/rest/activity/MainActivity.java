package name.peterbukhal.android.redmine.rest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import name.peterbukhal.android.redmine.rest.R;
import name.peterbukhal.android.redmine.rest.fragment.IssuesFragment;

import static name.peterbukhal.android.redmine.rest.fragment.IssuesFragment.TAG_ISSUES;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_content, IssuesFragment.newInstance(), TAG_ISSUES)
                    .commit();
        }
    }

}
