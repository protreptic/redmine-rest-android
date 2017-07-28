package name.peterbukhal.android.redmine.realm.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import name.peterbukhal.android.redmine.realm.StatusRealm;

public final class StatusDeserializer implements JsonDeserializer<StatusRealm> {

    @Override
    public StatusRealm deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        StatusRealm statusRealm = new StatusRealm();
        statusRealm.setId(jsonObject.get("id").getAsInt());
        statusRealm.setName(jsonObject.get("name").getAsString());

        return statusRealm;
    }

}
