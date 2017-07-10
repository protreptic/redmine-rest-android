package name.peterbukhal.android.redmine.service.redmine.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import name.peterbukhal.android.redmine.service.redmine.model.Status;

public final class IssueStatusesResponse {

    @SerializedName("issue_statuses")
    private List<Status> issueStatuses;

    public List<Status> getIssueStatuses() {
        return issueStatuses;
    }

}
