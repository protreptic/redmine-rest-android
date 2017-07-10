package name.peterbukhal.android.redmine.service.redmine;

import name.peterbukhal.android.redmine.service.redmine.model.Issue;
import name.peterbukhal.android.redmine.service.redmine.request.EditIssueRequest;
import name.peterbukhal.android.redmine.service.redmine.response.CurrentResponse;
import name.peterbukhal.android.redmine.service.redmine.response.IssueResponse;
import name.peterbukhal.android.redmine.service.redmine.response.IssueStatusesResponse;
import name.peterbukhal.android.redmine.service.redmine.response.IssuesResponse;
import name.peterbukhal.android.redmine.service.redmine.response.ProjectsResponse;
import name.peterbukhal.android.redmine.service.redmine.response.TimeEntriesResponse;
import name.peterbukhal.android.redmine.service.redmine.response.UserResponse;
import name.peterbukhal.android.redmine.service.redmine.response.UsersResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public interface Redmine {

    @GET("/users.json")
    Call<UsersResponse> users();

    @GET("/users/current.json")
    Call<CurrentResponse> currentUser();

    @GET("/users/{id}.json")
    Call<UserResponse> user(@Path("id") int id);

    /**
     * Returns a paginated list of issues. By default, it returns open issues only.
     *
     * @param offset skip this number of issues in response (optional)
     * @param limit number of issues per page (optional)
     * @param sort column to sort with (optional). Append :desc to invert the order.
     *
     * @param issueIds get issue with the given id or multiple issues by id using ',' to separate id.
     * @param projectIds get issues from the project with the given id
     *                  (a numeric value, not a project identifier).
     * @param subProjectIds get issues from the subproject with the given id.
     *                     You can use project_id=XXX&subproject_id=!* to get
     *                     only the issues of a given project and none of its subprojects.
     * @param trackerIds get issues from the tracker with the given id
     * @param statusIds get issues with the given status id only.
     *                  Possible values: open, closed, * to get open and closed issues, status id
     * @param assignedToId get issues which are assigned to the given user id.
     *                       me can be used instead an ID to fetch all issues
     *                       from the logged in user (via API key or HTTP auth)
     * @param cfX get issues with the given value for custom field with an ID of x.
     *             (Custom field must have 'used as a filter' checked.)
     *
     * NB: operators containing ">", "<" or "=" should be hex-encoded so they're parsed correctly.
     *     Most evolved API clients will do that for you by default but for the sake of clarity
     *     the following examples have been written with no such magic feature in mind.
     *
     * @return list of issues
     */
    @GET("/issues.json")
    Call<IssuesResponse> issues(
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("sort") String sort,
            @Query("issue_id") String issueIds,
            @Query("project_id") String projectIds,
            @Query("subproject_id") String subProjectIds,
            @Query("tracker_id") String trackerIds,
            @Query("watcher_id") String watcherId,
            @Query("author_id") String authorId,
            @Query("status_id") String statusIds,
            @Query("assigned_to_id") String assignedToId,
            @Query("cf_x") String cfX);

    /**
     * Get a particular issue.
     *
     * @param id issue id
     * @param include fetch associated data (optional, use comma to fetch multiple associations).
     *                Possible values:
     *                  children, attachments, relations, changesets,
     *                  journals - See Issue journals for more information,
     *                  watchers - Since 2.3.0
     * @return particular issue
     */
    @GET("/issues/{id}.json")
    Call<IssueResponse> issue(
            @Path("id") int id,
            @Query("include") String include);

    @POST("/issues.json")
    Call<Void> createIssue();

    @GET("/projects.json")
    Call<ProjectsResponse> projects();

    @GET("/time_entries.json")
    Call<TimeEntriesResponse> timeEntries();

    @DELETE("/issues/{id}.json")
    Call<ResponseBody> removeIssue(@Path("id") int id);

    @POST("/issues/{id}/watchers.json")
    Call<ResponseBody> addWatcher(@Path("id") int id, @Query("user_id") int userId);

    @DELETE("/issues/{id}/watchers/{user_id}.json")
    Call<ResponseBody> removeWatcher(@Path("id") int id, @Path("user_id") int userId);

    @DELETE("/attachments/{attachmentId}.json")
    Call<ResponseBody> removeAttachment(@Path("attachmentId") int attachmentId);

    @GET("/issue_statuses.json")
    Call<IssueStatusesResponse> issueStatuses();

    @PUT("/issues/{id}.json")
    Call<ResponseBody> editIssue(@Path("id") int id, @Body EditIssueRequest request);

}
