package name.peterbukhal.android.redmine.rest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import name.peterbukhal.android.redmine.rest.R;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 23.03.2017.
 */
public final class ProjectsFragment extends Fragment {

    public static final String TAG_PROJECTS = "tag_projects";

    public static Fragment newInstance() {
        final Bundle args = new Bundle();

        Fragment fragment = new ProjectsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f_projects, container, false);
    }

}
