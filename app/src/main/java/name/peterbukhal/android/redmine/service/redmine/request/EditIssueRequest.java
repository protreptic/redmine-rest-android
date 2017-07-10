package name.peterbukhal.android.redmine.service.redmine.request;

import name.peterbukhal.android.redmine.service.redmine.model.Issue;

public final class EditIssueRequest {

    private Issue issue;

    public EditIssueRequest(Issue issue) {
        this.issue = issue;
    }

}
