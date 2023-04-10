import processing.core.PImage;

import java.util.List;

public abstract class Miner extends Entity_Animation {

    private String id;
    private int resourceLimit;
    private int resourceCount;

    public Miner(String id, Point position,
                           List<PImage> images, int resourceLimit, int resourceCount,
                           int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
        this.id = id;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public String getId()
    {
        return id;
    }

    public int getResourceLimit()
    {
        return resourceLimit;
    }

    public int getResourceCount()
    {
        return resourceCount;
    }

    public void incrementResourceCount(int i)
    {
        resourceCount += i;
    }

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    public List<Point> nextPosition(Point start, Point target,  WorldModel world)
    {

        //SingleStepPathingStrategy a = new SingleStepPathingStrategy();
        AStarPathingStrategy a = new AStarPathingStrategy();

        return a.computePath(start, target, p -> !(world.isOccupied(p)) && world.withinBounds(p),
                (p1, p2) -> ((Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) == 0) ||
                        Math.abs(p1.x - p2.x) == 0 && Math.abs(p1.y - p2.y) <=1),
                PathingStrategy.CARDINAL_NEIGHBORS);

//        int horiz = Integer.signum(pos.x - getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz,
//                this.getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos))
//        {
//            int vert = Integer.signum(pos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x,
//                    this.getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos))
//            {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
    }



}
