package d2.teamproject.module.planets;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import d2.teamproject.algorithm.sorting.BubbleSortStream;
import d2.teamproject.algorithm.sorting.QuickSortStream;
import d2.teamproject.algorithm.sorting.SortStream;
import d2.teamproject.module.BaseView;
import d2.teamproject.module.JsonController;
import d2.teamproject.module.ModuleLoader;
import d2.teamproject.module.planets.PlanetView.AnimState;
import d2.teamproject.tutorial.Tutorial;
import javafx.scene.image.Image;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static d2.teamproject.PARTH.LOG;

/**
 * @author Parth Chandratreya
 */
public class PlanetController extends JsonController {
    private final PlanetView view;

    private SortStream<Planet> sort;
    private List<Planet> planets;
    private Map<String, Tutorial> tutorials;

    public PlanetController() {
        view = new PlanetView(this);
    }

    @Override
    public void onOpen() {
        view.onOpen();
    }
    public void prevState() {
        view.disableNavButtons(true);
        view.stepBackward();
    }
    public void nextState() {
        AnimState s = view.getAnimationState();
        if (s != AnimState.NOTHING && s != AnimState.COMPARED &&
                s != AnimState.ZOOMED && s != AnimState.ZOOMING) {
            LOG.finer("Already animating...");
            return;
        }
        view.disableNavButtons(true);
        view.stepForward();
    }

    @Override
    public void loadResources(Map<String, Object> res) throws ModuleLoader.LoadException {
        res.forEach((k, v) -> LOG.fine(" > Loaded resource \"%s\" = %s", k, v.toString()));

        // Load Tutorial JSONs
        tutorials = new LinkedHashMap<>();
        tutorials.put(BubbleSortStream.class.getName(), new Tutorial((JsonArray) res.get("bubblesortT")));
        tutorials.put(QuickSortStream.class.getName(), new Tutorial((JsonArray) res.get("quicksortT")));

        // Load planet JSON
        JsonObject planetData = (JsonObject) res.get("planetinfo");
        JsonArray planetArr = planetData.get("planets").asArray();
        planets = new ArrayList<>(planetArr.size());

        for (JsonValue pData : planetArr)
            planets.add(Planet.loadFromJson(pData.asObject()));

        // Load planet textures
        ZipInputStream zis = (ZipInputStream) res.get("planettexture");
        ZipEntry entry;
        try {
            Map<String, Image> textures = new LinkedHashMap<>();
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                Image img = new Image(new BufferedInputStream(zis, (int) entry.getSize()));
                textures.put(name, img);
            }
            zis.close();

            // Apply textures to Planet objects
            planets.stream().forEach(p -> p.setTextures(textures));

        } catch (IOException e) {
            throw new ModuleLoader.LoadException(e);
        }

        // Load GUI resources
        view.loadResources(res);
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public Tutorial getTutorial(String key) {
        return tutorials.get(key);
    }

    public SortStream<Planet> getSorter() {
        return sort;
    }

    @Override
    public BaseView getView() {
        return view;
    }

    public void setSorter(SortStream<Planet> sort) {
        this.sort = sort;
    }
}