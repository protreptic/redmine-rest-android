package name.peterbukhal.android.redmine.service.redmine.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class Project implements Parcelable {

    private int id;
    private String name;
    private String identifier;
    private String description;
    private int status;
    private Project parent;

    @SerializedName("is_public")
    private boolean isPublic;

    @SerializedName("created_on")
    private String createdOn;

    @SerializedName("updated_on")
    private String updatedOn;

    protected Project(Parcel in) {
        id = in.readInt();
        name = in.readString();
        identifier = in.readString();
        description = in.readString();
        status = in.readInt();
        parent = in.readParcelable(Project.class.getClassLoader());
        isPublic = in.readByte() != 0;
        createdOn = in.readString();
        updatedOn = in.readString();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }

    public Project getParent() {
        return parent;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public boolean isSubProject() {
        return parent != null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(identifier);
        parcel.writeString(description);
        parcel.writeInt(status);
        parcel.writeParcelable(parent, i);
        parcel.writeByte((byte) (isPublic ? 1 : 0));
        parcel.writeString(createdOn);
        parcel.writeString(updatedOn);
    }
}
