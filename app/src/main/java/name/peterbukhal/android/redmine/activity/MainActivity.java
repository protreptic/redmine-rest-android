package name.peterbukhal.android.redmine.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

import io.realm.Realm;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.activity.abs.AbsActivity;
import name.peterbukhal.android.redmine.fragment.GuideFragment;
import name.peterbukhal.android.redmine.fragment.MyPageFragment;
import name.peterbukhal.android.redmine.fragment.project.ProjectsFragment;
import name.peterbukhal.android.redmine.service.redmine.RedmineProvider;
import name.peterbukhal.android.redmine.service.redmine.model.Status;
import name.peterbukhal.android.redmine.service.redmine.response.IssueStatusesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static name.peterbukhal.android.redmine.fragment.GuideFragment.TAG_GUIDE;
import static name.peterbukhal.android.redmine.fragment.MyPageFragment.FRAGMENT_TAG_MY_PAGE;
import static name.peterbukhal.android.redmine.fragment.project.ProjectsFragment.TAG_PROJECTS;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class MainActivity extends AbsActivity {

    private Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }

        return toolbar;
    }

    private AccountHeader initMenuHeader() {
        final ProfileDrawerItem defaultProfile =
                new ProfileDrawerItem()
                        .withTextColorRes(R.color.colorPrimary)
                        .withName("Peter Bukhal")
                        .withEmail("peter.bukhal@gmail.com");

        return new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(defaultProfile)
                .build();
    }

    public static final int MENU_MY_PAGE = 100;
    public static final int MENU_PROJECTS = 200;
    public static final int MENU_GUIDE = 300;

    private IDrawerItem[] initMenuItems() {
        return new IDrawerItem[] {
                new PrimaryDrawerItem()
                        .withIdentifier(MENU_MY_PAGE)
                        .withIcon(R.drawable.ic_my_page)
                        .withName(R.string.my_page),
                new PrimaryDrawerItem()
                        .withIdentifier(MENU_PROJECTS)
                        .withIcon(R.drawable.ic_projects)
                        .withName(R.string.projects),
                new PrimaryDrawerItem()
                        .withIdentifier(MENU_GUIDE)
                        .withIcon(R.drawable.ic_guide)
                        .withName(R.string.guide) };
    }

    private Drawer mDrawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_main);

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withToolbar(initToolbar())
                .withSliderBackgroundColorRes(R.color.c2)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(initMenuHeader())
                .addDrawerItems(initMenuItems())
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case MENU_MY_PAGE: {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_content, MyPageFragment.newInstance(), FRAGMENT_TAG_MY_PAGE)
                                        .commit();
                            } break;
                            case MENU_PROJECTS: {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_content, ProjectsFragment.newInstance(), TAG_PROJECTS)
                                        .commit();
                            } break;
                            case MENU_GUIDE: {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_content, GuideFragment.newInstance(), TAG_GUIDE)
                                        .commit();
                            } break;
                        }

                        return false;
                    }

                })
                .withCloseOnClick(true)
                .build();

        if (savedInstanceState == null) {
            mDrawer.setSelection(MENU_MY_PAGE);

            fetchIssueStatuses();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void showBackArrow() {
        mDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressWarnings("ConstantConditions")
    public void hideBackArrow() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
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
                                    name.peterbukhal.android.redmine.realm.Status statusRealm =
                                            new name.peterbukhal.android.redmine.realm.Status();
                                    statusRealm.setId(issueStatus.getId());
                                    statusRealm.setName(issueStatus.getName());

                                    realm.copyToRealmOrUpdate(statusRealm);
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