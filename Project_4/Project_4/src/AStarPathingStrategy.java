import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        Node curr = new Node(start, (Math.abs(start.x - end.x) + Math.abs(start.y - end.y)), 0, null);
        List<Point> path = new LinkedList<>();
        HashMap<Point, Node> closed = new HashMap<>();
        Stream<Node> s = potentialNeighbors.apply(start).filter(canPassThrough).map(new MapToNode(start, end, curr));
        List<Node> temp =  s.collect(Collectors.toList());
        Node next = curr;
        if(temp.size() != 0) {
            next = temp.get(0);
        }
        for(int i = 0; i < temp.size(); i++)
        {
            if(withinReach.test(temp.get(i).getPos(), end))
            {
                Node traceback = temp.get(i);
                while(traceback != null)
                {
                    path.add(traceback.getPos());
                    traceback = traceback.getPrior();
                }
            }
            if(temp.get(i).getSum() < next.getSum())
            {

                next = temp.get(i);
            }
        }
        closed.put(curr.getPos(), curr);
        temp.remove(curr);
        curr = next;
        while(temp.size() != 0)
        {
            HashMap<Point, Node> open = new HashMap<>();
            for(int j = 0; j < temp.size(); j++)
            {
                open.put(temp.get(j).getPos(), temp.get(j));
            }
            Stream<Node> t = potentialNeighbors.apply(curr.getPos()).filter(canPassThrough).map(new MapToNode(curr.getPos(), end, curr));
            List <Node> interns = t.collect(Collectors.toList());
            for(int i = 0; i < interns.size(); i++)
            {
                if(open.containsKey(interns.get(i).getPos()))
                {
                    if(interns.get(i).getSum() < open.get(interns.get(i).getPos()).getSum())
                    {
                        temp.remove(open.get(interns.get(i).getPos()));
                        open.remove(interns.get(i).getPos());
                        temp.add(interns.get(i));
                        open.put(interns.get(i).getPos(), interns.get(i));
                    }
                }
                else if(!closed.containsKey(interns.get(i).getPos()))
                {
                    temp.add(interns.get(i));
                }
            }
            next = temp.get(0);
            temp.remove(curr);
            for(int i = 0; i < temp.size(); i++)
            {
                if(withinReach.test(temp.get(i).getPos(), end))
                {
                    Node traceback = temp.get(i);
                    while(traceback != null)
                    {
                        path.add(0, traceback.getPos());
                        traceback = traceback.getPrior();
                    }
                    return path;
                }
                if(temp.get(i).getSum() < next.getSum())
                {

                    next = temp.get(i);
                }
            }
            closed.put(curr.getPos(), curr);
            curr = next;
        }
        return path;
    }
}
