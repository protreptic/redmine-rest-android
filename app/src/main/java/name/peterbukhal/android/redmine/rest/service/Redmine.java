package name.peterbukhal.android.redmine.rest.service;

import name.peterbukhal.android.redmine.rest.service.response.IssuesResponse;
import name.peterbukhal.android.redmine.rest.service.response.ProjectsResponse;
import name.peterbukhal.android.redmine.rest.service.response.TimeEntriesResponse;
import name.peterbukhal.android.redmine.rest.service.response.UsersResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public interface Redmine {

    @GET("/issues.json")
    Call<IssuesResponse> issues();

    @GET("/projects.json")
    Call<ProjectsResponse> projects();

    @GET("/users.json")
    Call<UsersResponse> users();

    @GET("/time_entries.json")
    Call<TimeEntriesResponse> timeEntries();

}
