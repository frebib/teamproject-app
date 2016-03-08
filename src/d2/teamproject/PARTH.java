package d2.teamproject;

import d2.teamproject.gui.MainMenuView;
import d2.teamproject.module.ModuleLoader;
import d2.teamproject.util.Log;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.Level;

public class PARTH extends Application {
    public static final double WIDTH = 1280;
    public static final double HEIGHT = 720;

    public static final Log LOG = new Log("Parth", Level.FINEST);

    private static PARTH instance;

    private MainMenuView menu;

    public PARTH() {
        instance = this;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread.getAllStackTraces().keySet().stream().forEach(t -> t.setUncaughtExceptionHandler(LOG));
        // TODO: Show "loading" screen

        try {
            primaryStage.setResizable(false);
            primaryStage.setMinWidth(WIDTH);
            primaryStage.setMinHeight(HEIGHT);

            // Start ModuleLoader loading
            ModuleLoader loader = ModuleLoader.getInstance();
            loader.loadAllModules((module, current, max) ->
                    LOG.info("%d/%d: %s\n", current + 1, max, module.getName()));
            loader.onLoaded(modules -> {
                menu = new MainMenuView(modules, primaryStage);
                primaryStage.setScene(menu.getScene());
                primaryStage.show();
            });
        } catch (Exception e) {
            LOG.exception(e);
            throw e;
        }
    }

    public MainMenuView getMenu() {
        return menu;
    }

    public static PARTH getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Application.launch(PARTH.class, args);
    }
}
