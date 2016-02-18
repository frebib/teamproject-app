package d2.teamproject.module.tubesearch;

import d2.teamproject.module.BaseView;
import d2.teamproject.module.JsonController;
import d2.teamproject.module.ModuleLoader;

import java.util.Map;

public class TubeSearchController extends JsonController {
    private TubeSearchView view;

    public TubeSearchController() {
        view = new TubeSearchView(this);
        // TODO: Implement planet sorting
    }

    @Override
    public void loadResources(Map<String, Object> res) throws ModuleLoader.LoadException {
        // No resources to load yet
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onClose() {
    }

    @Override
    public BaseView getView() {
        return view;
    }
}