package d2.teamproject.module.tubesearch;

public class TubeStation {
    private String name, id;
    private double x, y;

    public TubeStation(String name, String id, double x, double y) {
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
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

    @Override
    public String toString() {
        return "TubeStation{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}