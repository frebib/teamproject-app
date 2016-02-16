package d2.teamproject.module.planets;

import com.eclipsesource.json.JsonObject;

import java.util.Comparator;

public class Planet implements Comparable<Planet> {
    private String name;
    private long mass, diameter, distToSun;
    private Comparator<Planet> cmp;

    @Override
    public int compareTo(Planet planet) {
        return cmp.compare(this, planet);
    }

    public String getName() {
        return name;
    }

    public long getMass() {
        return mass;
    }

    public long getDiameter() {
        return diameter;
    }

    public long getDistToSun() {
        return distToSun;
    }

    public Planet loadFromFile(JsonObject obj) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
