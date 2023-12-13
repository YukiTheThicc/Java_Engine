package diamondEngine.diaUtils.serializers;

import com.google.gson.*;
import diamondEngine.Entity;
import diamondEngine.Environment;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaComponents.Transform;

import java.lang.reflect.Type;

public class EntitySerializer implements JsonDeserializer<Entity> {

    // CONSTRUCTOR
    public EntitySerializer() {
    }

    // METHODS
    @Override
    public Entity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray components = jsonObject.getAsJsonArray("components");
        JsonElement uuid = jsonObject.get("uuid");
        JsonElement name = jsonObject.get("name");

        Entity entity = new Entity(uuid.getAsString());
        entity.setName(name.getAsString());
        for (JsonElement j : components) {
            Component c = context.deserialize(j, Component.class);
            entity.addComponent(c);
        }
        return entity;
    }
}
