package name.peterbukhal.android.redmine.service.redmine.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
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

    @SerializedName("assigned_to")
    private Author assignedTo;

    @SerializedName("start_date")
    private String startDate;

    @SerializedName("due_date")
    private String endDate;

    @SerializedName("done_ratio")
    private int doneRatio;

    @SerializedName("custom_fields")
    private List<CustomField> customFields;

    @SerializedName("created_on")
    private String createdOn;

    @SerializedName("updated_on")
    private String updatedOn;

    @SerializedName("children")
    private List<Issue> children;

    @SerializedName("relations")
    private List<Relation> relations;

    @SerializedName("watchers")
    private List<Author> watchers;

    @SerializedName("journals")
    private List<HistoryRecord> journals;

    @SerializedName("attachments")
    private List<Attachment> attachments;

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

    public Author getAssignedTo() {
        return assignedTo;
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

    public String getEndDate() {
        return endDate;
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

    public List<Issue> getChildren() {
        if (children == null) {
            children = Collections.emptyList();
        }

        return children;
    }

    public List<Relation> getRelations() {
        if (relations == null) {
            relations = Collections.emptyList();
        }

        return relations;
    }

    public List<Author> getWatchers() {
        if (watchers == null) {
            watchers = Collections.emptyList();
        }

        return watchers;
    }

    public List<HistoryRecord> getJournals() {
        if (journals == null) {
            journals = Collections.emptyList();
        }

        return journals;
    }

    public List<Attachment> getAttachments() {
        if (attachments == null) {
            attachments = Collections.emptyList();
        }

        return attachments;
    }

}