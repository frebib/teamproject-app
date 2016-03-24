package d2.teamproject.module;

import com.eclipsesource.json.JsonObject;
import javafx.scene.image.Image;

import java.util.Map;

/**
 * Loads module {@link BaseController} from a Json object
 * @author Joseph Groocock
 */
public abstract class JsonController implements BaseController {
    private String name, description;
    private Image banner;

    /**
     * Called by the {@link ModuleLoader} when loading the module from file
     * @param info module information to load
     * @param banner module banner image
     */
    public void init(JsonObject info, Image banner) {
        this.name = info.get("name").asString();
        this.description = info.get("desc").asString();
        this.banner = banner;
    }

    /**
     * Called by the {@link ModuleLoader} with a list of
     * resources specified in the module.json metadata file
     * @param res a map of key -> {@link java.io.InputStreamReader} or specified type
     * @throws ModuleLoader.LoadException thrown if there is an error loading a resource
     */
    public abstract void loadResources(Map<String, Object> res) throws ModuleLoader.LoadException;

    /**
     * @return @inheritDoc
     */
    public String getName() {
        return name;
    }

    /**
     * @return @inheritDoc
     */
    public String getDesc() {
        return description;
    }

    /**
     * @return @inheritDoc
     */
    public Image getBanner() {
        return banner;
    }
}