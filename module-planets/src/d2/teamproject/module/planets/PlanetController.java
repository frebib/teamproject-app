package d2.teamproject.module.planets;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import d2.teamproject.algorithm.sorting.QuickSortStream;
import d2.teamproject.module.JsonController;
import d2.teamproject.module.ModuleLoader;
import javafx.scene.image.Image;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PlanetController extends JsonController {

    public enum PlanetSort {
        DIST_TO_SUN,
        DIAMETER,
        MASS,
        ROTATE_TIME
    }

    // TODO: Allow for all types of sort, not just QS
    private QuickSortStream<Planet> sort;
    private List<Planet> planets;

    private Comparator<Planet> planetCompare;

    public PlanetController() {
        sort = new QuickSortStream<>(planets, getPlanetCompare(PlanetSort.DIST_TO_SUN));

        // TODO: Implement planet sorting
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public QuickSortStream<Planet> getSorter() {
        return sort;
    }

    public Comparator<Planet> getPlanetCompare(PlanetSort sortBy) {
        // Is ugly. Needs to be cleaned up
        switch (sortBy) {
            case DIST_TO_SUN:
                return (p1, p2) -> Float.compare(p1.getDistToSun(), p2.getDistToSun());
            case DIAMETER:
                return (p1, p2) -> Float.compare(p1.getDiameter(), p2.getDistToSun());
            case MASS:
                return (p1, p2) -> Float.compare(p1.getMass(), p2.getMass());
            case ROTATE_TIME:
                return (p1, p2) -> Float.compare(p1.getRotationTime(), p2.getRotationTime());
        }
        return null; // Java, both you and I know execution will never reach here so shut the F#!$ up
    }

    @Override
    public void loadResources(Map<String, Object> res) throws ModuleLoader.LoadException {
        res.forEach((k, v) -> System.out.printf(" > Loaded resource \"%s\" = %s\n", k, v.toString()));

        // Load planet JSON
        JsonObject planetData = (JsonObject) res.get("planetinfo");
        JsonArray planetArr = planetData.get("planets").asArray();
        planets = new ArrayList<Planet>(planetArr.size());

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
    }
}