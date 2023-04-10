import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Ore implements Entity_Action {

    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;

    private static final Random rand = new Random();

    private static final String BLOB_KEY = "blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;


    public Ore(Point position, List<PImage> images, int actionPeriod) {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;

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
        Point pos = this.position;  // store current position before removing

        this.removeEntity(world);
        scheduler.unscheduleAllEvents(this);

        Ore_Blob blob = Ore_Blob.createOreBlob(pos,
                this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                actionPeriod);
    }


    public static Ore createOre(Point p, int actionPeriod, List<PImage> images)
    {
        return new Ore(p, images, actionPeriod);
    }
}
