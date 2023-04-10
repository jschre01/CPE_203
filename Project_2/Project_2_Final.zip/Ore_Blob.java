import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Ore_Blob implements Entity_Advanced {

    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;


    private static final String QUAKE_KEY = "quake";


    public Ore_Blob(Point position,
                      List<PImage> images, int actionPeriod, int animationPeriod) {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;

    }

    public Point getPosition()
    {
        return this.position;
    }



    public void setPosition(Point p)
    {
        this.position = p;
    }



    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }


    public void removeEntity(WorldModel world)
    {
        world.removeEntityAt(this.position);
    }

    public PImage getCurrentImage()
    {
        return images.get(imageIndex);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = world.findNearest(this.position, Vein.class);
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (this.moveTo(world, blobTarget.get(), scheduler))
            {
                Quake quake = Quake.createQuake(tgtPos, imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                actionPeriod);
        scheduler.scheduleEvent(this,
                Animation.createAnimationAction(this,0), animationPeriod);
    }

    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }

    public Point nextPosition(Point pos, WorldModel world)
    {
        int horiz = Integer.signum(pos.x - position.x);
        Point newPos = new Point(position.x + horiz,
                position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.getClass().equals(Ore.class))))
        {
            int vert = Integer.signum(pos.y - position.y);
            newPos = new Point(position.x, position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.getClass().equals(Ore.class))))
            {
                newPos = position;
            }
        }

        return newPos;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (position.adjacent(target.getPosition()))
        {
            target.removeEntity(world);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(target.getPosition(), world);

            if (!position.equals(nextPos))
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
