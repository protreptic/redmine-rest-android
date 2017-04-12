package name.peterbukhal.android.redmine.realm.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import name.peterbukhal.android.redmine.realm.Status;

public final class StatusDeserializer implements JsonDeserializer<Status> {

    @Override
    public Status deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Status status = new Status();
        status.setId(jsonObject.get("id").getAsInt());
        status.setName(jsonObject.get("name").getAsString());

        return status;
    }

}
