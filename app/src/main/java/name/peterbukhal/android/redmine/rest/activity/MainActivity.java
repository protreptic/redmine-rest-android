package name.peterbukhal.android.redmine.rest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import name.peterbukhal.android.redmine.rest.R;
import name.peterbukhal.android.redmine.rest.fragment.GuideFragment;
import name.peterbukhal.android.redmine.rest.fragment.MyPageFragment;
import name.peterbukhal.android.redmine.rest.fragment.ProjectsFragment;

import static name.peterbukhal.android.redmine.rest.fragment.GuideFragment.TAG_GUIDE;
import static name.peterbukhal.android.redmine.rest.fragment.MyPageFragment.TAG_MY_PAGE;
import static name.peterbukhal.android.redmine.rest.fragment.ProjectsFragment.TAG_PROJECTS;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class MainActivity extends AppCompatActivity {

    private AccountHeader generateAccountHeader() {
        final ProfileDrawerItem defaultProfile =
                new ProfileDrawerItem()
                        .withTextColorRes(R.color.colorPrimary)
                        .withName("Mike Penz")
                        .withEmail("mikepenz@gmail.com");

        return new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(defaultProfile)
                .build();
    }

    private IDrawerItem[] generateDrawerItems() {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        setSupportActionBar(toolbar);

        final Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(generateAccountHeader())
                .addDrawerItems(generateDrawerItems())
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
        }
    }

}
