import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FBI extends Entity_Action {

    private int goldNum;


    public FBI(Point position,
                     List<PImage> images, int actionPeriod) {
        super(position, images, actionPeriod);
        goldNum = 0;


    }

    public int getGoldNum()
    {
        return goldNum;
    }

    public void incrementGoldNum()
    {
        goldNum += 1;
    }



    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> gold = world.findNearest(this.getPosition(), Gold.class);
        long nextPeriod = this.getActionPeriod();

        if (gold.isPresent())
        {

            if (this.moveTo(world, gold.get(), scheduler, imageStore))
            {
                incrementGoldNum();
                nextPeriod += this.getActionPeriod();
            }
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public List<Point> nextPosition(Point start, Point target,  WorldModel world, ImageStore imageStore) {

        //SingleStepPathingStrategy a = new SingleStepPathingStrategy();
        AStarPathingStrategy a = new AStarPathingStrategy();

        return a.computePath(start, target, p -> !(world.isOccupied(p)) && world.withinBounds(p) && canPassThrough.test(world.getBackgroundImage(p).get(), imageStore),
                (p1, p2) -> ((Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) == 0) ||
                        Math.abs(p1.x - p2.x) == 0 && Math.abs(p1.y - p2.y) <= 1),
                PathingStrategy.CARDINAL_NEIGHBORS);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore)
    {
        if (getPosition().adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            world.decrementGoldCount();
            incrementGoldNum();
            return true;
        }
        else
        {
            Point nextPos;
            List<Point> nextPositions = this.nextPosition(getPosition(), target.getPosition(), world, imageStore);

            if(nextPositions.size() != 0)
            {
                nextPos = nextPositions.get(0);


                if (!getPosition().equals(nextPos))
                {
                    Optional<Entity> occupant = world.getOccupant(nextPos);
                    if (occupant.isPresent())
                    {
                        scheduler.unscheduleAllEvents(occupant.get());
                    }

                    world.moveEntity(this, nextPos);
                }}
            return false;
        }
    }

    public static FBI createFBI(Point p, int actionPeriod, List<PImage> images)
    {
        return new FBI(p, images.stream().map(shrinkImageRec).collect(Collectors.toList()), actionPeriod);
    }
}

