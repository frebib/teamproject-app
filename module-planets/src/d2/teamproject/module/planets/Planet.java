package d2.teamproject.module.planets;

import com.eclipsesource.json.JsonObject;
import javafx.scene.image.Image;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Planet implements Comparable<Planet> {
    private String name;
    private float mass, diameter, distToSun, tilt;
    private Comparator<Planet> cmp;
    private Map<String, Image> textures;

    private Planet(String name, float mass, float diameter, float distToSun, float tilt) {
        this.name = name;
        this.mass = mass;
        this.diameter = diameter;
//        this.diameter = 70;
        this.distToSun = distToSun;
        this.tilt = tilt;

        textures = new LinkedHashMap<>();
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

    public float getTilt() {
        return tilt;
    }

    public static Planet loadFromJson(JsonObject obj) {
        return new Planet(
                obj.get("name").asString(),
                obj.get("mass").asFloat(),
                obj.get("diam").asFloat(),
                obj.get("dist").asFloat(),
                obj.get("tilt").asFloat()
        );
    }

    public Map<String,Image> getTextures() {
        return textures;
    }

    public void setTextures(Map<String, Image> allTextures) {
        textures = allTextures.keySet()
                .stream() // Java 8 Streams are supposed to be pretty...
                .filter(s -> s.matches(String.format("^%s(-[\\w]+)*\\.(\\w+)$", name.toLowerCase())))
                .collect(Collectors.toMap(s -> s, allTextures::get));
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
