package d2.teamproject.module.tubesearch;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import d2.teamproject.algorithm.search.Node;

import java.util.ArrayList;
import java.util.List;

public class TubeStation extends Node<TubeStation> {
    private ArrayList<TubeConnection> from, to;
    private String name, id;
    private double x, y;
    private int index;

    public TubeStation(String name, String id, double x, double y, int index) {
        contents = this;

        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
        this.index = index;

        from = new ArrayList<>();
        to = new ArrayList<>();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<TubeConnection> getTo() {
        return to;
    }

    public List<TubeConnection> getFrom() {
        return from;
    }

    public void addFrom(TubeConnection conn) {
        if (!from.contains(conn))
            from.add(conn);
    }

    public void addTo(TubeConnection conn) {
        if (!to.contains(conn))
            to.add(conn);
    }

    @Override
    public String toString() {
        return "TubeStation{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public static TubeStation fromJson(JsonValue val, int index) {
        JsonObject obj = val.asObject();
        return new TubeStation(
                obj.get("id").asString(),
                obj.get("name").asString(),
                obj.getDouble("xpos", 0),
                obj.getDouble("ypos", 0),
                index
        );
    }

    public int getIndex(){
        return index;
    }
}