package ecs;

import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class EditorManager {

    /**
     * Initialize the editor window.
     * Should only be called within {@code main}.
     */
    public static void initializeEditor() {

        System.out.println("Initializing Editor");

        // Second window (e.g. Project Hierarchy)
        Stage hierarchyStage = new Stage();
        hierarchyStage.setTitle("Project Hierarchy");

        TreeItem<String> rootItem = new TreeItem<>("Project");
        rootItem.setExpanded(true);
        rootItem.getChildren().addAll(
                new TreeItem<>("Assets"),
                new TreeItem<>("Scripts"),
                new TreeItem<>("Scenes")
        );
        TreeView<String> tree = new TreeView<>(rootItem);

        hierarchyStage.setScene(new Scene(tree, 250, 400));
        hierarchyStage.show();

        System.out.println("Done");

    }

}
