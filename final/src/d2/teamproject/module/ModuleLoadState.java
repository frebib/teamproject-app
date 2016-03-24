package d2.teamproject.module;

/**
 * Provides a callback for progress when loading modules
 * @author Joseph Groocock
 */
@FunctionalInterface
public interface ModuleLoadState {
    /**
     * A callback for module load progress
     * @param module  the current module just loaded
     * @param current index of current module
     * @param max     maximum number of modules to load
     */
    void onLoadProgress(BaseController module, int current, int max);
}
