package name.peterbukhal.android.redmine.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import java.util.List;

import io.realm.Realm;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.activity.abs.AbsActivity;
import name.peterbukhal.android.redmine.fragment.MyPageFragment;
import name.peterbukhal.android.redmine.realm.StatusRealm;
import name.peterbukhal.android.redmine.service.redmine.RedmineProvider;
import name.peterbukhal.android.redmine.service.redmine.model.Status;
import name.peterbukhal.android.redmine.service.redmine.response.IssueStatusesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static name.peterbukhal.android.redmine.fragment.MyPageFragment.FRAGMENT_TAG_MY_PAGE;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class MainActivity extends AbsActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, MyPageFragment.newInstance(), FRAGMENT_TAG_MY_PAGE)
                    .commit();

            fetchIssueStatuses();
        }
    }

    private void fetchIssueStatuses() {
        RedmineProvider.provide().issueStatuses()
                .enqueue(new Callback<IssueStatusesResponse>() {

            @Override
            public void onResponse(Call<IssueStatusesResponse> call, Response<IssueStatusesResponse> response) {
                if (response.isSuccessful()) {
                    final List<Status> issueStatuses = response.body().getIssueStatuses();

                    try (Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransaction(new Realm.Transaction() {

                            @Override
                            public void execute(Realm realm) {
                                for (Status issueStatus : issueStatuses) {
                                    StatusRealm statusRealmRealm =
                                            new StatusRealm();
                                    statusRealmRealm.setId(issueStatus.getId());
                                    statusRealmRealm.setName(issueStatus.getName());

                                    realm.copyToRealmOrUpdate(statusRealmRealm);
                                }
                            }

                        });
                    } catch (Exception e) {
                        //
                    }
                }
            }

            @Override
            public void onFailure(Call<IssueStatusesResponse> call, Throwable t) {}

        });
    }

}