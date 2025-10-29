package game.Voltraxis;

import game.Voltraxis.Prefab.EffectIconUIPrefab;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Vector2;

import java.util.LinkedHashMap;

public final class VoltraxisEffectBarUI extends MonoBehaviour {

    private static final int MAX_ICON_PER_ROW = 6;
    private static final Vector2 ICON_OFFSET = new Vector2(35.0, 20.0);

    private final LinkedHashMap<VoltraxisEffectManager.EffectInputInfo, VoltraxisEffectIconUI> effectIconUIMap = new LinkedHashMap<>();

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisEffectBarUI(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        Voltraxis.getInstance().getVoltraxisEffectManager().onEffectAdded
                .addListener(this::voltraxisEffectManager_onEffectAdded);
        Voltraxis.getInstance().getVoltraxisEffectManager().onEffectRemoved
                .addListener(this::voltraxisEffectManager_onEffectRemoved);
    }

    /**
     * Called when {@link VoltraxisEffectManager#onEffectAdded} is invoked.<br><br>
     * This function adds a new icon UI as a new effect is added.
     *
     * @param sender Event caller {@link VoltraxisEffectManager}.
     * @param e      Event argument containing effect info.
     */
    private void voltraxisEffectManager_onEffectAdded(Object sender, VoltraxisEffectManager.EffectInputInfo e) {
        var infoUI = new EffectIconUIPrefab(e.index).instantiatePrefab()
                .getComponent(VoltraxisEffectIconUI.class);
        infoUI.getGameObject().setParent(gameObject);
        effectIconUIMap.put(e, infoUI);
        updateIconList();
    }

    /**
     * Called when {@link VoltraxisEffectManager#onEffectRemoved} is invoked.<br><br>
     * This function removes the existing icon UI based on the effect to be removed.
     *
     * @param sender Event caller {@link VoltraxisEffectManager}.
     * @param e      Event argument containing effect info.
     */
    private void voltraxisEffectManager_onEffectRemoved(Object sender, VoltraxisEffectManager.EffectInputInfo e) {
        GameObjectManager.destroy(effectIconUIMap.remove(e).getGameObject());
        updateIconList();
    }

    /**
     * Utility function to update all icon's positioning after addition or removal.
     */
    private void updateIconList() {

        var iterator = effectIconUIMap.entrySet().iterator();
        int index = 0;
        while (iterator.hasNext()) {

            var iconUI = iterator.next().getValue();
            var row = index / MAX_ICON_PER_ROW;
            var column = index % MAX_ICON_PER_ROW;

            iconUI.setTargetPosition(new Vector2(column, row).scaleUp(ICON_OFFSET));

            index++;

        }

    }

}