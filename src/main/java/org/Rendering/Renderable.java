package org.Rendering;

import javafx.scene.Node;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.GameObject.Transform;
import org.Layer.RenderLayer;
import utils.Vector2;

/**
 * Base class for any rendering object.
 */
public abstract class Renderable extends MonoBehaviour {

    private RenderLayer renderLayer = RenderLayer.Default;
    /**
     * The local rendering size for this rendering object.
     */
    private Vector2 size = new Vector2();

    /**
     * The local rendering position for this rendering object, anchored
     * with {@link #pivot}.
     */
    private Vector2 position = new Vector2();

    /**
     * The pivot for this rendering object, meaning
     * the point to anchor the image for rotation and rendering.
     */
    private Vector2 pivot = new Vector2();

    /**
     * Fired when this rendering object's {@link RenderLayer} is changed.<br><br>
     * <b><i><u>NOTE</u>: This function should only be listened to by
     * {@link RendererManager} to handle per-layer rendering.</i></b>
     */
    public EventHandler<OnRenderLayerChangedEventArgs> onRenderLayerChanged = new EventHandler<>(Renderable.class);

    /**
     * Event argument for {@link #onRenderLayerChanged}.<br><br>
     * Holds the changes of this rendering object's {@link RenderLayer}
     *
     * @param previousLayer The layer before the change.
     * @param newLayer      The current layer after the change.
     */
    public record OnRenderLayerChangedEventArgs(
            RenderLayer previousLayer,
            RenderLayer newLayer
    ) {
    }

    /**
     * Fired when the {@link #pivot} of this rendering object changes.
     */
    public EventHandler<Void> onPivotChanged = new EventHandler<>(Renderable.class);

    /**
     * Fired when the rendering position of this rendering
     * object changes. This also consider changes from
     * {@link Transform#getGlobalPosition()}.
     */
    public EventHandler<Void> onRenderPositionChanged = new EventHandler<>(Renderable.class);

