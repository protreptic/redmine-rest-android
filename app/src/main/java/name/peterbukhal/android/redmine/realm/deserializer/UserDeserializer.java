package name.peterbukhal.android.redmine.realm.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import name.peterbukhal.android.redmine.realm.User;

public final class UserDeserializer implements JsonDeserializer<User> {

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        User user = new User();
        user.setId(jsonObject.get("id").getAsInt());
        user.setLogin(jsonObject.get("login").getAsString());
        user.setFirstname(jsonObject.get("firstname").getAsString());
        user.setLastname(jsonObject.get("lastname").getAsString());
        user.setMail(jsonObject.get("mail").getAsString());
        user.setCreatedOn(jsonObject.get("created_on").getAsString());
        user.setLastLoginOn(jsonObject.get("last_login_on").getAsString());
        user.setApiKey(jsonObject.get("api_key").getAsString());

        return user;
    }

}
