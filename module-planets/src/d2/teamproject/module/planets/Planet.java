package d2.teamproject.module.planets;

import com.eclipsesource.json.JsonObject;

import java.util.Comparator;

public class Planet implements Comparable<Planet> {
    private String name;
    private float mass, diameter, distToSun;
    private Comparator<Planet> cmp;

    private Planet(String name, float mass, float diameter, float distToSun) {
        this.name = name;
        this.mass = mass;
        this.diameter = diameter;
        this.distToSun = distToSun;
    }

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
        return new Planet(
                obj.get("name").asString(),
                obj.get("mass").asFloat(),
                obj.get("diam").asFloat(),
                obj.get("dist").asFloat()
        );
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