    /**
     * Fired when the rendering size of this rendering object
     * changes. This also consider changes from
     * {@link Transform#getGlobalScale()}.
     */
    public EventHandler<Void> onRenderSizeChanged = new EventHandler<>(Renderable.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Renderable(GameObject owner) {
        super(owner);
        getTransform().onPositionChanged.addListener(this::transform_onPositionChanged);
        getTransform().onScaleChanged.addListener(this::transform_onScaleChanged);
        gameObject.onObjectActivenessChanged.addListener(this::gameObject_onObjectActivenessChanged);
    }

    @Override
    public final void awake() {
        RendererManager.registerNode(this);
        getNode().setVisible(gameObject.isActive());
        rendererAwake();
    }

    @Override
    protected final void onDestroy() {
        RendererManager.unregisterNode(this);
    }

    /**
     * Set the rendering layer for this renderer.
     *
     * @param renderLayer The layer this object should render to.
     */
    public final void setRenderLayer(RenderLayer renderLayer) {
        onRenderLayerChanged
                .invoke(this, new OnRenderLayerChangedEventArgs(this.renderLayer, renderLayer));
        this.renderLayer = renderLayer;
    }

    /**
     * Called when {@link GameObject#onObjectActivenessChanged}
     * is invoked. This function will modify renderer visibility
     * based on the {@link GameObject}'s activeness.
     *
     * @param sender {@link GameObject}.
     * @param e      Empty event argument.
     */
    private void gameObject_onObjectActivenessChanged(Object sender, Void e) {
        getNode().setVisible(gameObject.isActive());
    }

    /**
     * Called when {@link Transform#onPositionChanged} is invoked.
     * This function will calculate the rendering position and
     * invoke {@link #onRenderPositionChanged} as a result.
     *
     * @param sender {@link Transform}.
     * @param e      Empty event argument.
     */
    private void transform_onPositionChanged(Object sender, Void e) {
        onRenderPositionChanged.invoke(this, null);
    }

    /**
     * Called when {@link Transform#onScaleChanged} is invoked.
     * This function will calculate the rendering size and invoke
     * {@link #onRenderSizeChanged} as a result.
     *
     * @param sender {@link Transform}.
     * @param e      Empty event argument.
     */
    private void transform_onScaleChanged(Object sender, Void e) {
        onRenderSizeChanged.invoke(this, null);
    }

    /**
     * Secondary function followed after initial {@link #awake()}.
     */
    protected void rendererAwake() {
    }

    public final RenderLayer getRenderLayer() {
        return renderLayer;
    }

    /**
     * Get this rendering object's {@link Node} from JavaFX system.
     * This node is used to match this engine's logic with the one
     * from JavaFX.
     *
     * @return JavaFX's rendering {@link Node}.
     */
    public abstract Node getNode();

    /**
     * Set the {@link #pivot} for this object.
     *
     * @param pivot The pivot to set.
     */
    public final void setPivot(Vector2 pivot) {
        pivot.x = Math.clamp(pivot.x, 0, 1);
        pivot.y = Math.clamp(pivot.y, 0, 1);
        this.pivot = pivot;
        onPivotChanged.invoke(this, null);
    }

    /**
     * Set the local rendering {@link #size} for
     * this object.
     *
     * @param size The local rendering size to set.
     */
    public final void setSize(Vector2 size) {
        this.size = size;
        onRenderSizeChanged.invoke(this, null);
        onPivotChanged.invoke(this, null);
    }

    /**
     * Set the local rendering {@link #position} for
     * this object.
     *
     * @param position The local rendering position to set.
     */
    public final void setPosition(Vector2 position) {
        this.position = position;
        onRenderPositionChanged.invoke(this, null);
    }

    /**
     * Get the local rendering {@link #size} of
     * this object.
     *
     * @return The local rendering size.
     */
    public final Vector2 getSize() {
        return size;
    }

    /**
     * Get the local rendering {@link #position} of
     * this object.
     *
     * @return The local rendering position.
     */
    public final Vector2 getPosition() {
        return position;
    }

    /**
     * Get the {@link #pivot} of this object.
     *
     * @return The pivot of this object.
     */
    public final Vector2 getPivot() {
        return pivot;
    }

    /**
     * Get the minimum bound along the {@code X} axis
     * of this renderer, considering its global rendering
     * size and rendering position.
     *
     * @return The minimum bound along the {@code X} axis.
     */
    public final double minX() {
        return getRenderPosition().x;
    }

    /**
     * Get the maximum bound along the {@code X} axis
     * of this renderer, considering its global rendering
     * size and rendering position.
     *
     * @return The maximum bound along the {@code X} axis.
     */
    public final double maxX() {
        return minX() + getRenderSize().x;
    }

    /**
     * Get the minimum bound along the {@code Y} axis
     * of this renderer, considering its global rendering
     * size and rendering position.
     *
     * @return The minimum bound along the {@code Y} axis.
     */
    public final double minY() {
        return getTransform().getGlobalPosition().y + position.y;
    }

    /**
     * Get the maximum bound along the {@code Y} axis
     * of this renderer, considering its global rendering
     * size and rendering position.
     *
     * @return The maximum bound along the {@code Y} axis.
     */
    public final double maxY() {
        return minY() + getRenderSize().y;
    }

    /**
     * Get the global rendering position of this renderer, after
     * considering its local rendering {@link #position} and
     * this object's {@link Transform#getGlobalPosition()}.
     *
     * @return The global rendering position of this renderer.
     */
    public final Vector2 getRenderPosition() {
        return getTransform().getGlobalPosition()
                .add(position)
                .subtract(pivot.scaleUp(getRenderSize()));
    }

    /**
     * Get the global rendering size of this renderer, after considering
     * its local rendering {@link #position} and this object's
     * {@link Transform#getGlobalScale()}.
     *
     * @return The global rendering size of this renderer.
     */
    public final Vector2 getRenderSize() {
        return getTransform().getGlobalScale().scaleUp(size);
    }

    /**
     * Get the pivot point of this rendering object in
     * global space.
     *
     * @return The pivot point in global space.
     */
    public final Vector2 getPivotPoint() {
        return getRenderPosition().add(pivot.scaleUp(getRenderSize()));
    }

}