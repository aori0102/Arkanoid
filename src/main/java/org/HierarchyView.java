package org;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.util.HashMap;

public final class HierarchyView {

    private final static Stage stage = new Stage();
    private final static Group root = new Group();
    private final static Scene scene = new Scene(root, 500.0, 800.0);
    private final static TreeItem<String> treeRoot = new TreeItem<>("Root");
    private final static TreeView<String> treeView = new TreeView<>(treeRoot);
    private final static HashMap<GameObject, TreeItem<String>> treeNodeMap = new HashMap<>();

    public static void init() {

        root.getChildren().add(treeView);
        treeRoot.setExpanded(true);
        stage.setTitle("Hierarchy");
        stage.setScene(scene);
        stage.show();

        GameObjectManager.onGameObjectInstantiated.addListener(HierarchyView::onGameObjectInstantiated);
        GameObjectManager.onGameObjectDestroyed.addListener(HierarchyView::onGameObjectDestroyed);

    }

    private static void onGameObjectInstantiated(Object sender, GameObject e) {

        e.onParentChanged.addListener(HierarchyView::gameObject_onParentChanged);

        var node = new TreeItem<>(e.getName());
        treeNodeMap.put(e, node);
        if (e.parent == null) {
            treeRoot.getChildren().add(node);
        } else {
            var parentNode = treeNodeMap.get(e.parent);
            parentNode.getChildren().add(node);
        }

    }

    private static void gameObject_onParentChanged(Object sender, GameObject.OnParentChangedEventArgs e) {

        if (sender instanceof GameObject object) {

            var node = treeNodeMap.get(object);
            if (e.previousParent != null) {
                var parentNode = treeNodeMap.get(e.previousParent);
                parentNode.getChildren().remove(node);
            } else {
                treeRoot.getChildren().remove(node);
            }
            if (e.newParent != null) {
                var parentNode = treeNodeMap.get(e.newParent);
                parentNode.getChildren().add(node);
            } else {
                treeRoot.getChildren().add(node);
            }
        }

    }

    private static void onGameObjectDestroyed(Object sender, GameObject e) {

        var node = treeNodeMap.get(e);
        if (e.parent == null) {
            treeRoot.getChildren().remove(node);
        } else {
            var parentNode = treeNodeMap.get(e.parent);
            parentNode.getChildren().remove(node);
        }

        treeNodeMap.remove(e);

    }


}