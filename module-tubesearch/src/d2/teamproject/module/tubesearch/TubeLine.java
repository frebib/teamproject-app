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
    private Color colour, stroke;

    public TubeLine(String id, String name, Color colour) {
        this(id, name, colour, null);
    }
    public TubeLine(String id, String name, Color colour, Color stroke) {
        this.id = id;
        this.name = name;
        this.colour = colour;
        this.stroke = stroke;

        stations = new LinkedHashSet<>();
    }

    public static TubeLine fromJson(JsonValue val) {
        JsonObject obj = val.asObject();
        JsonValue stroke = obj.get("stroke");
        return new TubeLine(
                obj.get("id").asString(),
                obj.get("name").asString(),
                Color.web(obj.get("colour").asString()),
                stroke != null ? Color.web(stroke.asString()) : null
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

    public Color getStroke() {
        return stroke;
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