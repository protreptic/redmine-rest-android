package name.peterbukhal.android.redmine.realm;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ProjectDeserializer implements JsonDeserializer<Project> {

    @Override
    public Project deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Project project = new Project();
        project.setId(jsonObject.get("id").getAsInt());
        project.setName(jsonObject.get("name").getAsString());

        if (jsonObject.get("identifier") != null) {
            project.setIdentifier(jsonObject.get("identifier").getAsString());
        }

        if (jsonObject.get("description") != null) {
            project.setDescription(jsonObject.get("description").getAsString());
        }

        if (jsonObject.get("status") != null) {
            project.setStatus(jsonObject.get("status").getAsInt());
        }
//
//        project.setParent(context.<Project>deserialize(jsonObject.get("parent"), Project.class));
//        project.setPublic(jsonObject.get("is_public").getAsBoolean());
//        project.setCreatedOn(jsonObject.get("created_on").getAsString());
//        project.setUpdatedOn(jsonObject.get("updated_on").getAsString());

        return project;
    }

}
