package org;

import javafx.scene.Node;
import utils.Vector2;

public abstract class Renderable extends MonoBehaviour {

    private RenderLayer renderLayer = RenderLayer.Default;
    private Vector2 size = new Vector2();
    private Vector2 position = new Vector2();
    private Vector2 pivot = new Vector2();

    public EventHandler<OnRenderLayerChangedEventArgs> onRenderLayerChanged = new EventHandler<>(this);

    public record OnRenderLayerChangedEventArgs(
            RenderLayer previousLayer,
            RenderLayer newLayer
    ) {
    }

    public EventHandler<Void> onPivotChanged = new EventHandler<>(this);
    public EventHandler<Void> onRenderPositionChanged = new EventHandler<>(this);
    public EventHandler<Void> onRenderSizeChanged = new EventHandler<>(this);

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

    public final void setRenderLayer(RenderLayer renderLayer) {
        onRenderLayerChanged
                .invoke(this, new OnRenderLayerChangedEventArgs(this.renderLayer, renderLayer));
        this.renderLayer = renderLayer;
    }

    @Override
    public final void awake() {
        RendererManager.registerNode(this);
        getNode().setVisible(gameObject.isActive());
        rendererAwake();
    }

    private void gameObject_onObjectActivenessChanged(Object sender, Void e) {
        System.out.println(gameObject.getName() + " active: " + gameObject.isActive());
        getNode().setVisible(gameObject.isActive());
    }

    private void transform_onPositionChanged(Object sender, Void e) {
        onRenderPositionChanged.invoke(this, null);
    }

    private void transform_onScaleChanged(Object sender, Void e) {
        onRenderSizeChanged.invoke(this, null);
    }

    protected void rendererAwake() {
    }

    public final RenderLayer getRenderLayer() {
        return renderLayer;
    }

    @Override
    protected final void destroyComponent() {

        RendererManager.unregisterNode(this);

        pivot = null;
        size = null;
        position = null;
        onRenderSizeChanged = null;
        onPivotChanged = null;
        onRenderPositionChanged = null;
        onRenderLayerChanged = null;

        onComponentDestroyed();
    }

    public abstract Node getNode();

    protected abstract void onComponentDestroyed();

    public void setPivot(Vector2 pivot) {
        pivot.x = Math.clamp(pivot.x, 0, 1);
        pivot.y = Math.clamp(pivot.y, 0, 1);
        this.pivot = pivot;
        onPivotChanged.invoke(this, null);
    }

    public void setSize(Vector2 size) {
        this.size = size;
        onRenderSizeChanged.invoke(this, null);
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        onRenderPositionChanged.invoke(this, null);
    }

    public Vector2 getSize() {
        return size;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getPivot() {
        return pivot;
    }

    public double minX() {
        return getRenderPosition().x;
    }

    public double maxX() {
        return minX() + getRenderSize().x;
    }

    public double minY() {
        return getTransform().getGlobalPosition().y + position.y;
    }

    public double maxY() {
        return minY() + getRenderSize().y;
    }

    public Vector2 getRenderPosition() {
        return getTransform().getGlobalPosition()
                .add(position)
                .subtract(pivot.scaleUp(getRenderSize()));
    }

    public Vector2 getRenderSize() {
        return getTransform().getGlobalScale().scaleUp(size);
    }

    public Vector2 getPivotPoint() {
        return getRenderPosition().add(pivot.scaleUp(getRenderSize()));
    }

}
