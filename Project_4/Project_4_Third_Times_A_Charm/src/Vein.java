import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein extends Entity_Action {


    private static final Random rand = new Random();


    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;

    private static final String ORE_KEY = "ore";

    public Vein(Point position, List<PImage> images, int actionPeriod) {
        super(position, images, actionPeriod);


    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(super.getPosition());

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
                this.getActionPeriod());
    }


    public static Vein createVein(Point p, int actionPeriod,
                             List<PImage> images)
    {
        return new Vein(p, images, actionPeriod);
    }
}
