package d2.teamproject.module.tubesearch;

public class TubeStation {
    private String name, id;

    public TubeStation(String name, String id) {
        this.name = name;
        this.id = id;
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