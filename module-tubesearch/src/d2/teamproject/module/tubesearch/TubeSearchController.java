package d2.teamproject.module.tubesearch;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import d2.teamproject.algorithm.search.AStarSearchStream;
import d2.teamproject.algorithm.search.SearchStream;
import d2.teamproject.algorithm.search.SearchStream.Searcher;
import d2.teamproject.module.BaseView;
import d2.teamproject.module.JsonController;
import d2.teamproject.module.ModuleLoader;
import d2.teamproject.tutorial.Tutorial;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.function.BiFunction;

import static d2.teamproject.PARTH.LOG;

/**
 * @author Parth Chandratreya
 */
public class TubeSearchController extends JsonController {
    private enum StationSelect {
        NONE,
        START,
        GOAL,
        INFO
    }

    private TubeSearchView view;

    private SearchStream<TubeStation> stream;

    private Map<String, TubeStation> stationMap;
    private Map<String, TubeLine> lineMap;
    private Set<TubeConnection> links;
    private Map<String, Tutorial> tutorials;

    private StationSelect selection;
    private Searcher<TubeStation> searcher;
    private TubeStation start, goal;

    public TubeSearchController() {
        view = new TubeSearchView(this);
        stationMap = new LinkedHashMap<>();
        lineMap = new LinkedHashMap<>();
        links = new HashSet<>();

        selection = StationSelect.START;
        stream = new AStarSearchStream<>(null, null);

        // TODO: Implement graph search
        view.getWindow().setOnKeyPressed(e -> {
            if (stream.hasNext())
                view.animateState(stream.getNext());
        });
    }

    @Override
    public void onOpen() {
        view.onOpen();
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
        stationinfo.forEach(s -> {
            JsonObject obj = s.asObject();
            String id = obj.get("id").asString();
            stationMap.put(id, TubeStation.fromJson(obj));
        });

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

                try {
                    TubeConnection connection = new TubeConnection(from, to, line);
                    Optional<TubeConnection> equals = links.stream().filter(connection::equals).findFirst();

                    if (!equals.isPresent())
                        links.add(connection);
                    else {
                        LOG.info("Connection %s already exists", connection);

                        TubeConnection otherConn = equals.get();
                        if (connection.getTo() == otherConn.getFrom() &&
                                connection.getFrom() == otherConn.getTo()) {
                            otherConn.setBidirectional();
                            connection = otherConn;
                        }
                    }

                    JsonArray curvesJson = obj1.get("curves").asArray();
                    if (curvesJson.size() > 0) {
                        List<Point2D> curvePoints = new ArrayList<>(curvesJson.size());
                        curvesJson.forEach(curveJson -> {
                            JsonObject curveObj = curveJson.asObject();
                            curvePoints.add(new Point2D(
                                    curveObj.getDouble("xpos", 0),
                                    curveObj.getDouble("ypos", 0)
                            ));
                        });
                        // make sure the curves are in the right order
                        // according to the stations in the TubeConnection
                        if (to.equals(connection.getFrom()))
                            Collections.reverse(curvePoints);

                        connection.addCurvePoints(curvePoints);
                    }

                } catch (IllegalArgumentException e) {
                    LOG.warning("%s == %s", to, from);
                    LOG.exception(e);
                }
            });
        });

        view.loadResources(res);
        if (errors[0] > 0)
            LOG.warning("There were %d errors loading in the tube map", errors[0]);
    }

    public void onStationClick(TubeStation station) {
        // TODO: Add checks to prevent choosing the same start/goal node etc.
        switch (selection) {
            case NONE:
                break;
            case START:
                LOG.info("Selected start station %s", station);
                selection = StationSelect.GOAL;
                start = station;
                break;
            case GOAL:
                LOG.info("Selected goal station %s", station);
                selection = StationSelect.NONE;
                goal = station;
                initSearch(searcher, start, goal);
                LOG.fine("Search stream initialised");
                break;
            case INFO:
                break;
        }
    }

    @Override
    public BaseView getView() {
        return view;
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

    private void initSearch(Searcher<TubeStation> searcher, TubeStation start, TubeStation goal) {
        stream = searcher.get(start, goal);

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
    public void setSearcher(Searcher<TubeStation> searcher) {
        LOG.info("Searching with %s", searcher);
        this.searcher = searcher;
    }
}