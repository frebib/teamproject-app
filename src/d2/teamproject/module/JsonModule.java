package d2.teamproject.module;

import com.eclipsesource.json.JsonObject;
import javafx.scene.image.Image;

public abstract class JsonModule implements BaseModule {
    private BaseView view;
    private String name, description;
    private Image banner;

    public void init(JsonObject info, Image banner, BaseView view) {
        this.view = view;

        this.name = info.get("name").asString();
        this.description = info.get("desc").asString();
        this.banner = banner;
    }

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
}