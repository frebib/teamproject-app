package d2.teamproject.module.tubesearch;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import d2.teamproject.algorithm.search.*;
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
 * @author Joseph Groocock
 * @author Luke Taher
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
    private int mode;
    private JsonObject info;
    private JsonObject infomed;
    private JsonObject infobgn;

    public TubeSearchController() {
        mode = 0;
        view = new TubeSearchView(this);
        stationMap = new LinkedHashMap<>();
        lineMap = new LinkedHashMap<>();
        links = new HashSet<>();
    }

    @Override
    public void onOpen() {
        view.onOpen();
        view.updateText("choose-start");
    }

    @Override
    public void loadResources(Map<String, Object> res) throws ModuleLoader.LoadException {
        res.forEach((k, v) -> LOG.fine(" > Loaded resource \"%s\" = %s", k, v.toString()));
        // Load Tutorial JSONs
        tutorials = new LinkedHashMap<>();
        tutorials.put(BreadthFirstSearchStream.class.getName(), new Tutorial((JsonArray) res.get("bfsT")));
        tutorials.put(DepthFirstSearchStream.class.getName(), new Tutorial((JsonArray) res.get("dfsT")));
        tutorials.put(AStarSearchStream.class.getName(), new Tutorial((JsonArray) res.get("aStarT")));
        tutorials.put(DijkstraSearchStream.class.getName(), new Tutorial((JsonArray) res.get("dijkstraT")));

        infomed = (JsonObject) res.get("stationinfobgn");
        infobgn = (JsonObject) res.get("stationinfomed");
        info = (JsonObject) res.get("stationinfo");

        view.loadResources(res);

        reload();
    }

    public void reload(){
        // Load tube map JSONs
        JsonObject tubemapinfo;
        switch(mode)
        {
            case 1:
                tubemapinfo = infobgn;
                break;
            case 2:
                tubemapinfo = infomed;
                break;
            default:
                tubemapinfo = info;
                break;
        }
        JsonArray stationinfo = tubemapinfo.get("stations").asArray();
        JsonArray lineinfo = tubemapinfo.get("lines").asArray();

        lineMap.clear();
        stationMap.clear();
        links.clear();

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
//                        LOG.info("Connection %s already exists", connection);

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


        if (errors[0] > 0)
            LOG.warning("There were %d errors loading in the tube map", errors[0]);
    }

    public void onStationClick(TubeStation station) {
        switch (selection) {
            case NONE:
                break;
            case START:
                LOG.info("Selected start station %s", station);
                selection = StationSelect.GOAL;
                start = station;
                view.getMap().setStartStation(start);
                view.updateText("choose-goal", start.getName());
                break;
            case GOAL:
                if(station.equals(start))
                    break;
                LOG.info("Selected goal station %s", station);
                selection = StationSelect.NONE;
                goal = station;
                initSearch(searcher, start, goal);
                view.getMap().setGoalStation(goal);
                view.updateText("search-start", start.getName(), goal.getName());
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
        selection = StationSelect.START;
        start = null;
        goal = null;
        view.loadTutorial(searcher.getClazz().getName());
        view.updateText("choose-start");
    }

    public SearchState<Node<TubeStation>> nextState() {
        if(stream.hasNext())
            return stream.getNext();
        else
            return stream.getCurrent();
    }

    public SearchState<Node<TubeStation>> prevState() {
        if(stream.hasPrevious())
            return stream.getPrevious();
        else
            return stream.getCurrent();
    }

    public void setMode(int mode){
        this.mode = mode;
    }
}