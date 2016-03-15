package d2.teamproject.module.tubesearch;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import d2.teamproject.algorithm.search.AStarSearchStream;
import d2.teamproject.algorithm.search.Node;
import d2.teamproject.algorithm.search.SearchState;
import d2.teamproject.algorithm.search.SearchStream;
import d2.teamproject.module.BaseView;
import d2.teamproject.module.JsonController;
import d2.teamproject.module.ModuleLoader;
import d2.teamproject.tutorial.Tutorial;

import javax.swing.event.DocumentEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import static d2.teamproject.PARTH.LOG;

/**
 * @author Parth Chandratreya
 */

public class TubeSearchController extends JsonController {
    private TubeSearchView view;

    private SearchStream<TubeStation> stream;

    private Map<String, TubeStation> stationMap;
    private Map<String, TubeLine> lineMap;
    private Set<TubeConnection> links;
    private Map<String, Tutorial> tutorials;

    public TubeSearchController() {
        view = new TubeSearchView(this);
        stationMap = new LinkedHashMap<>();
        lineMap = new LinkedHashMap<>();
        links = new HashSet<>();



        // TODO: Implement graph search
        view.getWindow().setOnKeyPressed(e -> {
            if(stream.hasNext())
                view.animateState(stream.getNext());
        });
    }

    @Override
    public void onOpen() {
        view.onOpen();
    }

    @Override
    public BaseView getView() {
        return view;
    }

    @Override
    public void loadResources(Map<String, Object> res) throws ModuleLoader.LoadException {
        res.forEach((k, v) -> LOG.fine(" > Loaded resource \"%s\" = %s", k, v.toString()));

        // Load Tutorial JSONs
        tutorials = new LinkedHashMap<>();
        //TODO: Rename file or have multiple arrays
        Tutorial searchT = new Tutorial((JsonArray) res.get("help"));
        tutorials.put("search", searchT);

        // Load tube map JSONs
        JsonObject tubemapinfo = (JsonObject) res.get("stationinfo");
        JsonArray stationinfo = tubemapinfo.get("stations").asArray();
        JsonArray lineinfo = tubemapinfo.get("lines").asArray();

        // Create all lines
        lineinfo.forEach(l -> lineMap.put(l.asObject().get("id").asString(), TubeLine.fromJson(l)));
        // Create all station objects
        for (int i = 0; i < stationinfo.size(); i++) {
            JsonObject obj = stationinfo.get(i).asObject();
            String id = obj.get("id").asString();
            stationMap.put(id, TubeStation.fromJson(obj, i));
        }

        // Use array to get around 'should be final or effectively final' issue
        final Integer[] errors = {0};
        stationinfo.forEach(s -> {
            JsonObject obj = s.asObject();
            String fromStation = obj.get("id").asString();
            JsonArray connections = obj.get("connected").asArray();
            // Create each connection object, linking everything together
            connections.forEach(conn -> {
                JsonObject obj1 = conn.asObject();
                String toStation = obj1.get("id").asString();
                String lineId = obj1.get("lineId").asString();

                // Check that all stations and the line exists, complain otherwise
                TubeStation from = stationMap.get(fromStation);
                TubeStation to = stationMap.get(toStation);
                TubeLine line = lineMap.get(lineId);
                if (from == null || to == null || line == null) {
                    errors[0]++;
                    LOG.warning("Tube connection created with invalid args:" +
                                    " \n\tfrom: \"%s\", %s\n\tto: \"%s\", %s\n\tline: \"%s\", %s",
                            fromStation, from,
                            toStation, to,
                            lineId, line
                    );
                }
                // TODO: Load sublines here

                links.add(new TubeConnection(from, to, line));
            });
        });

        view.loadResources(res);
        if (errors[0] > 0)
            LOG.warning("There were %d errors loading in the tube map", errors[0]);

        stream = new AStarSearchStream<>(stationMap.get("bayswater"), stationMap.get("temple"));

        // Euclidean distance between 2 nodes
        BiFunction<TubeStation, TubeStation, Double> euclidean = (a, b) -> Math.sqrt(
                Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));

        // Euclidean distance between 2 adjacent nodes, plus total cost of previous node
        BiFunction<TubeStation, TubeStation, Double> costFn = (a, b) ->
                stream.getCost(a) + euclidean.apply(a, b);

        stream.setCostFn(costFn)
                .setHeuristicFn(euclidean)
                .initialise();
    }

    public Tutorial getTutorial(String key) {
        return tutorials.get(key);
    }

    public Map<String, TubeStation> getStationMap() {
        return stationMap;
    }

    public Map<String, TubeLine> getLineMap() {
        return lineMap;
    }

    public Set<TubeConnection> getLinks() {
        return links;
    }
}