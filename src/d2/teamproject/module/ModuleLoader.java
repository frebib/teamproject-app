package d2.teamproject.module;

public class ModuleLoader {
    private static ModuleLoader loaderInst = new ModuleLoader();

    public static ModuleLoader getInstance() {
        return loaderInst;
    }

    private ModuleLoader() {
    }
}
