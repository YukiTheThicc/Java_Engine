package diamondEngine.diaUtils.serializers;

import com.google.gson.*;
import diamondEngine.DiaEntity;

import java.lang.reflect.Type;

public class EntitySerializer implements JsonDeserializer<DiaEntity> {
    @Override
    public DiaEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray components = jsonObject.getAsJsonArray("components");

        DiaEntity entity = new DiaEntity();
        for (JsonElement e: components) {
            DiaEntity c = context.deserialize(e, DiaEntity.class);
        }
        return entity;
    }
}
