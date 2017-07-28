package name.peterbukhal.android.redmine.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;
import name.peterbukhal.android.redmine.activity.abs.FragmentTransactionAllowable;

public abstract class AbsFragment extends Fragment implements FragmentTransactionAllowable {

    private Realm mRealm;
    private FragmentTransactionAllowable mFragmentTransactionAllowable;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRealm = Realm.getDefaultInstance();

        if (getActivity() instanceof FragmentTransactionAllowable) {
            mFragmentTransactionAllowable = (FragmentTransactionAllowable) getActivity();
        }
    }

    @Override
    public boolean isTransactionAllowed() {
        return isAdded() && mFragmentTransactionAllowable.isTransactionAllowed();
    }

    protected void setTitle(String title) {
        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(title);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            mRealm.close();
        } catch (Exception e) {
            //
        }
    }

    protected Realm getRealm() {
        return mRealm;
    }

}
