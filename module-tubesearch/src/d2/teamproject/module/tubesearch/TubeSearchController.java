package d2.teamproject.module.tubesearch;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import d2.teamproject.PARTH;
import d2.teamproject.module.BaseView;
import d2.teamproject.module.JsonController;
import d2.teamproject.module.ModuleLoader;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import static d2.teamproject.PARTH.LOG;

public class TubeSearchController extends JsonController {
    private TubeSearchView view;

    private Map<String, TubeStation> stationMap;
    private Map<String, TubeLine> lineMap;
    private Set<TubeConnection> links;

    public TubeSearchController() {
        view = new TubeSearchView(this);
        stationMap = new LinkedHashMap<>();
        lineMap = new LinkedHashMap<>();
        links = new HashSet<>();

        // TODO: Implement graph search
    }

    @Override
    public void loadResources(Map<String, Object> res) throws ModuleLoader.LoadException {
        res.forEach((k, v) -> LOG.format(Level.FINE, " > Loaded resource \"%s\" = %s", k, v.toString()));

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
                    PARTH.LOG.format(Level.WARNING, "Tube connection created with invalid args:" +
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
        if (errors[0] > 0)
            PARTH.LOG.format(Level.WARNING, "There were %d errors loading in the tube map", errors[0]);
    }

    @Override
    public BaseView getView() {
        return view;
    }
}