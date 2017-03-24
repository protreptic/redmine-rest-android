package name.peterbukhal.android.redmine.rest.fragment.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * TODO Доработать документацию
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class UsersFragment extends Fragment {

    public static final String TAG_USERS = "tag_users";

    public static Fragment newInstance() {
        final Bundle args = new Bundle();

        final Fragment fragment = new UsersFragment();
        fragment.setArguments(args);

        return fragment;
    }

}
