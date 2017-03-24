package name.peterbukhal.android.redmine.rest.service;

import name.peterbukhal.android.redmine.rest.service.response.CurrentResponse;
import name.peterbukhal.android.redmine.rest.service.response.IssueResponse;
import name.peterbukhal.android.redmine.rest.service.response.IssuesResponse;
import name.peterbukhal.android.redmine.rest.service.response.ProjectsResponse;
import name.peterbukhal.android.redmine.rest.service.response.TimeEntriesResponse;
import name.peterbukhal.android.redmine.rest.service.response.UserResponse;
import name.peterbukhal.android.redmine.rest.service.response.UsersResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

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

    @GET("/issues.json")
    Call<IssuesResponse> issues();

    @GET("/issues/{id}.json")
    Call<IssueResponse> issue(@Path("id") int id);

    @GET("/projects.json")
    Call<ProjectsResponse> projects();

    @GET("/time_entries.json")
    Call<TimeEntriesResponse> timeEntries();

}
