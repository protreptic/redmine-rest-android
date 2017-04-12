package name.peterbukhal.android.redmine.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * TODO Доработать документацию
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class User implements Parcelable {

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

    protected User(Parcel in) {
        id = in.readInt();
        login = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        mail = in.readString();
        createdOn = in.readString();
        lastLoginOn = in.readString();
        apiKey = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(login);
        parcel.writeString(firstname);
        parcel.writeString(lastname);
        parcel.writeString(mail);
        parcel.writeString(createdOn);
        parcel.writeString(lastLoginOn);
        parcel.writeString(apiKey);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
