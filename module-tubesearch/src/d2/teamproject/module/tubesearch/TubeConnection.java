package d2.teamproject.module.tubesearch;

public class TubeConnection {
    private TubeStation from, to;
    private TubeLine line;

    public TubeConnection(TubeStation from, TubeStation to, TubeLine line) {
        this.from = from;
        this.to = to;
        this.line = line;

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

    @Override
    public String toString() {
        return "TubeConnection{" +
                "from=" + from +
                ", to=" + to +
                ", line=" + line +
                '}';
    }
}
