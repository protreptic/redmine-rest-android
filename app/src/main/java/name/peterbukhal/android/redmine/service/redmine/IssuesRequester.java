package name.peterbukhal.android.redmine.service.redmine;

import android.os.Parcel;
import android.os.Parcelable;

import name.peterbukhal.android.redmine.service.redmine.model.Project;
import name.peterbukhal.android.redmine.service.redmine.model.Tracker;
import name.peterbukhal.android.redmine.service.redmine.model.User;
import name.peterbukhal.android.redmine.service.redmine.response.IssuesResponse;

import static name.peterbukhal.android.redmine.service.redmine.RedmineProvider.provide;

public final class IssuesRequester implements Parcelable {

    public static IssuesRequester CREATED_BY_ME =
            new IssuesRequester()
                    .withCreatedByMe()
                    .withSort("updated_on", true)
                    .withLimit(10);

    public static IssuesRequester ASSIGNED_TO_ME =
            new IssuesRequester()
                    .withAssignedToMe()
                    .withIssueId()
                    .withSort("updated_on", true)
                    .withLimit(10);

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

    public IssuesRequester() {
        // empty
    }

    protected IssuesRequester(Parcel in) {
        mOffset = in.readInt();
        mLimit = in.readInt();
        mSort = in.readString();
        mIssueIds = in.readString();
        mProjectId = in.readString();
        mSubProjectId = in.readString();
        mTrackerId = in.readString();
        mStatusIds = in.readString();
        mAssignedToId = in.readString();
        mWatcherId = in.readString();
        mAuthorId = in.readString();
    }

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

    public retrofit2.Call<IssuesResponse> build() {
        return provide().issues(
                mOffset, mLimit, mSort, mIssueIds,
                mProjectId, mSubProjectId, mTrackerId, mWatcherId, mAuthorId,
                mStatusIds, mAssignedToId, null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mOffset);
        parcel.writeInt(mLimit);
        parcel.writeString(mSort);
        parcel.writeString(mIssueIds);
        parcel.writeString(mProjectId);
        parcel.writeString(mSubProjectId);
        parcel.writeString(mTrackerId);
        parcel.writeString(mStatusIds);
        parcel.writeString(mAssignedToId);
        parcel.writeString(mWatcherId);
        parcel.writeString(mAuthorId);
    }

    public static final Creator<IssuesRequester> CREATOR = new Creator<IssuesRequester>() {

        @Override
        public IssuesRequester createFromParcel(Parcel in) {
            return new IssuesRequester(in);
        }

        @Override
        public IssuesRequester[] newArray(int size) {
            return new IssuesRequester[size];
        }

    };

}
