import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Miner_Full extends Miner {


    public Miner_Full(String id, Point position,
                      List<PImage> images, int resourceLimit,
                      int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
    }



    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(), Blacksmith.class);

        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            this.transform(world, scheduler, imageStore);
        }
        else
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

    public void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        Miner_Not_Full miner = Miner_Not_Full.createMinerNotFull(getId(), getPosition(), getResourceLimit(),
                getActionPeriod(), getAnimationPeriod(),
                getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }


    public static Miner_Full createMinerFull(String id, Point p, int resourceLimit,
                                  int actionPeriod, int animationPeriod,
                                  List<PImage> images)
    {
        return new Miner_Full(id, p, images,
                resourceLimit, actionPeriod, animationPeriod);
    }
}
