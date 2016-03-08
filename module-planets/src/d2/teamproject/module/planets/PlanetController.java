package d2.teamproject.module.planets;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import d2.teamproject.algorithm.sorting.QuickSortStream;
import d2.teamproject.algorithm.sorting.SortState;
import d2.teamproject.module.BaseView;
import d2.teamproject.module.JsonController;
import d2.teamproject.module.ModuleLoader;
import d2.teamproject.module.planets.gfx.PlanetSort;
import javafx.scene.image.Image;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static d2.teamproject.PARTH.LOG;

public class PlanetController extends JsonController {


    private PlanetView view;

    // TODO: Allow for all types of sort, not just QS
    private QuickSortStream<Planet> sort;
    private List<Planet> planets;

    private Comparator<Planet> planetCompare;

    public PlanetController() {
        view = new PlanetView(this);
        view.getWindow().setOnKeyPressed(e -> {
            if (view.getAnimationState() == PlanetView.AnimState.NOTHING)
                view.updateState(sort.getNext());
        });
    }

    @Override
    public void onOpen() {
        sort = new QuickSortStream<>(planets, PlanetSort.DIAMETER);
        sort.initialise();

        view.onOpen();
    }

    public void handleNextState(Consumer<SortState<Planet>> fn) {
        fn.accept(sort.getNext());
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public QuickSortStream<Planet> getSorter() {
        return sort;
    }

    @Override
    public BaseView getView() {
        return view;
    }

    @Override
    public void loadResources(Map<String, Object> res) throws ModuleLoader.LoadException {
        res.forEach((k, v) -> LOG.fine(" > Loaded resource \"%s\" = %s", k, v.toString()));

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
}