package name.peterbukhal.android.redmine.service.response;

import java.util.Collections;
import java.util.List;

import name.peterbukhal.android.redmine.service.model.Project;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class ProjectsResponse {

    private List<Project> projects;

    public List<Project> getProjects() {
        if (projects == null) {
            projects = Collections.emptyList();
        }

        return projects;
    }

}
