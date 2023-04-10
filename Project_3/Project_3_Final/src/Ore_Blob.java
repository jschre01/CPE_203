import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Ore_Blob extends Entity_Animation {




    private static final String QUAKE_KEY = "quake";


    public Ore_Blob(Point position,
                      List<PImage> images, int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);


    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = world.findNearest(this.getPosition(), Vein.class);
        long nextPeriod = this.getActionPeriod();

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (this.moveTo(world, blobTarget.get(), scheduler))
            {
                Quake quake = Quake.createQuake(tgtPos, imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }



    public Point nextPosition(Point pos, WorldModel world)
    {
        int horiz = Integer.signum(pos.x - getPosition().x);
        Point newPos = new Point(getPosition().x + horiz,
                getPosition().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.getClass().equals(Ore.class))))
        {
            int vert = Integer.signum(pos.y - getPosition().y);
            newPos = new Point(getPosition().x, getPosition().y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.getClass().equals(Ore.class))))
            {
                newPos = getPosition();
            }
        }

        return newPos;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (getPosition().adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(target.getPosition(), world);

            if (!getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public static Ore_Blob createOreBlob(Point p,
                                int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Ore_Blob(p, images, actionPeriod, animationPeriod);
    }




}
