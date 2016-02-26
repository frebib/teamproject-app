package d2.teamproject.module.tubesearch;

import d2.teamproject.algorithm.search.Node;

public class TubeConnection extends Node<TubeConnection> {
    public enum Direction {
        Single,
        Both
    }

    private TubeStation from, to;
    private TubeLine line;
    private Direction dir;

    public TubeConnection(TubeStation from, TubeStation to, TubeLine line) {
        this(from, to, line, Direction.Single);
    }

    public TubeConnection(TubeStation from, TubeStation to, TubeLine line, Direction dir) {
        super(null);
        contents = this;
        this.from = from;
        this.to = to;
        this.line = line;
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "TubeConnection{" +
                "from=" + from +
                ", to=" + to +
                ", line=" + line +
                ", dir=" + dir +
                '}';
    }
}
