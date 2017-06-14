package name.peterbukhal.android.redmine.fragment.issue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.dialog.ConfirmationDialog;
import name.peterbukhal.android.redmine.fragment.base.AbsFragment;
import name.peterbukhal.android.redmine.service.redmine.model.Author;
import name.peterbukhal.android.redmine.service.redmine.model.Issue;
import name.peterbukhal.android.redmine.service.redmine.model.JournalRecord;
import name.peterbukhal.android.redmine.service.redmine.model.Relation;
import name.peterbukhal.android.redmine.service.redmine.model.User;
import name.peterbukhal.android.redmine.service.redmine.response.IssueResponse;
import name.peterbukhal.android.redmine.service.redmine.response.UserResponse;
import name.peterbukhal.android.redmine.util.RoundedTransformation;
import name.peterbukhal.android.redmine.util.spans.DateSpannable;
import name.peterbukhal.android.redmine.util.spans.UserSpannable;
import name.peterbukhal.android.redmine.widget.SourceCodeView;
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
        provide().addWatcher(mIssue.getId(), 0)
                .enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!isTransactionAllowed()) return;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!isTransactionAllowed()) return;

            }

        });
    }

    private void deleteWatcher(int userId) {
        provide().deleteWatcher(mIssue.getId(), userId).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!isTransactionAllowed()) return;

                if (response.isSuccessful()) {
                    fetchIssue(mIssue.getId());

                    Toast.makeText(getActivity(), R.string.message_000003, Toast.LENGTH_LONG).show();
                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!isTransactionAllowed()) return;

            }

        });
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
                                if (!isTransactionAllowed()) return;

                                if (response.isSuccessful()) {
                                    getActivity()
                                            .getSupportFragmentManager()
                                            .popBackStack();

                                    Toast.makeText(getActivity(), R.string.message_000001, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                if (!isTransactionAllowed()) return;

                            }

                        });
            }

            @Override
            public void onCancel() {}

        });
        confirmationDialog.show();
    }

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSrlRefresh;

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

    @BindView(R.id.history_group)
    ViewGroup mVgHistory;

    @BindView(R.id.history)
    RecyclerView mRvHistory;

    @BindView(R.id.watchers_group)
    ViewGroup mVgWatchers;

    @BindView(R.id.watchers)
    RecyclerView mRvWatchers;

    @BindView(R.id.source_code)
    SourceCodeView sourceCodeView;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup content = (ViewGroup) inflater.inflate(R.layout.l_issue_details, container, false);

        if (content != null) {
            mUnbinder = ButterKnife.bind(this, content);

            mSrlRefresh.setOnRefreshListener(this);
            mRvChildren.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRvRelations.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRvWatchers.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                if (!isTransactionAllowed()) return;

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
            public void onFailure(Call<UserResponse> call, Throwable t) {
                if (!isTransactionAllowed()) return;

            }

        });
    }

    private void fetchIssue(final int issueId) {
        sourceCodeView.setText("");

        provide().issue(issueId, "children,relations,watchers,journals")
                .enqueue(new Callback<IssueResponse>() {

            @Override
            public void onResponse(Call<IssueResponse> call, Response<IssueResponse> response) {
                if (!isTransactionAllowed()) return;

                if (response.isSuccessful()) {
                    mIssue = response.body().getIssue();

                    setTitle(getString(R.string.issue_no, issueId) + " " + mIssue.getSubject());

                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.append("Добавил(а) ");
                    builder.append(new UserSpannable((AppCompatActivity) getActivity(), mIssue.getAuthor()));
                    builder.append(" ");
                    builder.append(new DateSpannable(mIssue.getCreatedOn()));
                    builder.append(" назад. ");
                    builder.append("Обновлено ");
                    builder.append(new DateSpannable(mIssue.getUpdatedOn()));
                    builder.append(" назад.");

                    mTvIssueSubject.setText(mIssue.getSubject());
                    mTvIssueCreation.setText(builder);
                    mTvIssueCreation.setMovementMethod(LinkMovementMethod.getInstance());
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

                    if (!mIssue.getJournals().isEmpty()) {
                        mRvHistory.setAdapter(new HistoryAdapter(mIssue.getJournals()));
                        mVgHistory.setVisibility(View.VISIBLE);
                    } else {
                        mVgHistory.setVisibility(View.GONE);
                    }

                    if (!mIssue.getWatchers().isEmpty()) {
                        mRvWatchers.setAdapter(new WatchersAdapter(mIssue.getWatchers()));
                        mVgWatchers.setVisibility(View.VISIBLE);
                    } else {
                        mVgWatchers.setVisibility(View.GONE);
                    }

                    fetchUser(mIssue.getAuthor().getId());
                }

                mSrlRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<IssueResponse> call, Throwable t) {
                if (!isTransactionAllowed()) return;

                mSrlRefresh.setRefreshing(false);
            }

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

            holder.mTvIssueNo.setText(getString(R.string.issue_no, relation.getIssueToId()));

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

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryRecordHolder> {

        private List<JournalRecord> history = new ArrayList<>();

        HistoryAdapter(List<JournalRecord> history) {
            this.history.addAll(history);
        }

        @Override
        public HistoryRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HistoryRecordHolder(LayoutInflater.from(getActivity())
                    .inflate(R.layout.l_history_record, parent, false));
        }

        @Override
        public void onBindViewHolder(HistoryRecordHolder holder, int position) {
            final JournalRecord journalRecord = history.get(position);

            Picasso.with(getActivity())
                    .load(getGravatar(""))
                    .transform(new RoundedTransformation(4, 0))
                    .into(holder.avatar);

            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append("Обновлено ");
            builder.append(new UserSpannable((AppCompatActivity) getActivity(), journalRecord.getUser()));
            builder.append(" ");
            builder.append(new DateSpannable(journalRecord.getCreated_on()));
            builder.append(" назад.");

            holder.title.setText(builder);
            holder.title.setMovementMethod(LinkMovementMethod.getInstance());

            try {
                holder.notes.setText(Html.fromHtml(journalRecord.getNotes()));
            } catch (Exception e) {
                //
            }
        }

        @Override
        public int getItemCount() {
            return history.size();
        }

    }

    private static class HistoryRecordHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView title;
        TextView notes;

        public HistoryRecordHolder(View itemView) {
            super(itemView);

            avatar = (ImageView) itemView.findViewById(R.id.creator_avatar);
            title = (TextView) itemView.findViewById(R.id.title);
            notes = (TextView) itemView.findViewById(R.id.notes);
        }

    }

    private class WatchersAdapter extends RecyclerView.Adapter<WatcherViewHolder> {

        private List<Author> watchers = new ArrayList<>();

        WatchersAdapter(List<Author> watchers) {
            this.watchers.addAll(watchers);
        }

        @Override
        public WatcherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WatcherViewHolder(LayoutInflater.from(getActivity())
                    .inflate(R.layout.l_watcher, parent, false));
        }

        @Override
        public void onBindViewHolder(WatcherViewHolder holder, int position) {
            final Author watcher = watchers.get(position);

            Picasso.with(getActivity())
                    .load(getGravatar(""))
                    .transform(new RoundedTransformation(4, 0))
                    .into(holder.avatar);

            holder.mTvName.setText(new UserSpannable((AppCompatActivity) getActivity(), watcher));
            holder.mTvName.setMovementMethod(LinkMovementMethod.getInstance());

            holder.mBtRemove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteWatcher(watcher.getId());
                }

            });
        }

        @Override
        public int getItemCount() {
            return watchers.size();
        }

    }

    static class WatcherViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.creator_avatar)
        ImageView avatar;

        @BindView(R.id.watcher_name)
        TextView mTvName;

        @BindView(R.id.watcher_remove)
        ImageButton mBtRemove;

        WatcherViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

}
