package d2.teamproject.module.tubesearch;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Joseph Groocock
 * @author Luke Taher
 */
public class TubeLine {
    private Set<TubeStation> stations;
    private String id, name;
    private Color colour, inner;

    public TubeLine(String id, String name, Color colour) {
        this(id, name, colour, null);
    }
    public TubeLine(String id, String name, Color colour, Color inner) {
        this.id = id;
        this.name = name;
        this.colour = colour;
        this.inner = inner;

        stations = new LinkedHashSet<>();
    }

    public static TubeLine fromJson(JsonValue val) {
        JsonObject obj = val.asObject();
        JsonValue inner = obj.get("inner");
        return new TubeLine(
                obj.get("id").asString(),
                obj.get("name").asString(),
                Color.web(obj.get("colour").asString()),
                inner != null ? Color.web(inner.asString()) : null
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

    public Color getInnerColour() {
        return inner;
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
                ", inner=" + String.valueOf(inner) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TubeLine tubeLine = (TubeLine) o;

        return stations.equals(tubeLine.stations) && id.equals(tubeLine.id) &&
                (name != null ? name.equals(tubeLine.name) : tubeLine.name == null);
    }
    @Override
    public int hashCode() {
        int result = stations.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}