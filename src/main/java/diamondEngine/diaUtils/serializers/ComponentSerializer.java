package diamondEngine.diaUtils.serializers;

import com.google.gson.*;
import diamondEngine.diaComponents.Component;

import java.lang.reflect.Type;

public class ComponentSerializer implements JsonSerializer<Component>, JsonDeserializer<Component> {


    // CONSTRUCTORS
    public ComponentSerializer() {
    }

    // METHODS
    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
        try {
            Component loaded = context.deserialize(element, Class.forName(type));
            loaded.init();
            return loaded;
        } catch (Exception e) {
            throw new JsonParseException("Failed to load component: " + type, e);
        }
    }
}
