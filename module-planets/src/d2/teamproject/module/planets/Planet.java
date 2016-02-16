package d2.teamproject.module.planets;

import com.eclipsesource.json.JsonObject;

import java.util.Comparator;

public class Planet implements Comparable<Planet> {
    private String name;
    private float mass, diameter, distToSun;
    private Comparator<Planet> cmp;

    @Override
    public int compareTo(Planet planet) {
        return cmp.compare(this, planet);
    }

    public String getName() {
        return name;
    }

    public float getMass() {
        return mass;
    }

    public float getDiameter() {
        return diameter;
    }

    public float getDistToSun() {
        return distToSun;
    }

    public static Planet loadFromJson(JsonObject obj) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String toString() {
        return "Planet{" +
                "name='" + name + '\'' +
                ", mass=" + mass +
                ", diameter=" + diameter +
                ", distToSun=" + distToSun +
                '}';
    }
}
