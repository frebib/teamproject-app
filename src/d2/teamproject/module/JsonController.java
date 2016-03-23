package d2.teamproject.module;

import com.eclipsesource.json.JsonObject;
import javafx.scene.image.Image;

import java.util.Map;

/**
 * @author Joseph Groocock
 */
public abstract class JsonController implements BaseController {
    private String name, description;
    private Image banner;

    public void init(JsonObject info, Image banner) {
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

    // TODO: Add custom CSS loading
    public boolean hasStyles() {
        return false;
    }

    String getStyles() {
        return "";
    }
}