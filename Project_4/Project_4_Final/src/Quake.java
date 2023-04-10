import processing.core.PImage;

import java.util.List;

public class Quake extends Entity_Animation {





    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent(this,
                Animation.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
                getAnimationPeriod());
    }



    public static Quake createQuake(Point p, List<PImage> images)
    {
        return new Quake(p, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }
}
