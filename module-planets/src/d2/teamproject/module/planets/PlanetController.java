package d2.teamproject.module.planets;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import d2.teamproject.algorithm.sorting.QuickSortStream;
import d2.teamproject.module.BaseView;
import d2.teamproject.module.JsonController;
import d2.teamproject.module.ModuleLoader;
import javafx.scene.image.Image;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PlanetController extends JsonController {

    public static class PlanetSort<T extends Comparable<T>> implements Comparator<Planet> {
        public static final PlanetSort<Float>
        DIST_TO_SUN = new PlanetSort<>(Planet::getDistToSun),
        ROTATE_TIME = new PlanetSort<>(Planet::getRotationTime),
        DIAMETER = new PlanetSort<>(Planet::getDiameter),
        MASS = new PlanetSort<>(Planet::getMass);

        private Function<Planet, T> fn;

        private PlanetSort(Function<Planet, T> fn) {
            this.fn = fn;
        }

        @Override
        public int compare(Planet p1, Planet p2) {
            return fn.apply(p1).compareTo(fn.apply(p2));
        }
    }

    private PlanetView view;

    // TODO: Allow for all types of sort, not just QS
    private QuickSortStream<Planet> sort;
    private List<Planet> planets;

    private Comparator<Planet> planetCompare;

    public PlanetController() {
        view = new PlanetView(this);
        view.getWindow().setOnKeyPressed(e -> {
            System.out.println(e.getCode());
//            System.out.println(sort.getNext());
            view.updateState(sort.getNext());
        });
    }

    @Override
    public void onOpen() {
        // TODO: Implement planet sorting
        sort = new QuickSortStream<>(planets, PlanetSort.DIAMETER);
        sort.initialise();
        System.out.println(sort.getNext());
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
        res.forEach((k, v) -> System.out.printf(" > Loaded resource \"%s\" = %s\n", k, v.toString()));

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
                int size = (int) entry.getSize();
                Image img = new Image(new BufferedInputStream(zis, size));
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