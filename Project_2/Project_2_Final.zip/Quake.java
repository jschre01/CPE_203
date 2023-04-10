import processing.core.PImage;

import java.util.List;

public class Quake implements Entity_Animation {

    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;


    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(Point position, List<PImage> images,
                      int actionPeriod, int animationPeriod) {
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
        scheduler.unscheduleAllEvents(this);
        this.removeEntity(world);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                actionPeriod);
        scheduler.scheduleEvent(this,
                Animation.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
                animationPeriod);
    }

    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }

    public static Quake createQuake(Point p, List<PImage> images)
    {
        return new Quake(/*QUAKE_ID,*/ p, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }
}
