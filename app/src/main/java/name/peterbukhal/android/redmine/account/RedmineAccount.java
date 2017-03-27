package name.peterbukhal.android.redmine.account;

import android.accounts.Account;

/**
 * TODO Доработать документацию
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public class RedmineAccount extends Account {

    public RedmineAccount(String name) {
        super(name, "org.redmine.account");
    }

}
