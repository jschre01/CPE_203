import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Ore extends Entity_Action {



    private static final Random rand = new Random();

    private static final String BLOB_KEY = "blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;


    public Ore(Point position, List<PImage> images, int actionPeriod) {
        super(position, images, actionPeriod);


    }



    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Ore_Blob blob = Ore_Blob.createOreBlob(pos,
                this.getActionPeriod() / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }




    public static Ore createOre(Point p, int actionPeriod, List<PImage> images)
    {
        return new Ore(p, images, actionPeriod);
    }
}
