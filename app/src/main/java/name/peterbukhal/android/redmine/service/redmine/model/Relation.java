package name.peterbukhal.android.redmine.service.redmine.model;

public final class Relation {

    private int id;
    private int issue_id;
    private int issue_to_id;
    private String relation_type;
    private String delay;

    public int getId() {
        return id;
    }

    public int getIssueId() {
        return issue_id;
    }

    public int getIssueToId() {
        return issue_to_id;
    }

    public String getType() {
        return relation_type;
    }

    public String getDelay() {
        return delay;
    }

}
