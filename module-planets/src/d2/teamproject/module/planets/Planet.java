package d2.teamproject.module.planets;

import com.eclipsesource.json.JsonObject;
import javafx.scene.image.Image;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Planet {
    private String name;
    private float mass, diameter, distToSun, time, tilt;
    private Map<String, Image> textures;

    private Planet(String name, float mass, float diameter, float distToSun, float time, float tilt) {
        this.name = name;
        this.mass = mass;
        this.diameter = diameter;
        this.distToSun = distToSun;
        this.time = time;
        this.tilt = tilt;

        textures = new LinkedHashMap<>();
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

    /**
     * Gets the amount of earth days it takes for the planet to complete 1 rotation
     * @return the rotation time in earth-days
     */
    public float getRotationTime() {
        return time;
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
                obj.get("time").asFloat(),
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
                ", time=" + time +
                ", tilt=" + tilt +
                ", textures=" + textures +
                '}';
    }
}
