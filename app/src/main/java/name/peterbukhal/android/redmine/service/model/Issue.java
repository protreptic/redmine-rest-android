package name.peterbukhal.android.redmine.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class Issue {

    private int id;
    private Project project;
    private Tracker tracker;
    private Status status;
    private Priority priority;
    private Author author;
    private String subject;
    private String description;

    @SerializedName("start_date")
    private String startDate;

    @SerializedName("done_ratio")
    private int doneRatio;

    @SerializedName("custom_fields")
    private List<CustomField> customFields;

    @SerializedName("created_on")
    private String createdOn;

    @SerializedName("updated_on")
    private String updatedOn;

    public int getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public Status getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public Author getAuthor() {
        return author;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getDoneRatio() {
        return doneRatio;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

}
