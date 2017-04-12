package name.peterbukhal.android.redmine.realm.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import name.peterbukhal.android.redmine.realm.CustomField;

public final class CustomFieldDeserializer implements JsonDeserializer<CustomField> {

    @Override
    public CustomField deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        CustomField customField = new CustomField();
        customField.setId(jsonObject.get("id").getAsInt());
        customField.setName(jsonObject.get("name").getAsString());
        customField.setValue(jsonObject.get("value").getAsString());

        return customField;
    }

}
