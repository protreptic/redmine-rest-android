package name.peterbukhal.android.redmine.fragment.issue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.fragment.abs.AbsFragment;
import name.peterbukhal.android.redmine.service.redmine.RedmineProvider;
import name.peterbukhal.android.redmine.service.redmine.model.Issue;
import name.peterbukhal.android.redmine.service.redmine.request.EditIssueRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class IssueEditFragment extends AbsFragment {

    public static final String TAG_EDIT_ISSUE = "fragment_tag_edit_issue";
    public static final String ARG_ISSUE_ID = "arg_issue_id";

    public static Fragment newInstance(int issueId) {
        final Bundle arguments = new Bundle();
        arguments.putInt(ARG_ISSUE_ID, issueId);

        Fragment fragment = new IssueEditFragment();
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
        inflater.inflate(R.menu.m_issue_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save: {
                saveIssue(mIssue);
            } break;
            default: {
                return false;
            }
        }

        return true;
    }

    private void saveIssue(Issue issue) {
        if (!TextUtils.isEmpty(mEtNote.getText())) {
            issue.setSubject(mEtNote.getText().toString());
        }

        RedmineProvider.provide()
                .editIssue(issue.getId(), new EditIssueRequest(issue))
                        .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    getActivity()
                            .getSupportFragmentManager()
                            .popBackStack();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}

        });
    }

    @BindView(R.id.note)
    EditText mEtNote;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup content = (ViewGroup) inflater.inflate(R.layout.f_edit_issue, container, false);

        if (content != null) {
            mUnbinder = ButterKnife.bind(this, content);
        }

        return content;
    }

    private Issue mIssue;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTitle(getString(R.string.edit));

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_ISSUE_ID)) {
            mIssue = new Issue();
            mIssue.setId(savedInstanceState.getInt(ARG_ISSUE_ID));
        } else if (getArguments() != null && getArguments().containsKey(ARG_ISSUE_ID)) {
            mIssue = new Issue();
            mIssue.setId(getArguments().getInt(ARG_ISSUE_ID));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            mUnbinder.unbind();
        } catch (Exception e) {
            //
        }
    }

}
