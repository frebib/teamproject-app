package d2.teamproject.visualisations;

import java.util.ArrayList;
import java.util.List;
public class ModuleLoader {
    private static final String MODULE_PATH = "res/module/";
    private static ModuleLoader instance = new ModuleLoader();

    private List<BaseModule> modules;

    public static ModuleLoader getInstance() {
        return instance;
    }

    private ModuleLoader() {
        modules = new ArrayList<>();
    }

    /**
     * loads all available modules and calls back after each is loaded
     *
     * @param callback callback for loaded module
     */
    public void loadAllModules(ModuleLoadState callback) {
    }

    public List<BaseModule> getLoadedModules() {
        return modules;
    }
}
