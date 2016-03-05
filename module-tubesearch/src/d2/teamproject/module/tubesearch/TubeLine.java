package d2.teamproject.module.tubesearch;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class TubeLine {
    private Set<TubeStation> stations;
    private String id, name;
    private Color colour;

    public TubeLine(String id, String name, Color colour) {
        this.id = id;
        this.name = name;
        this.colour = colour;

        stations = new LinkedHashSet<>();
    }

    public static TubeLine fromJson(JsonValue val) {
        JsonObject obj = val.asObject();
        return new TubeLine(
                obj.get("id").asString(),
                obj.get("name").asString(),
                Color.web(obj.get("colour").asString())
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColour() {
        return colour;
    }

    public Set<TubeStation> getStations() {
        return stations;
    }

    public void addStations(TubeStation... stns) {
        Collections.addAll(stations, stns);
    }

    @Override
    public String toString() {
        return "TubeLine{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", colour=" + colour +
                '}';
    }
}