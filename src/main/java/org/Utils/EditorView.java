package org.Utils;

import javafx.beans.Observable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Scene.SceneKey;
import org.Scene.SceneManager;

import java.util.HashMap;

public final class EditorView {

    private static class TreeInfo {
        public TreeItem<String> root;
        public TreeView<String> view;
    }

    private final static Stage stage = new Stage();
    private final static Group root = new Group();
    private final static Scene scene = new Scene(root, 600.0, 450.0);
    private final static HashMap<SceneKey, TreeInfo> treeInfoMap = new HashMap<>();
    private final static HashMap<GameObject, TreeItem<String>> treeNodeMap = new HashMap<>();
    private final static VBox hierarchyPanel = new VBox();
    private final static VBox inspectionPanel = new VBox();
    private final static ComboBox<SceneKey> sceneSelector = new ComboBox<>();

    private static GameObject inspectorSelectedGameObject = null;

    public static void init() {

        initHierarchy();
        initInspector();

        root.getChildren().add(hierarchyPanel);
        root.getChildren().add(inspectionPanel);
        inspectionPanel.setPrefWidth(200.0);
        inspectionPanel.setLayoutX(350.0);
        inspectionPanel.setLayoutY(10.0);
        hierarchyPanel.setLayoutY(10.0);
        hierarchyPanel.setLayoutX(10.0);
        hierarchyPanel.setMaxHeight(420.0);
        stage.setTitle("Editor");
        stage.setScene(scene);
        stage.show();

    }

    private static void initHierarchy() {

        var title = new Label("Hierarchy");
        var content = new VBox();

        var keyArray = SceneKey.values();
        for (var key : keyArray) {

            var keyName = key.name();
            var panel = new TitledPane();

            sceneSelector.getItems().add(key);

            panel.setText(keyName);
            TreeItem<String> treeRoot = new TreeItem<>(keyName);
            TreeView<String> treeView = new TreeView<>(treeRoot);
            treeView.getSelectionModel().selectedItemProperty()
                    .addListener(EditorView::onSelectingGameObject);
            treeRoot.setExpanded(true);
            var treeInfo = new TreeInfo();
            treeInfo.root = treeRoot;
            treeInfo.view = treeView;
            treeInfoMap.put(key, treeInfo);
            panel.setContent(treeView);

        }

        sceneSelector.setOnAction(e -> {
            SceneKey selected = sceneSelector.getValue();
            for (var key : keyArray) {
                if (key == selected) {
                    content.getChildren().clear();
                    content.getChildren().add(treeInfoMap.get(key).view);
                }
            }
        });

        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);
        hierarchyPanel.setSpacing(10);
        hierarchyPanel.getChildren().add(title);
        hierarchyPanel.getChildren().add(sceneSelector);
        hierarchyPanel.getChildren().add(content);

        GameObjectManager.onGameObjectInstantiated.addListener(EditorView::onGameObjectInstantiated);
        GameObjectManager.onGameObjectDestroyed.addListener(EditorView::onGameObjectDestroyed);

    }

    private static void initInspector() {

        var title = new Label("Inspector");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);
        inspectionPanel.getChildren().add(title);
        inspectionPanel.setSpacing(10);

    }

    private static void onSelectingGameObject(Observable observable, TreeItem<String> oldValue, TreeItem<String> newValue) {

        if (newValue != null) {

            inspectorSelectedGameObject = null;
            for (var entry : treeNodeMap.entrySet()) {
                if (entry.getValue() == newValue) {
                    inspectorSelectedGameObject = entry.getKey();
                    break;
                }
            }

            if (inspectorSelectedGameObject != null) {

                // do something with the selected GameObject
                inspectionPanel.getChildren().clear();
                var title = new Label("Inspector");
                var objectNameLabel = new Label(inspectorSelectedGameObject.getName());
                title.setAlignment(Pos.CENTER);
                title.setMaxWidth(Double.MAX_VALUE);
                objectNameLabel.setAlignment(Pos.CENTER);
                objectNameLabel.setMaxWidth(Double.MAX_VALUE);
                objectNameLabel.setStyle("""
                                            -fx-font-size: 18px;
                                            -fx-font-weight: bold;
                        """);
                inspectionPanel.getChildren().add(title);
                inspectionPanel.getChildren().add(objectNameLabel);

                for (var component : inspectorSelectedGameObject.getAllComponents()) {
                    processComponent(component);
                }

            }

        }

    }

    private static void processComponent(MonoBehaviour component) {

        var componentName = component.getClass().getSimpleName();

        var label = new Label(componentName);
        label.setStyle("""
                    -fx-font-size: 12px;
                """);
        inspectionPanel.getChildren().add(label);

    }

    private static void onGameObjectInstantiated(Object sender, GameObject e) {

        e.onParentChanged.addListener(EditorView::gameObject_onParentChanged);

        var node = new TreeItem<>(e.getName());
        treeNodeMap.put(e, node);
        if (e.getParent() == null) {
            var treeRoot = treeInfoMap.get(e.getRegisteredSceneKey()).root;
            treeRoot.getChildren().add(node);
        } else {
            var parentNode = treeNodeMap.get(e.getParent());
            parentNode.getChildren().add(node);
        }

    }

    private static void gameObject_onParentChanged(Object sender, GameObject.OnParentChangedEventArgs e) {

        if (sender instanceof GameObject object) {

            var node = treeNodeMap.get(object);
            var treeRoot = treeInfoMap.get(object.getRegisteredSceneKey()).root;
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
        if (e.getParent() == null) {
            var treeRoot = treeInfoMap.get(e.getRegisteredSceneKey()).root;
            treeRoot.getChildren().remove(node);
        } else {
            var parentNode = treeNodeMap.get(e.getParent());
            if (parentNode != null) {
                parentNode.getChildren().remove(node);
            }
        }

        treeNodeMap.remove(e);

        if (e == inspectorSelectedGameObject) {
            inspectionPanel.getChildren().clear();
            var title = new Label("Inspector");
            title.setAlignment(Pos.CENTER);
            title.setMaxWidth(Double.MAX_VALUE);
            inspectionPanel.getChildren().add(title);
            inspectorSelectedGameObject = null;
        }

    }

    public static void wakeHierarchy() {
        sceneSelector.getSelectionModel().selectFirst();
    }

}