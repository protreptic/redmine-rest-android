package name.peterbukhal.android.redmine.fragment.issue;

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
import name.peterbukhal.android.redmine.realm.Issue;
import name.peterbukhal.android.redmine.service.redmine.request.IssuesRequester;
import name.peterbukhal.android.redmine.service.redmine.response.IssuesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class IssuesFragment extends Fragment {

    public static final String TAG_ISSUES = "tag_issues";

    public static Fragment newInstance() {
        final Bundle args = new Bundle();

        Fragment fragment = new IssuesFragment();
        fragment.setArguments(args);

        return fragment;
    }

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
            new IssuesRequester()
                    .withAssignedToMe()
                    .build()
                    .enqueue(new Callback<IssuesResponse>() {

                @Override
                public void onResponse(Call<IssuesResponse> call, Response<IssuesResponse> response) {
                    if (response.isSuccessful()) {
                        mIssues = response.body().getIssues();
                        mIssuesAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<IssuesResponse> call, Throwable t) {}

            });
        }
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

            holder.mTvTitle.setText("#" + issue.getId() + " " + issue.getSubject());
            holder.mTvDescription.setText(Html.fromHtml(issue.getDescription()));
        }

        @Override
        public int getItemCount() {
            return mIssues.size();
        }

    }

    static class IssueViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView mTvTitle;

        @BindView(R.id.description)
        TextView mTvDescription;

        IssueViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

}
