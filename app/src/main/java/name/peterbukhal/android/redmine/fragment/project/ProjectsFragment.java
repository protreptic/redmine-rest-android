package name.peterbukhal.android.redmine.fragment.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.service.redmine.RedmineProvider;
import name.peterbukhal.android.redmine.service.redmine.model.Project;
import name.peterbukhal.android.redmine.service.redmine.response.ProjectsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static name.peterbukhal.android.redmine.fragment.project.ProjectFragment.TAG_PROJECT;

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

    @BindView(R.id.projects)
    RecyclerView mRvProjects;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup content = (ViewGroup) inflater.inflate(R.layout.f_projects, container, false);

        if (content != null) {
            mUnbinder = ButterKnife.bind(this, content);

            mRvProjects.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        return content;
    }

    private List<Project> mProjects = new ArrayList<>();
    private ProjectsAdapter mProjectsAdapter = new ProjectsAdapter();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRvProjects.setAdapter(mProjectsAdapter);

        if (savedInstanceState == null) {
            RedmineProvider.provide().projects().enqueue(new Callback<ProjectsResponse>() {

                @Override
                public void onResponse(Call<ProjectsResponse> call, Response<ProjectsResponse> response) {
                    if (response.isSuccessful()) {
                        for (final Project project : response.body().getProjects()) {
                            if (project.getParent() == null) {
                                mProjects.add(project);
                            }
                        }

                        mProjectsAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ProjectsResponse> call, Throwable t) {}

            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }

    private class ProjectsAdapter extends RecyclerView.Adapter<ProjectViewHolder> {

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ProjectViewHolder(LayoutInflater.from(getActivity())
                    .inflate(R.layout.l_project, parent, false));
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            final Project project = mProjects.get(position);

            holder.mTvTitle.setText(project.getName());

            if (!project.getDescription().isEmpty()) {
                holder.mTvDescription.setText(Html.fromHtml(project.getDescription()));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content, ProjectFragment.newInstance(project), TAG_PROJECT)
                            .commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mProjects.size();
        }

    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView mTvTitle;

        @BindView(R.id.description)
        TextView mTvDescription;

        ProjectViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

}
