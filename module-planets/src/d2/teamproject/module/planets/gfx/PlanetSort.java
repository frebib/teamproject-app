package d2.teamproject.module.planets.gfx;

import d2.teamproject.module.planets.Planet;

import java.util.Comparator;
import java.util.function.Function;

public class PlanetSort<T extends Comparable<T>> implements Comparator<Planet> {
    private enum Dir {
        ASCENDING(1),
        DESCENDING(-1);

        int polarity;
        Dir(int i) {
            polarity = i;
        }

        @Override
        public String toString() {
            return this.name().charAt(0) +
                    this.name().substring(1).toLowerCase();
        }
    }

    public static final PlanetSort<Float>
            DIST_TO_SUN = new PlanetSort<>(Planet::getDistToSun, "Distance to Sun"),
            ROTATE_TIME = new PlanetSort<>(Planet::getRotationTime, "Rotation Time"),
            DIAMETER = new PlanetSort<>(Planet::getDiameter, "Diameter"),
            MASS = new PlanetSort<>(Planet::getMass, "Mass");

    private Function<Planet, T> fn;
    private String description;
    private Dir dir;

    private PlanetSort(Function<Planet, T> fn, String description) {
        this(fn, description, Dir.ASCENDING);
    }
    private PlanetSort(Function<Planet, T> fn, String description, Dir dir) {
        this.fn = fn;
        this.description = description;
        this.dir = dir;
    }

    @Override
    public int compare(Planet p1, Planet p2) {
        return fn.apply(p1).compareTo(fn.apply(p2)) * dir.polarity;
    }
    public String getDirection() {
        return dir.toString();
    }
    public PlanetSort<T> setAscending() {
        return new PlanetSort<>(this.fn, this.description, Dir.ASCENDING);
    }
    public PlanetSort<T> setDescending() {
        return new PlanetSort<>(this.fn, this.description, Dir.DESCENDING);
    }
    public String getDescription() {
        return description;
    }
    @Override
    public String toString() {
        return description;
    }
}