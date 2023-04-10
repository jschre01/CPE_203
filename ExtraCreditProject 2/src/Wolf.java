import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Wolf extends Entity_Action {

    private int goldNum;


    public Wolf(Point position,
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

    public void decrementGoldNum() {
        goldNum -= 1;
    }



    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        long nextPeriod = this.getActionPeriod();
        if(getGoldNum() == 0)
        {
            Optional<Entity> player = world.findNearest(this.getPosition(), Player.class);


            if (player.isPresent()) {

                if (this.moveTo(world, player.get(), scheduler, imageStore)) {
                    nextPeriod += this.getActionPeriod();
                }
            }
        }
        else
        {
            Optional<Entity> fbi = world.findNearest(this.getPosition(), FBI.class);
            if(fbi.isPresent())
            {
                if (this.moveTo(world, fbi.get(), scheduler, imageStore))
                {
                        nextPeriod += this.getActionPeriod();
                }
            }

        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public List<Point> nextPosition(Point start, Point target,  WorldModel world, ImageStore imageStore)
    {

        //SingleStepPathingStrategy a = new SingleStepPathingStrategy();
        AStarPathingStrategy a = new AStarPathingStrategy();

        return a.computePath(start, target, p -> !(world.isOccupied(p)) && world.withinBounds(p) &&canPassThrough.test(world.getBackgroundImage(p).get(), imageStore),
                (p1, p2) -> ((Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) == 0) ||
                        Math.abs(p1.x - p2.x) == 0 && Math.abs(p1.y - p2.y) <=1),
                PathingStrategy.CARDINAL_NEIGHBORS);

    }


    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore)
    {
        if (getPosition().adjacent(target.getPosition()))
        {
            if(target.getClass().equals(Player.class))
            {
                Player p  = (Player)target;
                incrementGoldNum();
                p.decrementGoldNum();
            }
            if(target.getClass().equals(FBI.class)) {
                FBI f = (FBI) target;
                decrementGoldNum();
                f.incrementGoldNum();
            }
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

    public static Wolf createWolf(Point p, int actionPeriod,  List<PImage> images)
    {
        return new Wolf(p, images.stream().map(shrinkImage).collect(Collectors.toList()), actionPeriod);
    }
}

