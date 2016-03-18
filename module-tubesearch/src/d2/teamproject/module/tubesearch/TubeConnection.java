package d2.teamproject.module.tubesearch;

public class TubeConnection {
    private TubeStation from, to;
    private TubeLine line;
    private boolean bidir;

    public TubeConnection(TubeStation from, TubeStation to, TubeLine line) {
        if (from.equals(to))
            throw new IllegalArgumentException("Origin and Destination stations cannot be the same station");
        this.from = from;
        this.to = to;
        this.line = line;
        this.bidir = false;

        from.addSuccessor(to);

        to.addFrom(this);
        from.addTo(this);
        line.addStations(to, from);
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

    @Override
    public String toString() {
        return "TubeConnection{" +
                "from=" + from +
                ", to=" + to +
                ", line=" + line +
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
