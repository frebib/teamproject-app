package d2.teamproject.module;

import java.util.List;

@FunctionalInterface
public interface ModulesLoaded {
    void onLoaded(List<BaseController> modules);
}
