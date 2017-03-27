package name.peterbukhal.android.redmine.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class Project {

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

}
