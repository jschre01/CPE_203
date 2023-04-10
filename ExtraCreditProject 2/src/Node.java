public class Node {
    private Point pos;
    private int heuristic;
    private double traveled;
    private Node prior;
    private double sum;

    public Node(Point p, int h, double t, Node n)
    {
        pos = p;
        heuristic = h;
        traveled = t;
        prior = n;
        sum = h + t;
    }

    public Point getPos(){return pos;}

    public double getTraveled(){return traveled;}

    public Node getPrior(){return prior;}

    public double getSum(){return sum;}
}
