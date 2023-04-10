import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Miner_Not_Full extends Miner {



    public Miner_Not_Full(String id, Point position,
                      List<PImage> images, int resourceLimit, int resourceCount,
                      int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }



    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(), Ore.class);

        if (!notFullTarget.isPresent() ||
                !this.moveTo(world, notFullTarget.get(), scheduler) ||
                !this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }






    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (getPosition().adjacent(target.getPosition()))
        {
            incrementResourceCount(1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos;
            List<Point> nextPositions = this.nextPosition(this.getPosition(), target.getPosition(), world);
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


    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (getResourceCount() >= getResourceLimit())
        {
            Miner_Full miner = Miner_Full.createMinerFull(getId(), getPosition(), getResourceLimit(),
                    getActionPeriod(), getAnimationPeriod(),
                    getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }



    public static Miner_Not_Full createMinerNotFull(String id, Point p, int resourceLimit,
                                     int actionPeriod, int animationPeriod,
                                     List<PImage> images)
    {
        return new Miner_Not_Full(id, p, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }
}
