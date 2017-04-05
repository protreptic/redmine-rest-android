package name.peterbukhal.android.redmine.service;

import name.peterbukhal.android.redmine.service.model.Project;
import name.peterbukhal.android.redmine.service.model.Tracker;
import name.peterbukhal.android.redmine.service.model.User;
import name.peterbukhal.android.redmine.service.response.IssuesResponse;

import static name.peterbukhal.android.redmine.service.RedmineProvider.provide;

public final class IssuesRequester {

    private int mOffset = 0;
    private int mLimit = 0;
    private String mSort;
    private String mIssueIds;
    private String mProjectId;
    private String mSubProjectId;
    private String mTrackerId;
    private String mStatusIds;
    private String mAssignedToId;
    private String mWatcherId;
    private String mAuthorId;

    public IssuesRequester withOffset(int offset) {
        mOffset = offset;

        return this;
    }

    public IssuesRequester withLimit(int limit) {
        mLimit = limit;

        return this;
    }

    public IssuesRequester withIssueId(int... issueIds) {
        if (issueIds.length == 0) {
            mIssueIds = null;

            return this;
        }

        mIssueIds = "";

        for (int issueId : issueIds) {
            mIssueIds += issueId + ",";
        }

        if (mIssueIds.endsWith(",")) {
            mIssueIds = mIssueIds.substring(0, mIssueIds.length() - 1);
        }

        return this;
    }

    public IssuesRequester withProject(Project project) {
        mProjectId = project.getId() + "";

        return this;
    }

    public IssuesRequester withSubProject(Project project) {
        mSubProjectId = project.getId() + "";

        return this;
    }

    public IssuesRequester withTracker(Tracker tracker) {
        mTrackerId = tracker.getId() + "";

        return this;
    }

    public IssuesRequester withAssignedTo(User user) {
        mAssignedToId = user.getId() + "";

        return this;
    }

    public IssuesRequester withAssignedToMe() {
        mAssignedToId = "me";

        return this;
    }

    public IssuesRequester withWatchedByMe() {
        mWatcherId = "me";

        return this;
    }

    public IssuesRequester withCreatedByMe() {
        mAuthorId = "me";

        return this;
    }

    public IssuesRequester withSort(String field, boolean desc) {
        mSort = field + (desc ? ":desc" : "");

        return this;
    }

    public retrofit2.Call<IssuesResponse> request() {
        return provide().issues(
                mOffset, mLimit, mSort, mIssueIds,
                mProjectId, mSubProjectId, mTrackerId, mWatcherId, mAuthorId,
                mStatusIds, mAssignedToId, null);
    }

}
