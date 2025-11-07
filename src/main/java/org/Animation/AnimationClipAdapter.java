package org.Animation;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.Rendering.ImageAsset;
import utils.Vector2;

import java.lang.reflect.Type;
import java.util.List;

/**
 * JSON serializer and deserializer for {@link SpriteAnimationClip}.
 */
public class AnimationClipAdapter implements
        JsonSerializer<SpriteAnimationClip>,
        JsonDeserializer<SpriteAnimationClip> {

    private static final String IMAGE_PATH_PREFIX = "imagePath";
    private static final String FRAME_LIST_PREFIX = "frameList";
    private static final String RENDER_SIZE_PREFIX = "renderSize";
    private static final String PIVOT_PREFIX = "pivot";
    private static final String LOOP_PREFIX = "loop";

    @Override
    public SpriteAnimationClip deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        ImageAsset.ImageIndex imageIndex = context.deserialize(obj.get(IMAGE_PATH_PREFIX), ImageAsset.ImageIndex.class);
        List<AnimationFrame> frameList = context.deserialize(obj.get(FRAME_LIST_PREFIX),
                new TypeToken<List<AnimationFrame>>() {
                }.getType());
        Vector2 renderSize = context.deserialize(obj.get(RENDER_SIZE_PREFIX), Vector2.class);
        Vector2 pivot = context.deserialize(obj.get(PIVOT_PREFIX), Vector2.class);
        boolean loop = obj.get(LOOP_PREFIX).getAsBoolean();

        SpriteAnimationClip clip = new SpriteAnimationClip();
        clip.setSpriteSheet(imageIndex == null ? ImageAsset.ImageIndex.None : imageIndex);
        clip.setPivot(pivot == null ? Vector2.zero() : pivot);
        clip.setRenderSize(renderSize == null ? Vector2.zero() : renderSize);
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
        obj.add(RENDER_SIZE_PREFIX, context.serialize(clip.getRenderSize()));
        obj.add(PIVOT_PREFIX, context.serialize(clip.getPivot()));
        obj.add(LOOP_PREFIX, context.serialize(clip.getLoop()));
        return obj;
    }

}
