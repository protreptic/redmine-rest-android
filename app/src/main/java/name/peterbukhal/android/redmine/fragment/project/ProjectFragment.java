package name.peterbukhal.android.redmine.fragment.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.service.redmine.model.Project;

public final class ProjectFragment extends Fragment {

    public static final String TAG_PROJECT = "tag_project";
    public static final String ARG_PROJECT = "arg_project";

    public static Fragment newInstance(Project project) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_PROJECT, project);

        Fragment fragment = new ProjectFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @BindView(R.id.project_name)
    TextView mTvProjectName;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup content = (ViewGroup) inflater.inflate(R.layout.f_project, container, false);

        if (content != null) {
            mUnbinder = ButterKnife.bind(this, content);
        }

        return content;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ARG_PROJECT, mProject);
    }

    private Project mProject;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_PROJECT)) {
            mProject = savedInstanceState.getParcelable(ARG_PROJECT);
        } else if (getArguments().containsKey(ARG_PROJECT)) {
            mProject = getArguments().getParcelable(ARG_PROJECT);
        }

        if (mProject != null) {
            mTvProjectName.setText(mProject.getName());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }

}
