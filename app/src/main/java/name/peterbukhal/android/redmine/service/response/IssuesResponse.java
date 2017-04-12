package name.peterbukhal.android.redmine.service.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import name.peterbukhal.android.redmine.realm.Issue;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 22.03.2017.
 */
public final class IssuesResponse {

    private List<Issue> issues;

    @SerializedName("total_count")
    private int totalCount;

    private int offset;
    private int limit;

    public List<Issue> getIssues() {
        return issues;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

}
