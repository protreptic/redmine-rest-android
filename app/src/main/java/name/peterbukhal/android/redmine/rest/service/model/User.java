package name.peterbukhal.android.redmine.rest.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * TODO Доработать документацию
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class User {

    private int id;
    private String login;
    private String firstname;
    private String lastname;
    private String mail;

    @SerializedName("created_on")
    private String createdOn;

    @SerializedName("last_login_on")
    private String lastLoginOn;

    @SerializedName("api_key")
    private String apiKey;

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMail() {
        return mail;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getLastLoginOn() {
        return lastLoginOn;
    }

    public String getApiKey() {
        return apiKey;
    }

}
