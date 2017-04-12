package name.peterbukhal.android.redmine.realm;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class IssueDeserializer implements JsonDeserializer<Issue> {

    @Override
    public Issue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Issue issue = new Issue();
        issue.setId(jsonObject.get("id").getAsInt());
        issue.setProject(context.<Project>deserialize(jsonObject.get("project"), Project.class));
        issue.setTracker(context.<Tracker>deserialize(jsonObject.get("tracker"), Tracker.class));
        issue.setStatus(context.<Status>deserialize(jsonObject.get("status"), Status.class));
        issue.setPriority(context.<Priority>deserialize(jsonObject.get("priority"), Priority.class));
        issue.setAuthor(context.<Author>deserialize(jsonObject.get("author"), Author.class));
        issue.setSubject(jsonObject.get("subject").getAsString());
        issue.setDescription(jsonObject.get("description").getAsString());
        issue.setStartDate(jsonObject.get("start_date").getAsString());
        issue.setDoneRatio(jsonObject.get("done_ratio").getAsInt());
        issue.setCreatedOn(jsonObject.get("created_on").getAsString());
        issue.setUpdatedOn(jsonObject.get("updated_on").getAsString());

        return issue;
    }

}
