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
        //final Node c = curr;
        //Function<Point, Node> mapToNode = p -> {return new Node(p, (Math.abs(p.x - end.x) + Math.abs(p.y - end.y)), 1 + c.getTraveled(), c);};
        List<Point> path = new LinkedList<>();
        HashMap<Point, Node> closed = new HashMap<>();
        //PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(Node::getSum));
        Stream<Node> s = potentialNeighbors.apply(start).filter(canPassThrough).map(new MapToNode(end, curr));
        List<Node> temp =  s.collect(Collectors.toList());
        Node next = curr;
        if(temp.size() != 0) {
            next = temp.get(0);
        }
        for(int i = 0; i < temp.size(); i++)
        {
            //System.out.println(temp.get(i).getTraveled() + " " + temp.get(i).getHeuristic() + " " + temp.get(i).getSum());
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
        //System.out.println("Old man in town " + curr.getTraveled() + " " + curr.getHeuristic() + " " + curr.getSum());
        curr = next;
        //System.out.println("New man in town " + curr.getTraveled() + " " + curr.getHeuristic() + " " + curr.getSum());
        while(temp.size() != 0)
        {
            //for(int k = 0; k < temp.size(); k++)
            //{
                //System.out.println(" " + temp.get(k));
            //}
            HashMap<Point, Node> open = new HashMap<>();
            for(int j = 0; j < temp.size(); j++)
            {
                open.put(temp.get(j).getPos(), temp.get(j));
            }
            Stream<Node> t = potentialNeighbors.apply(curr.getPos()).filter(canPassThrough).map(new MapToNode(end, curr));
            List <Node> interns = t.collect(Collectors.toList());
            //System.out.print(interns.size());
            for(int i = 0; i < interns.size(); i++)
            {
                if(open.containsKey(interns.get(i).getPos()))
                {
                    if(interns.get(i).getHeuristic() < temp.get(temp.indexOf(open.get(interns.get(i).getPos()))).getHeuristic())
                    {
                        temp.remove(interns.get(i));
                        open.remove(interns.get(i).getPos());
                    }
                }
                else if(!closed.containsKey(interns.get(i).getPos()))
                {
                    temp.add(interns.get(i));
                }
            }
            next = temp.get(0);
            for(int i = 0; i < temp.size(); i++)
            {
                //System.out.println("Position" + i + ": " +  temp.get(i).getTraveled() + " " + temp.get(i).getHeuristic() + " " + temp.get(i).getSum());
                if(withinReach.test(temp.get(i).getPos(), end))
                {
                    Node traceback = temp.get(i);
                    while(traceback != null)
                    {
                        path.add(traceback.getPos());
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
            temp.remove(curr);
            //System.out.println("Old man in town " + curr.getTraveled() + " " + curr.getHeuristic() + " " + curr.getSum());
            curr = next;
            //System.out.println("New man in town " + curr.getTraveled() + " " + curr.getHeuristic() + " " + curr.getSum());
        }
        return path;
    }
}
