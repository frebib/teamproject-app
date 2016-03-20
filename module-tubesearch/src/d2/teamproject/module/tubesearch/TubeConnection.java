package d2.teamproject.module.tubesearch;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class TubeConnection {
    private TubeStation from, to;
    private TubeLine line;
    private boolean bidir;
    private List<Point2D> curvePoints;

    public TubeConnection(TubeStation from, TubeStation to, TubeLine line) {
        if (from.equals(to))
            throw new IllegalArgumentException("Origin and Destination stations cannot be the same station");
        this.from = from;
        this.to = to;
        this.line = line;
        this.bidir = false;
        this.curvePoints = new ArrayList<>();

        to.addFrom(this);
        from.addTo(this);

        if (!line.getId().equals("walk")) {
            line.addStations(to, from);
            from.addSuccessor(to);
        }
    }

    public TubeStation getFrom() {
        return from;
    }

    public TubeStation getTo() {
        return to;
    }

    public TubeLine getLine() {
        return line;
    }

    public boolean isBidirectional() {
        return bidir;
    }

    public TubeConnection setBidirectional() {
        this.bidir = true;
        return this;
    }

    /**
     * Gets the coordinates of all points within the line
     * making up the connection between the two {@link TubeStation}s
     * @return a {@link List} of {@link Point2D}s representing the coordinates
     */
    public List<Point2D> getCurvePoints() {
        return curvePoints;
    }

    /**
     * Gets the coordinates of all points of the line including the stations
     * themselves making up the connection between the two {@link TubeStation}s
     * @return a {@link List} of {@link Point2D}s representing the coordinates
     */
    public List<Point2D> getLinePoints() {
        List<Point2D> allPoints = new ArrayList<>(curvePoints);
        allPoints.add(0, from.getPos());
        allPoints.add(to.getPos());
        return allPoints;
    }

    public void addCurvePoints(List<Point2D> curvePoints) {
        this.curvePoints.addAll(curvePoints);
    }

    @Override
    public String toString() {
        return "TubeConnection{" +
                "from=" + from +
                ", to=" + to +
                ", line=" + line +
                ", bidir=" + bidir +
                ", curvePoints=" + curvePoints +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TubeConnection conn = (TubeConnection) o;

        boolean same = this.from.equals(conn.from) && this.to.equals(conn.to);
        boolean swap = this.from.equals(conn.to) && this.to.equals(conn.from);

        return (same ^ swap) && this.line.equals(conn.line);
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (line != null ? line.hashCode() : 0);
        return result;
    }
}
