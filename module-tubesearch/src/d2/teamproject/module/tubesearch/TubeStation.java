package d2.teamproject.module.tubesearch;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import d2.teamproject.algorithm.search.Node;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

import static d2.teamproject.PARTH.LOG;

/**
 * @author Luke Taher
 */

public class TubeStation extends Node<TubeStation> {
    private ArrayList<TubeConnection> from, to;
    private String name, id;
    private Point2D pos;

    public TubeStation(String id, String name, double x, double y) {
        super(null);
        contents = this;

        if (x == 0 && y == 0)
            LOG.warning("Potentially invalid coordinates of pos={x=%d, y=%d} in TubeStation name=%s", x, y, name);

        this.id = id;
        this.name = name;
        this.pos = new Point2D(x, y);

        from = new ArrayList<>();
        to = new ArrayList<>();
    }

    public Point2D getPos() {
        return pos;
    }

    @Deprecated
    public double getX() {
        return pos.getX();
    }

    @Deprecated
    public double getY() {
        return pos.getY();
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
                ", pos=" + pos +
                '}';
    }

    public static TubeStation fromJson(JsonValue val) {
        JsonObject obj = val.asObject();
        return new TubeStation(
                obj.get("id").asString(),
                obj.get("name").asString(),
                obj.getDouble("xpos", 0),
                obj.getDouble("ypos", 0)
        );
    }
}