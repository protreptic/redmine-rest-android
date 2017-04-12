package name.peterbukhal.android.redmine.account;

import android.accounts.Account;

/**
 * TODO Доработать документацию
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public class RedmineAccount extends Account {

    private String name;
    private String password;

    public RedmineAccount(String name, String password) {
        super(name, "org.redmine.account");

        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
