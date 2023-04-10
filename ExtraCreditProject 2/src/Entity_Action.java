import processing.core.PImage;

import java.util.List;

public abstract class Entity_Action extends Entity{

    private int actionPeriod;

    public Entity_Action(Point position, List<PImage> images, int actionPeriod)
    {
        super(position, images, 0);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod()
    {
        return this.actionPeriod;
    }

    public void setActionPeriod(int i) { actionPeriod = i;}

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                actionPeriod);
    }

}
