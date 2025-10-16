package org.Animation;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.Rendering.ImageAsset;

import java.lang.reflect.Type;
import java.util.List;

public class AnimationClipAdapter implements
        JsonSerializer<SpriteAnimationClip>,
        JsonDeserializer<SpriteAnimationClip> {

    private static final String IMAGE_PATH_PREFIX = "imagePath";
    private static final String FRAME_LIST_PREFIX = "frameList";
    private static final String LOOP_PREFIX = "loop";

    @Override
    public SpriteAnimationClip deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        ImageAsset.ImageIndex imageIndex = context.deserialize(obj.get(IMAGE_PATH_PREFIX), ImageAsset.ImageIndex.class);
        List<AnimationFrame> frameList = context.deserialize(obj.get(FRAME_LIST_PREFIX),
                new TypeToken<List<AnimationFrame>>() {
                }.getType());
        boolean loop = obj.get(LOOP_PREFIX).getAsBoolean();

        SpriteAnimationClip clip = new SpriteAnimationClip();
        clip.setSpriteSheet(imageIndex);
        for (var frame : frameList) {
            clip.addFrame(frame);
        }
        clip.setLoop(loop);
        return clip;
    }

    @Override
    public JsonElement serialize(SpriteAnimationClip clip, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.add(IMAGE_PATH_PREFIX, context.serialize(clip.getImageIndex()));
        obj.add(FRAME_LIST_PREFIX, context.serialize(clip.getFrameList()));
        obj.add(LOOP_PREFIX, context.serialize(clip.getLoop()));
        return obj;
    }

}
