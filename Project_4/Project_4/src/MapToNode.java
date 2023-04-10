import java.util.function.Function;

public class MapToNode implements Function<Point, Node> {

    private Point start;
    private Point end;
    private Node curr;

    public MapToNode(Point start, Point end, Node curr)
    {
        this.end = end;
        this.curr = curr;
        this.start = start;
    }

    public Node apply(Point p)
    {
        if(Math.abs(start.x - p.x) == 1 && Math.abs(start.y - p.y) == 1)
        {
            return new Node(p, (Math.abs(p.x - end.x) + Math.abs(p.y - end.y)), 1.4 + curr.getTraveled(), curr);
        }
        return new Node(p, (Math.abs(p.x - end.x) + Math.abs(p.y - end.y)), 1 + curr.getTraveled(), curr);
    }

}
