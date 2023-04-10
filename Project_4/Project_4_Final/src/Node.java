
public class Node {
    private Point pos;
    private int heuristic;
    private int traveled;
    private Node prior;
    private int sum;

    public Node(Point p, int h, int t, Node n)
    {
        pos = p;
        heuristic = h;
        traveled = t;
        prior = n;
        sum = h + t;
    }

    public Point getPos(){return pos;}

    public int getHeuristic(){return heuristic;}

    public int getTraveled(){return traveled;}

    public Node getPrior(){return prior;}

    public int getSum(){return sum;}
}
