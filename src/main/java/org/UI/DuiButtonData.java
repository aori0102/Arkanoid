package org.UI;

import utils.Vector2;

public enum DuiButtonData {

    Continue(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0), new Vector2(0.0, 0.0)),
    Menu(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0), new Vector2(0.0, 0.0)),
    Options(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0), new Vector2(0.0, 0.0)),
    Pause(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0), new Vector2(0.0, 0.0)),
    Quit(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0), new Vector2(0.0, 0.0)),
    Record(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0), new Vector2(0.0, 0.0)),
    Start(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0), new Vector2(0.0, 0.0));

    private final Vector2 idleAnchor;
    private final Vector2 hoverAnchor;
    private final Vector2 activeAnchor;

    DuiButtonData(Vector2 idleAnchor, Vector2 hoverAnchor, Vector2 activeAnchor) {
        this.idleAnchor = idleAnchor;
        this.hoverAnchor = hoverAnchor;
        this.activeAnchor = activeAnchor;
    }

    public Vector2 getIdleAnchor() {
        return idleAnchor;
    }

    public Vector2 getHoverAnchor() {
        return hoverAnchor;
    }

    public Vector2 getActiveAnchor() {
        return activeAnchor;
    }

}
