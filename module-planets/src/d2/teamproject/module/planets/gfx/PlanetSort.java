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
            DIST_TO_SUN = new PlanetSort<>(Planet::getDistToSun),
            ROTATE_TIME = new PlanetSort<>(Planet::getRotationTime),
            DIAMETER = new PlanetSort<>(Planet::getDiameter),
            MASS = new PlanetSort<>(Planet::getMass);

    private Function<Planet, T> fn;
    private Dir dir;

    private PlanetSort(Function<Planet, T> fn) {
        this(fn, Dir.ASCENDING);
    }
    private PlanetSort(Function<Planet, T> fn, Dir dir) {
        this.fn = fn;
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
        return new PlanetSort<>(this.fn, Dir.ASCENDING);
    }
    public PlanetSort<T> setDescending() {
        return new PlanetSort<>(this.fn, Dir.DESCENDING);
    }
}

