package d2.teamproject;

import d2.teamproject.gui.MainMenuView;
import d2.teamproject.module.ModuleLoader;
import javafx.application.Application;
import javafx.stage.Stage;

public class PARTH extends Application {
    private ModuleLoader loader;
    private MainMenuView menu;

    public PARTH() {
        loader = ModuleLoader.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO: Show "loading" screen

        // Start ModuleLoader loading
        loader.loadAllModules((module, current, max) -> System.out.printf("%d/%d: %s\n", current + 1, max, module.getName()));
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
