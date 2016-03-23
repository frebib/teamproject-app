package d2.teamproject.module;

import java.util.List;

/**
 * @author Joseph Groocock
 */
@FunctionalInterface
public interface ModulesLoaded {
    void onLoaded(List<BaseController> modules);
}
