import java.util.function.Function;

public class MapToNode implements Function<Point, Node> {

    private Point end;
    private Node curr;
    public MapToNode(Point end, Node curr)
    {
        this.end = end;
        this.curr = curr;
    }

    public Node apply(Point p)
    {
        return new Node(p, (Math.abs(p.x - end.x) + Math.abs(p.y - end.y)), 1 + curr.getTraveled(), curr);
    }

}
