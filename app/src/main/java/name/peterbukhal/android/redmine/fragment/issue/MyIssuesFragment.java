package name.peterbukhal.android.redmine.fragment.issue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.realm.Issue;
import name.peterbukhal.android.redmine.service.IssuesRequester;
import name.peterbukhal.android.redmine.service.response.IssuesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static name.peterbukhal.android.redmine.fragment.issue.IssueFragment.TAG_ISSUE;
import static name.peterbukhal.android.redmine.service.IssuesRequester.ASSIGNED_TO_ME;

public final class MyIssuesFragment extends Fragment {

    public static final String TAG_MY_ISSUES = "tag_my_issues";

    public static Fragment newInstance() {
        final Bundle args = new Bundle();

        Fragment fragment = new MyIssuesFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @BindView(R.id.my_issues)
    TextView mTvMyIssuesCount;

    @BindView(R.id.all)
    TextView mTvShowAll;

    @BindView(R.id.issues)
    RecyclerView mRvIssues;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup content = (ViewGroup) inflater.inflate(R.layout.f_my_issues, container, false);

        if (content != null) {
            mUnbinder = ButterKnife.bind(this, content);

            mRvIssues.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        return content;
    }

    private List<Issue> mIssues = new ArrayList<>();
    private IssuesAdapter mIssuesAdapter = new IssuesAdapter();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRvIssues.setAdapter(mIssuesAdapter);

        if (savedInstanceState == null) {
            try (Realm realm = Realm.getDefaultInstance()) {
                if (mIssues.isEmpty()) {
                    updateIssues();
                } else {
                    mTvMyIssuesCount.setText(getString(R.string.my_issues, mIssues.size()));
                    mIssuesAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void updateIssues() {
        ASSIGNED_TO_ME.build()
                .enqueue(new Callback<IssuesResponse>() {

                    @Override
                    public void onResponse(Call<IssuesResponse> call, Response<IssuesResponse> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful()) {
                            mIssues = response.body().getIssues();
                            mIssuesAdapter.notifyDataSetChanged();

                            mTvMyIssuesCount.setText(getString(R.string.my_issues, response.body().getTotalCount()));
                            mTvShowAll.setVisibility(response.body().getTotalCount() > mIssues.size() ? View.VISIBLE : View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<IssuesResponse> call, Throwable t) {}

                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }

    private class IssuesAdapter extends RecyclerView.Adapter<IssueViewHolder> {

        @Override
        public IssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new IssueViewHolder(LayoutInflater.from(getActivity())
                    .inflate(R.layout.l_issue, parent, false));
        }

        @Override
        public void onBindViewHolder(IssueViewHolder holder, int position) {
            final Issue issue = mIssues.get(position);

            holder.mTvIssueNo.setText(issue.getId() + "");
            holder.mTvProject.setText(issue.getProject().getName());
            holder.mTvTracker.setText(issue.getTracker().getName());
            holder.mTvSubjectAndStatus.setText(issue.getSubject() + " (" + issue.getStatus().getName() + ")");

            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content,
                                    IssueFragment.newInstance(issue.getId()), TAG_ISSUE)
                            .addToBackStack(TAG_ISSUE)
                            .commit();
                }

            });
        }

        @Override
        public int getItemCount() {
            return mIssues.size();
        }

    }

    static class IssueViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.issue_no)
        TextView mTvIssueNo;

        @BindView(R.id.project)
        TextView mTvProject;

        @BindView(R.id.tracker)
        TextView mTvTracker;

        @BindView(R.id.subject_and_status)
        TextView mTvSubjectAndStatus;

        IssueViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

}
