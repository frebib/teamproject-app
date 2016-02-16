package d2.teamproject.module.planets;

import d2.teamproject.algorithm.sorting.QuickSortStream;
import d2.teamproject.module.JsonController;
import d2.teamproject.module.ModuleLoader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PlanetController extends JsonController {
    private QuickSortStream<Planet> sort;
    private List<Planet> planets;

    private Comparator<Planet> planetCompare;

    public PlanetController() {
        // TODO: Load planets
        planets = new ArrayList<Planet>(8);
        sort = new QuickSortStream<>(planets, (o1, o2) -> Float.compare(o1.getDistToSun(), o2.getDistToSun()));

        // TODO: Implement planet sorting
    }

    @Override
    public void loadResources(Map<String, Object> res) throws ModuleLoader.LoadException {
        res.forEach((k, v) -> System.out.printf(" > Loaded resource \"%s\" = %s\n", k, v.toString()));
        // TODO: Load resources for Planets
        JsonObject planetData = (JsonObject) res.get("planetsinfo");
        JsonArray planetArr = planetData.get("planets").asArray();
        planets = new ArrayList<Planet>(planetArr.size());

        for (JsonValue pData : planetArr)
            planets.add(Planet.loadFromJson(pData.asObject()));

        planets.stream().forEach(System.out::println);
    }
}