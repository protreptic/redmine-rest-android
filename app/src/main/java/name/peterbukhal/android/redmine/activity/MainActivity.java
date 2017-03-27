package name.peterbukhal.android.redmine.activity;

import android.accounts.Account;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.fragment.GuideFragment;
import name.peterbukhal.android.redmine.fragment.MyPageFragment;
import name.peterbukhal.android.redmine.fragment.project.ProjectsFragment;

import static name.peterbukhal.android.redmine.account.RedmineAccountManager.EXTRA_ACCOUNT;
import static name.peterbukhal.android.redmine.fragment.GuideFragment.TAG_GUIDE;
import static name.peterbukhal.android.redmine.fragment.MyPageFragment.TAG_MY_PAGE;
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
        final List<Map<String, String>> projects = new ArrayList<>();

        final Map<String, String> init = new HashMap<>();
        init.put("name", "Перейти к проекту...");

        final Map<String, String> project1 = new HashMap<>();
        project1.put("name", "project1");

        final Map<String, String> project2 = new HashMap<>();
        project2.put("name", "project2");

        final Map<String, String> project3 = new HashMap<>();
        project3.put("name", "project3");

        projects.add(init);
        projects.add(project1);
        projects.add(project2);
        projects.add(project3);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner_nav);
        spinner.setAdapter(new SimpleAdapter(this, projects, android.R.layout.simple_spinner_dropdown_item, new String[] { "name" }, new int[] { android.R.id.text1 }));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccount = getIntent().getParcelableExtra(EXTRA_ACCOUNT);

        setContentView(R.layout.a_main);

        final Drawer drawer = new DrawerBuilder()
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
                .withSelectedItem(1)
                .withCloseOnClick(true)
                .build();

        if (savedInstanceState == null) {
            drawer.setSelection(1);

            initProjects();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        return true;
    }

}
