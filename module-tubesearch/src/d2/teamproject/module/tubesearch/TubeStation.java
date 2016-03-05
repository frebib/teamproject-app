package d2.teamproject.module.tubesearch;

import d2.teamproject.algorithm.search.Node;

import java.util.ArrayList;
import java.util.List;

public class TubeStation extends Node<TubeStation> {
    private ArrayList<TubeConnection> from, to;
    private String name, id;
    private double x, y;

    public TubeStation(String name, String id, double x, double y) {
        super(null);
        contents = this;

        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;

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
                '}';
    }
}