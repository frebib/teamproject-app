package d2.teamproject.module.planets;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import d2.teamproject.algorithm.sorting.QuickSortStream;
import d2.teamproject.module.JsonController;
import d2.teamproject.module.ModuleLoader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PlanetController extends JsonController {

    public enum PlanetSort {
        DIST_TO_SUN,
        DIAMETER,
        MASS
    }

    private QuickSortStream<Planet> sort;
    private List<Planet> planets;

    private Comparator<Planet> planetCompare;

    public PlanetController() {
        sort = new QuickSortStream<>(planets, getPlanetCompare(PlanetSort.DIST_TO_SUN));

        // TODO: Implement planet sorting
    }

    @Override
    public void loadResources(Map<String, Object> res) throws ModuleLoader.LoadException {
        res.forEach((k, v) -> System.out.printf(" > Loaded resource \"%s\" = %s\n", k, v.toString()));

        JsonObject planetData = (JsonObject) res.get("planetsinfo");
        JsonArray planetArr = planetData.get("planets").asArray();
        planets = new ArrayList<Planet>(planetArr.size());

        for (JsonValue pData : planetArr)
            planets.add(Planet.loadFromJson(pData.asObject()));

        planets.stream().forEach(System.out::println);
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
        }
        return null; // Java, both you and I know execution will never reach here so shut the F#!$ up
    }
}