package name.peterbukhal.android.redmine.activity.abs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class AbsActivity extends AppCompatActivity implements FragmentTransactionAllowable {

    private boolean mIsFragmentTransactionsAllowed;

    @Override
    public boolean isTransactionAllowed() {
        return mIsFragmentTransactionsAllowed;
    }

    @Override
    protected void onResumeFragments() {
        mIsFragmentTransactionsAllowed = true;

        super.onResumeFragments();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mIsFragmentTransactionsAllowed = false;

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsFragmentTransactionsAllowed = true;
    }

}
