package name.peterbukhal.android.redmine.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.fragment.abs.AbsFragment;
import name.peterbukhal.android.redmine.service.redmine.model.User;
import name.peterbukhal.android.redmine.service.redmine.response.UserResponse;
import name.peterbukhal.android.redmine.util.RoundedTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static name.peterbukhal.android.redmine.service.redmine.RedmineProvider.provide;
import static name.peterbukhal.android.redmine.util.Utils.getGravatar;

/**
 * TODO Доработать документацию
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class UserFragment extends AbsFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String FRAGMENT_TAG_USER = "fragment_tag_user";
    public static final String ARG_USER_ID = "arg_user_id";

    public static Fragment newInstance(int userId) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_USER_ID, userId);

        Fragment fragment = new UserFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSrlRefresh;

    @BindView(R.id.creator_avatar)
    ImageView mCreatorAvatar;

    @BindView(R.id.issue_subject)
    TextView mTvFullName;

    @BindView(R.id.issue_creation)
    TextView mTvEmail;

    @BindView(R.id.status)
    TextView mTvStatus;

    @BindView(R.id.priority)
    TextView mTvPriority;

    @BindView(R.id.assign_to)
    TextView mTvAssignTo;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup content = (ViewGroup) inflater.inflate(R.layout.f_user, container, false);

        if (content != null) {
            mUnbinder = ButterKnife.bind(this, content);

            mSrlRefresh.setOnRefreshListener(this);
        }

        return content;
    }

    private int mUserId;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_USER_ID, mUserId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTitle(getString(R.string.user));

        if (getArguments().containsKey(ARG_USER_ID)) {
            mUserId = getArguments().getInt(ARG_USER_ID);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(ARG_USER_ID)) {
            mUserId = savedInstanceState.getInt(ARG_USER_ID);
        }

        fetchUser(mUserId);
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

                            mTvFullName.setText(user.getFullname());
                            mTvEmail.setText(user.getMail());

                            setTitle(user.getFullname());
                        } else {
                            Toast.makeText(getActivity(), "Данные не загружены", Toast.LENGTH_LONG).show();
                        }

                        mSrlRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        if (!isTransactionAllowed()) return;

                        mSrlRefresh.setRefreshing(false);

                        Toast.makeText(getActivity(), "Данные не загружены", Toast.LENGTH_LONG).show();
                    }

                });
    }

    @Override
    public void onRefresh() {
        mSrlRefresh.setRefreshing(true);

        fetchUser(mUserId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }

}
