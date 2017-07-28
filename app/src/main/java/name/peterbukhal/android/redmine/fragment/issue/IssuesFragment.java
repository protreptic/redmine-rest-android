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

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.realm.Issue;
import name.peterbukhal.android.redmine.service.redmine.request.IssuesRequester;
import name.peterbukhal.android.redmine.service.redmine.response.IssuesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static name.peterbukhal.android.redmine.fragment.issue.IssueFragment.TAG_ISSUE;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class IssuesFragment extends Fragment {

    public static final String TAG_ISSUES = "fragment_tag_issues";

    public static final String ARG_TITLE = "arg_title";
    public static final String ARG_REQUESTER = "arg_requester";

    public static Fragment newInstance(String title, IssuesRequester requester) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, title);
        arguments.putParcelable(ARG_REQUESTER, requester);

        Fragment fragment = new IssuesFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @BindView(R.id.title)
    TextView mTvTitle;

    @BindView(R.id.issues)
    RecyclerView mRvIssues;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup content = (ViewGroup) inflater.inflate(R.layout.f_issues, container, false);

        if (content != null) {
            mUnbinder = ButterKnife.bind(this, content);

            mRvIssues.setLayoutManager(new LinearLayoutManager(getContext()));
            mRvIssues.setAdapter(mIssuesAdapter);
        }

        return content;
    }

    private String mTitle;
    private IssuesRequester mRequester;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ARG_TITLE, mTitle);
        outState.putParcelable(ARG_REQUESTER, mRequester);
    }

    private List<Issue> mIssues = Collections.emptyList();
    private IssuesAdapter mIssuesAdapter = new IssuesAdapter();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null &&
                savedInstanceState.containsKey(ARG_TITLE) &&
                savedInstanceState.containsKey(ARG_REQUESTER)) {
            mTitle = savedInstanceState.getString(ARG_TITLE);
            mRequester = savedInstanceState.getParcelable(ARG_REQUESTER);
        } else if (getArguments().containsKey(ARG_TITLE) &&
                        getArguments().containsKey(ARG_REQUESTER)) {
            mTitle = getArguments().getString(ARG_TITLE);
            mRequester = getArguments().getParcelable(ARG_REQUESTER);
        }

        fetchContent();
    }

    private void fetchContent() {
        mTvTitle.setText(mTitle);

        mRequester
                .build()
                .enqueue(new Callback<IssuesResponse>() {

                    @Override
                    public void onResponse(Call<IssuesResponse> call, Response<IssuesResponse> response) {
                        if (response.isSuccessful()) {
                            getView().setVisibility(
                                    response.body()
                                            .getIssues()
                                            .isEmpty() ? View.GONE : View.VISIBLE);

                            mIssues = response.body().getIssues();
                            mIssuesAdapter.notifyDataSetChanged();
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

            holder.mTvIssueNo.setText(getString(R.string.issue_no, issue.getId()));
            holder.mTvProject.setText(issue.getProject().getName());
            holder.mTvTracker.setText(issue.getTracker().getName());
            holder.mTvSubjectAndStatus.setText(issue.getSubject() + " (" + issue.getStatus().getName() + ")");

            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content, IssueFragment.newInstance(issue.getId()), TAG_ISSUE)
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
