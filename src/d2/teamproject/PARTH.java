package d2.teamproject;

import d2.teamproject.gui.MainMenuView;
import d2.teamproject.gui.VisView;
import d2.teamproject.module.ModuleLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PARTH extends Application {
    private ModuleLoader loader;
    private MainMenuView menu;
    private VisView visView;

    public PARTH() {
        loader = ModuleLoader.getInstance();
        visView = new VisView();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO: Show "loading" screen

        // Start ModuleLoader loading
        loader.loadAllModules((module, current, max) -> System.out.printf("%d/%d: %s\n", current + 1, max, module.getName()));
        loader.onLoaded(modules -> {
            menu = new MainMenuView(modules, visView);
            Scene menuStage = new Scene(menu.getMenu());
            primaryStage.setScene(menuStage);
            primaryStage.show();
        });
    }

    public static void main(String[] args) {
        Application.launch(PARTH.class, args);
    }
}
