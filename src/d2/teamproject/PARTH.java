package d2.teamproject;

import d2.teamproject.gui.MainMenuView;
import d2.teamproject.module.ModuleLoader;
import javafx.application.Application;
import javafx.stage.Stage;

public class PARTH extends Application {
    public static final double MIN_WIDTH = 1280;
    public static final double MIN_HEIGHT = 720;

    private ModuleLoader loader;
    private MainMenuView menu;

    public PARTH() {
        loader = ModuleLoader.getInstance();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO: Show "loading" screen

        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        // Start ModuleLoader loading
        loader.loadAllModules((module, current, max) ->
                System.out.printf("%d/%d: %s\n", current + 1, max, module.getName()));
        loader.onLoaded(modules -> {
            menu = new MainMenuView(modules, primaryStage);
            primaryStage.setScene(menu.getScene());
            primaryStage.show();
        });
    }

    public static void main(String[] args) {
        Application.launch(PARTH.class, args);
    }
}
