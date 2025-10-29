package game.Voltraxis.Prefab;

import org.GameObject.GameObject;

public interface IVoltraxisPrefab {
    /**
     * Create the prefab correspond to what the implementing class
     * represents.
     *
     * @return The prefab representing the implementing prefab class.
     */
    GameObject instantiatePrefab();
}
