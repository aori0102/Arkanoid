package org;

import javafx.scene.Node;

public interface IHasNode {
    public default Node getNode() {
        return null;
    }
}
