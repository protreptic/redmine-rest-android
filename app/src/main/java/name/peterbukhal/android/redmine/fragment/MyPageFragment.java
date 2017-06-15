package name.peterbukhal.android.redmine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.fragment.base.AbsFragment;
import name.peterbukhal.android.redmine.fragment.issue.CreatedByMeIssuesFragment;
import name.peterbukhal.android.redmine.fragment.issue.MyIssuesFragment;
import name.peterbukhal.android.redmine.fragment.issue.WatchedIssuesFragment;

import static name.peterbukhal.android.redmine.fragment.issue.CreatedByMeIssuesFragment.TAG_CREATED_BY_ME_ISSUES;
import static name.peterbukhal.android.redmine.fragment.issue.MyIssuesFragment.TAG_MY_ISSUES;
import static name.peterbukhal.android.redmine.fragment.issue.WatchedIssuesFragment.TAG_WATCHED_ISSUES;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 23.03.2017.
 */
public final class MyPageFragment extends AbsFragment {

    public static final String FRAGMENT_TAG_MY_PAGE = "fragment_tag_my_page";

    public static Fragment newInstance() {
        Bundle arguments = new Bundle();

        Fragment fragment = new MyPageFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup content = (ViewGroup) inflater.inflate(R.layout.f_my_page, container, false);

        if (content != null) {

        }

        return content;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTitle(getString(R.string.my_page));

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContent, MyIssuesFragment.newInstance(), TAG_MY_ISSUES)
                    .add(R.id.fragmentContent, WatchedIssuesFragment.newInstance(), TAG_WATCHED_ISSUES)
                    .add(R.id.fragmentContent, CreatedByMeIssuesFragment.newInstance(), TAG_CREATED_BY_ME_ISSUES)
                    .commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getChildFragmentManager()
                .beginTransaction()
                .remove(getChildFragmentManager().findFragmentByTag(TAG_MY_ISSUES))
                .remove(getChildFragmentManager().findFragmentByTag(TAG_WATCHED_ISSUES))
                .remove(getChildFragmentManager().findFragmentByTag(TAG_CREATED_BY_ME_ISSUES))
                .commitAllowingStateLoss();
    }

}
