package name.peterbukhal.android.redmine.fragment.issue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.dialog.ConfirmationDialog;
import name.peterbukhal.android.redmine.fragment.base.AbsFragment;
import name.peterbukhal.android.redmine.realm.Project;
import name.peterbukhal.android.redmine.service.redmine.model.Issue;
import name.peterbukhal.android.redmine.service.redmine.model.Relation;
import name.peterbukhal.android.redmine.service.redmine.model.User;
import name.peterbukhal.android.redmine.service.redmine.response.IssueResponse;
import name.peterbukhal.android.redmine.service.redmine.response.UserResponse;
import name.peterbukhal.android.redmine.util.RoundedTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static name.peterbukhal.android.redmine.fragment.issue.EditIssueFragment.TAG_EDIT_ISSUE;
import static name.peterbukhal.android.redmine.service.redmine.RedmineProvider.provide;
import static name.peterbukhal.android.redmine.util.Utils.getGravatar;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class IssueFragment extends AbsFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG_ISSUE = "tag_issue";
    public static final String ARG_ISSUE_ID = "arg_issue_id";

    public static Fragment newInstance(int issueId) {
        final Bundle arguments = new Bundle();
        arguments.putInt(ARG_ISSUE_ID, issueId);

        Fragment fragment = new IssueFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.m_issue, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit: {
                editIssue();
            } break;
            case R.id.watch: {
                watchIssue();
            } break;
            case R.id.remove: {
                deleteIssue();
            } break;
            default: {
                return false;
            }
        }

        return true;
    }

    private void editIssue() {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, EditIssueFragment.newInstance(), TAG_EDIT_ISSUE)
                .addToBackStack(TAG_EDIT_ISSUE)
                .commitAllowingStateLoss();
    }

    private void watchIssue() {

    }

    private void deleteIssue() {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(getActivity());
        confirmationDialog.setConfirmationDialogListener(new ConfirmationDialog.ConfirmationDialogListener() {

            @Override
            public void onConfirmed() {
                provide().deleteIssue(mIssue.getId())
                        .enqueue(new Callback<ResponseBody>() {

                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    getActivity()
                                            .getSupportFragmentManager()
                                            .popBackStack();

                                    Toast.makeText(getActivity(), R.string.message_000001, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {}

                        });
            }

            @Override
            public void onCancel() {}

        });
        confirmationDialog.show();
    }

    @BindView(R.id.creator_avatar)
    ImageView mCreatorAvatar;

    @BindView(R.id.issue_subject)
    TextView mTvIssueSubject;

    @BindView(R.id.issue_creation)
    TextView mTvIssueCreation;

    @BindView(R.id.status)
    TextView mTvStatus;

    @BindView(R.id.priority)
    TextView mTvPriority;

    @BindView(R.id.assign_to)
    TextView mTvAssignTo;

    @BindView(R.id.start_date)
    TextView mTvStartDate;

    @BindView(R.id.end_date)
    TextView mTvEndDate;

    @BindView(R.id.done_ratio)
    TextView mTvDoneRatio;

    @BindView(R.id.description)
    TextView mTvDescription;

    @BindView(R.id.children_group)
    ViewGroup mVgChildren;

    @BindView(R.id.children)
    RecyclerView mRvChildren;

    @BindView(R.id.relations_group)
    ViewGroup mVgRelations;

    @BindView(R.id.relations)
    RecyclerView mRvRelations;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup content = (ViewGroup) inflater.inflate(R.layout.l_issue_details, container, false);

        if (content != null) {
            mUnbinder = ButterKnife.bind(this, content);

            mRvChildren.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRvRelations.setLayoutManager(new LinearLayoutManager(getActivity()));
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

    private void persistIssue(final Issue issue) {
        getRealm().executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                name.peterbukhal.android.redmine.realm.Issue realmIssue = new name.peterbukhal.android.redmine.realm.Issue();
                realmIssue.setId(issue.getId());

                if (issue.getProject() != null) {
                    Project project = new Project();
                    project.setId(issue.getProject().getId());
                    project.setDescription(issue.getProject().getDescription());

                    realmIssue.setProject(project);
                }

                realm.copyToRealmOrUpdate(realmIssue);
            }

        });
    }

    private void fetchIssue(final int issueId) {
        provide().issue(issueId, "children,relations")
                .enqueue(new Callback<IssueResponse>() {

            @Override
            public void onResponse(Call<IssueResponse> call, Response<IssueResponse> response) {
                if (response.isSuccessful()) {
                    mIssue = response.body().getIssue();

                    setTitle(getString(R.string.issue_no, issueId) + " " + mIssue.getSubject());

                    persistIssue(mIssue);

                    mTvIssueSubject.setText(mIssue.getSubject());
                    mTvIssueCreation.setText(getString(R.string.issue_creation, mIssue.getAuthor().getName(), mIssue.getCreatedOn(), mIssue.getUpdatedOn()));
                    mTvStatus.setText("Статус: " + mIssue.getStatus().getName());
                    mTvPriority.setText("Приоритет: " + mIssue.getPriority().getName());
                    mTvAssignTo.setText("Назначена: " + mIssue.getAssignedTo().getName());
                    mTvStartDate.setText("Дата начала: " + mIssue.getStartDate());
                    mTvEndDate.setText("Дата завершения: " + mIssue.getEndDate());
                    mTvDoneRatio.setText("Готовность: " + mIssue.getDoneRatio() + "%");
                    mTvDescription.setText(Html.fromHtml(mIssue.getDescription()));

                    if (!mIssue.getChildren().isEmpty()) {
                        mRvChildren.setAdapter(new ChildrenAdapter(mIssue.getChildren()));
                        mVgChildren.setVisibility(View.VISIBLE);
                    } else {
                        mVgChildren.setVisibility(View.GONE);
                    }

                    if (!mIssue.getRelations().isEmpty()) {
                        mRvRelations.setAdapter(new RelationsAdapter(mIssue.getRelations()));
                        mVgRelations.setVisibility(View.VISIBLE);
                    } else {
                        mVgRelations.setVisibility(View.GONE);
                    }

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

    private class ChildrenAdapter extends RecyclerView.Adapter<IssueViewHolder> {

        private List<Issue> issues = new ArrayList<>();

        ChildrenAdapter(List<Issue> issues) {
            this.issues.addAll(issues);
        }

        @Override
        public IssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new IssueViewHolder(LayoutInflater.from(getActivity())
                    .inflate(R.layout.l_issue, parent, false));
        }

        @Override
        public void onBindViewHolder(final IssueViewHolder holder, int position) {
            final Issue issue = issues.get(position);

            holder.mTvIssueNo.setText("#" + issue.getId());
            holder.mTvProject.setText(mIssue.getProject().getName());
            holder.mTvTracker.setText(issue.getTracker().getName());
            holder.mTvSubjectAndStatus.setText(issue.getSubject());

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
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    PopupMenu menu = new PopupMenu(getActivity(), holder.itemView, Gravity.END);
                    menu.inflate(R.menu.m_issue);
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit: {
                                    editIssue();
                                } break;
                                case R.id.watch: {
                                    watchIssue();
                                } break;
                                case R.id.remove: {
                                    deleteIssue();
                                } break;
                                default: {
                                    return false;
                                }
                            }

                            return true;
                        }

                    });
                    menu.show();

                    return true;
                }

            });
        }

        @Override
        public int getItemCount() {
            return issues.size();
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

    private class RelationsAdapter extends RecyclerView.Adapter<IssueViewHolder> {

        private List<Relation> relations = new ArrayList<>();

        RelationsAdapter(List<Relation> relations) {
            this.relations.addAll(relations);
        }

        @Override
        public IssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new IssueViewHolder(LayoutInflater.from(getActivity())
                    .inflate(R.layout.l_issue, parent, false));
        }

        @Override
        public void onBindViewHolder(IssueViewHolder holder, int position) {
            final Relation relation = relations.get(position);

            holder.mTvIssueNo.setText("#" + relation.getIssueToId());

            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content,
                                    IssueFragment.newInstance(relation.getIssueToId()), TAG_ISSUE)
                            .addToBackStack(TAG_ISSUE)
                            .commit();
                }

            });
        }

        @Override
        public int getItemCount() {
            return relations.size();
        }

    }

}
