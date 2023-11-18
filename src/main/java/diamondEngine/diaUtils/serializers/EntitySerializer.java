package diamondEngine.diaUtils.serializers;

import com.google.gson.*;
import diamondEngine.Entity;
import diamondEngine.Environment;
import diamondEngine.diaComponents.Component;

import java.lang.reflect.Type;

public class EntitySerializer implements JsonDeserializer<Entity> {

    // ATTRIBUTES
    private final Environment env;

    // CONSTRUCTOR
    public EntitySerializer(Environment env) {
        this.env = env;
    }

    // METHODS
    @Override
    public Entity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray components = jsonObject.getAsJsonArray("components");

        Entity entity = new Entity(env);
        for (JsonElement e: components) {
            Component c = context.deserialize(e, Component.class);
            entity.addComponent(c);
        }

        return entity;
    }
}
