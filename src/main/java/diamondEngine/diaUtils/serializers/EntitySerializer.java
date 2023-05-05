package diamondEngine.diaUtils.serializers;

import com.google.gson.*;
import diamondEngine.Entity;

import java.lang.reflect.Type;

public class EntitySerializer implements JsonDeserializer<Entity> {
    @Override
    public Entity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray components = jsonObject.getAsJsonArray("components");

        Entity entity = new Entity();
        for (JsonElement e: components) {
            Entity c = context.deserialize(e, Entity.class);
        }
        return entity;
    }
}
