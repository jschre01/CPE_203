import processing.core.PImage;

import java.util.List;

public abstract class Entity_Animation extends Entity_Action{

    private int animationPeriod;

    public Entity_Animation(Point position, List<PImage> images,
                            int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }


    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent(this,
                Animation.createAnimationAction(this, 0), getAnimationPeriod());
    }

    public void nextImage()
    {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());//= (this.getImageIndex() + 1) % this.getImages().size();
    }
}
