package name.peterbukhal.android.redmine.util.spans;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentManager;
import android.view.View;

import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.fragment.user.UserFragment;
import name.peterbukhal.android.redmine.service.redmine.model.Author;

import static name.peterbukhal.android.redmine.fragment.user.UserFragment.FRAGMENT_TAG_USER;

@SuppressLint("ParcelCreator")
public final class UserSpan extends RedmineUrlSpan {

    private FragmentManager mFragmentManager;
    private Author mAuthor;

    public UserSpan(FragmentManager fragmentManager, Author author) {
        super("");

        mFragmentManager = fragmentManager;
        mAuthor = author;
    }

    @Override
    public void onClick(View widget) {
        mFragmentManager
                .beginTransaction()
                .replace(R.id.main_content,
                        UserFragment.newInstance(mAuthor.getId()), FRAGMENT_TAG_USER)
                .addToBackStack(FRAGMENT_TAG_USER)
                .commit();
    }

}
