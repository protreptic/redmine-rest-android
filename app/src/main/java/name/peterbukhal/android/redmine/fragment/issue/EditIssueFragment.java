package name.peterbukhal.android.redmine.fragment.issue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.fragment.base.AbsFragment;

public final class EditIssueFragment extends AbsFragment {

    public static final String TAG_EDIT_ISSUE = "tag_edit_issue";

    public static Fragment newInstance() {
        Bundle arguments = new Bundle();

        Fragment fragment = new EditIssueFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTitle(getString(R.string.edit));
    }

}
