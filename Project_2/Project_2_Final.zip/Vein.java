import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein implements Entity_Action {

    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;

    private static final Random rand = new Random();


    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;

    private static final String ORE_KEY = "ore";

    public Vein(Point position, List<PImage> images, int actionPeriod) {
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
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent())
        {
            Ore ore = Ore.createOre( openPt.get(),
                    ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                actionPeriod);
    }

    public static Vein createVein(Point p, int actionPeriod,
                             List<PImage> images)
    {
        return new Vein(p, images, actionPeriod);
    }
}
