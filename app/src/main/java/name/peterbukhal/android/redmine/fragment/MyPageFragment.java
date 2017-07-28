package name.peterbukhal.android.redmine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.fragment.abs.AbsFragment;
import name.peterbukhal.android.redmine.fragment.issue.IssuesFragment;

import static name.peterbukhal.android.redmine.fragment.issue.IssuesFragment.TAG_ISSUES;
import static name.peterbukhal.android.redmine.service.redmine.request.IssuesRequester.ASSIGNED_TO_ME;
import static name.peterbukhal.android.redmine.service.redmine.request.IssuesRequester.CREATED_BY_ME;
import static name.peterbukhal.android.redmine.service.redmine.request.IssuesRequester.WATCHED_BY_ME;

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
        return inflater.inflate(R.layout.f_my_page, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTitle(getString(R.string.my_page));

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_content, IssuesFragment.newInstance(getString(R.string.my_issues), ASSIGNED_TO_ME), TAG_ISSUES + 1)
                    .add(R.id.fragment_content, IssuesFragment.newInstance(getString(R.string.watched_issues), WATCHED_BY_ME), TAG_ISSUES + 2)
                    .add(R.id.fragment_content, IssuesFragment.newInstance(getString(R.string.created_by_me_issues), CREATED_BY_ME), TAG_ISSUES + 3)
                    .commitNow();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getChildFragmentManager()
                .beginTransaction()
                .remove(getChildFragmentManager().findFragmentByTag(TAG_ISSUES + 1))
                .remove(getChildFragmentManager().findFragmentByTag(TAG_ISSUES + 2))
                .remove(getChildFragmentManager().findFragmentByTag(TAG_ISSUES + 3))
                .commitNowAllowingStateLoss();
    }

}
