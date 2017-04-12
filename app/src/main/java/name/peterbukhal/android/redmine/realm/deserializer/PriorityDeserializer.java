package name.peterbukhal.android.redmine.realm.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import name.peterbukhal.android.redmine.realm.Priority;

public final class PriorityDeserializer implements JsonDeserializer<Priority> {

    @Override
    public Priority deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Priority priority = new Priority();
        priority.setId(jsonObject.get("id").getAsInt());
        priority.setName(jsonObject.get("name").getAsString());

        return priority;
    }

}
