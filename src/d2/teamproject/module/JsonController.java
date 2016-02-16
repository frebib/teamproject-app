package d2.teamproject.module;

import com.eclipsesource.json.JsonObject;
import javafx.scene.image.Image;

import java.util.Map;

public abstract class JsonController implements BaseController {
    private BaseView view;
    private String name, description;
    private Image banner;

    public void init(JsonObject info, Image banner, BaseView view) {
        this.view = view;

        this.name = info.get("name").asString();
        this.description = info.get("desc").asString();
        this.banner = banner;
    }

    public abstract void loadResources(Map<String, Object> res) throws ModuleLoader.LoadException;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return description;
    }

    public Image getBanner() {
        return banner;
    }

    @Override
    public BaseView getView() {
        return view;
    }

    @Override
    public void setView(BaseView view) {
        this.view = view;
    }
}