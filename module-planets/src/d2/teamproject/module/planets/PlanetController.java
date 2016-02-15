package d2.teamproject.module.planets;

import d2.teamproject.module.JsonController;
import d2.teamproject.algorithm.sorting.QuickSortStream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlanetController extends JsonController {
    private QuickSortStream<Planet> sort;
    private List<Planet> planets;

    private Comparator<Planet> planetCompare;

    public PlanetController() {
        // TODO: Load planets
        planets = new ArrayList<Planet>(8);
        sort = new QuickSortStream<>(planets);

        // TODO: Implement planet sorting
    }
}