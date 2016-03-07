package d2.teamproject.module;

public interface ModuleLoadState {
    /**
     * A callback for module load progress
     * @param module  the current module just loaded
     * @param current index of current module
     * @param max     maximum number of modules to load
     */
    void onLoadProgress(BaseController module, int current, int max);
}
