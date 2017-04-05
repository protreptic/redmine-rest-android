package name.peterbukhal.android.redmine.activity;

import android.accounts.Account;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import io.realm.Realm;
import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.adapter.ProjectMenuAdapter;
import name.peterbukhal.android.redmine.fragment.GuideFragment;
import name.peterbukhal.android.redmine.fragment.MyPageFragment;
import name.peterbukhal.android.redmine.fragment.project.ProjectFragment;
import name.peterbukhal.android.redmine.fragment.project.ProjectsFragment;
import name.peterbukhal.android.redmine.service.RedmineProvider;
import name.peterbukhal.android.redmine.service.model.Project;
import name.peterbukhal.android.redmine.service.response.ProjectsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static name.peterbukhal.android.redmine.account.RedmineAccountManager.EXTRA_ACCOUNT;
import static name.peterbukhal.android.redmine.fragment.GuideFragment.TAG_GUIDE;
import static name.peterbukhal.android.redmine.fragment.MyPageFragment.TAG_MY_PAGE;
import static name.peterbukhal.android.redmine.fragment.project.ProjectFragment.TAG_PROJECT;
import static name.peterbukhal.android.redmine.fragment.project.ProjectsFragment.TAG_PROJECTS;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class MainActivity extends AppCompatActivity {

    private Toolbar initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.redmine);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        return toolbar;
    }

    private AccountHeader initAccountHeader() {
        final ProfileDrawerItem defaultProfile =
                new ProfileDrawerItem()
                        .withTextColorRes(R.color.colorPrimary)
                        .withName("")
                        .withEmail(mAccount.name);

        return new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(defaultProfile)
                .build();
    }

    private IDrawerItem[] initDrawerItems() {
        return new IDrawerItem[] {
                new PrimaryDrawerItem()
                        .withIdentifier(1)
                        .withIcon(R.drawable.ic_my_page)
                        .withName(R.string.my_page),
                new PrimaryDrawerItem()
                        .withIdentifier(2)
                        .withIcon(R.drawable.ic_projects)
                        .withName(R.string.projects),
                new PrimaryDrawerItem()
                        .withIdentifier(3)
                        .withIcon(R.drawable.ic_guide)
                        .withName(R.string.guide) };
    }

    private Account mAccount;

    private void initProjects() {
        RedmineProvider.provide().projects().enqueue(new Callback<ProjectsResponse>() {

            @Override
            public void onResponse(Call<ProjectsResponse> call, Response<ProjectsResponse> response) {
                if (response.isSuccessful()) {
                    final Spinner spinner = (Spinner) findViewById(R.id.spinner_nav);
                    spinner.setAdapter(new ProjectMenuAdapter(getApplicationContext(), response.body().getProjects()));
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            final Project project = (Project) adapterView.getItemAtPosition(i);

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_content, ProjectFragment.newInstance(project), TAG_PROJECT)
                                    .commit();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {}

                    });
                } else {

                }
            }

            @Override
            public void onFailure(Call<ProjectsResponse> call, Throwable t) {

            }

        });
    }

    private Drawer mDrawer;
    private Realm mRealm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();
        mAccount = getIntent().getParcelableExtra(EXTRA_ACCOUNT);

        setContentView(R.layout.a_main);

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withToolbar(initToolbar())
                .withSliderBackgroundColorRes(R.color.c2)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(initAccountHeader())
                .addDrawerItems(initDrawerItems())
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1: {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_content, MyPageFragment.newInstance(), TAG_MY_PAGE)
                                        .commit();
                            } break;
                            case 2: {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_content, ProjectsFragment.newInstance(), TAG_PROJECTS)
                                        .commit();
                            } break;
                            case 3: {
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
            mDrawer.setSelection(1);

            initProjects();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        return true;
    }

}
