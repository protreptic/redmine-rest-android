package name.peterbukhal.android.redmine.realm.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import name.peterbukhal.android.redmine.realm.Author;

public final class AuthorDeserializer implements JsonDeserializer<Author> {

    @Override
    public Author deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Author author = new Author();
        author.setId(jsonObject.get("id").getAsInt());
        author.setName(jsonObject.get("name").getAsString());

        return author;
    }

}
