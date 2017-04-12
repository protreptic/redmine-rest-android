package name.peterbukhal.android.redmine.fragment.issue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.service.model.Issue;
import name.peterbukhal.android.redmine.service.model.User;
import name.peterbukhal.android.redmine.service.response.IssueResponse;
import name.peterbukhal.android.redmine.service.response.UserResponse;
import name.peterbukhal.android.redmine.util.RoundedTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static name.peterbukhal.android.redmine.service.RedmineProvider.provide;
import static name.peterbukhal.android.redmine.util.Utils.getGravatar;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class IssueFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG_ISSUE = "tag_issue";
    public static final String ARG_ISSUE_ID = "arg_issue_id";

    public static Fragment newInstance(int issueId) {
        final Bundle arguments = new Bundle();
        arguments.putInt(ARG_ISSUE_ID, issueId);

        Fragment fragment = new IssueFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @BindView(R.id.issue_no)
    TextView mTvIssueNo;

    @BindView(R.id.creator_avatar)
    ImageView mCreatorAvatar;

    @BindView(R.id.issue_subject)
    TextView mTvIssueSubject;

    @BindView(R.id.issue_creation)
    TextView mTvIssueCreation;

    @BindView(R.id.description)
    TextView mTvDescription;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup content = (ViewGroup) inflater.inflate(R.layout.l_issue_details, container, false);

        if (content != null) {
            mUnbinder = ButterKnife.bind(this, content);
        }

        return content;
    }

    private Issue mIssue;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_ISSUE_ID)) {
            final int issueId = savedInstanceState.getInt(ARG_ISSUE_ID);

            fetchIssue(issueId);
        } else if (getArguments() != null && getArguments().containsKey(ARG_ISSUE_ID)) {
            final int issueId = getArguments().getInt(ARG_ISSUE_ID);

            fetchIssue(issueId);
        }
    }

    private void fetchUser(final int userId) {
        provide().user(userId)
                .enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    final User user = response.body().getUser();

                    Picasso.with(getActivity())
                            .load(getGravatar(user.getMail()))
                            .transform(new RoundedTransformation(4, 0))
                            .into(mCreatorAvatar);
                } else {

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {}

        });
    }

    private void fetchIssue(final int issueId) {
        provide().issue(issueId, null)
                .enqueue(new Callback<IssueResponse>() {

            @Override
            public void onResponse(Call<IssueResponse> call, Response<IssueResponse> response) {
                if (response.isSuccessful()) {
                    mIssue = response.body().getIssue();

                    mTvIssueNo.setText(getString(R.string.issue_no, mIssue.getId()));
                    mTvIssueSubject.setText(mIssue.getSubject());
                    mTvIssueCreation.setText(getString(R.string.issue_creation, mIssue.getAuthor().getName(), mIssue.getCreatedOn(), mIssue.getUpdatedOn()));
                    mTvDescription.setText(Html.fromHtml(mIssue.getDescription()));

                    fetchUser(mIssue.getAuthor().getId());
                } else {

                }
            }

            @Override
            public void onFailure(Call<IssueResponse> call, Throwable t) {}

        });
    }

    @Override
    public void onRefresh() {
        fetchIssue(mIssue.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }

}
