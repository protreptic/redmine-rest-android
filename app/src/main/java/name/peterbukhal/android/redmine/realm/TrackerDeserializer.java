package name.peterbukhal.android.redmine.realm;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class TrackerDeserializer implements JsonDeserializer<Tracker> {

    @Override
    public Tracker deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Tracker tracker = new Tracker();
        tracker.setId(jsonObject.get("id").getAsInt());
        tracker.setName(jsonObject.get("name").getAsString());

        return tracker;
    }

}
