import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Terminator_Dog extends Entity_Action {

    private int goldNum;
    private boolean loyal;


    public Terminator_Dog(Point position,
                List<PImage> images, int actionPeriod) {
        super(position, images, actionPeriod);
        goldNum = 0;
        loyal = true;


    }

    public int getGoldNum()
    {
        return goldNum;
    }

    public void incrementGoldNum()
    {
        goldNum += 2;
    }

    public void decrementGoldNum() {
        goldNum -= 2;
    }

    public void changeAllegiance()
    {
        loyal = false;
        goldNum = 0;
    }



    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        long nextPeriod = this.getActionPeriod();
        if(loyal) {
            if (getGoldNum() == 0) {
                Optional<Entity> player = world.findNearest(this.getPosition(), Player.class);


                if (player.isPresent()) {

                    if (this.moveTo(world, player.get(), scheduler, imageStore)) {
                        nextPeriod += this.getActionPeriod();
                    }
                }
            } else {
                Optional<Entity> terminator = world.findNearest(this.getPosition(), Terminator.class);
                if (terminator.isPresent()) {
                    if (this.moveTo(world, terminator.get(), scheduler, imageStore)) {
                        nextPeriod += this.getActionPeriod();
                    }
                }

            }
        }
        else
        {
            if(goldNum == 0) {
                Optional<Entity> terminator = world.findNearest(this.getPosition(), Terminator.class);
                if (terminator.isPresent())
                {
                    if (this.moveTo(world, terminator.get(), scheduler, imageStore))
                    {
                                nextPeriod += this.getActionPeriod();
                    }
                }
            }


            else {
                Optional<Entity> player = world.findNearest(this.getPosition(), Player.class);
                if (player.isPresent()) {
                    if (this.moveTo(world, player.get(), scheduler, imageStore)) {
                        nextPeriod += this.getActionPeriod();
                    }
                }
            }
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public List<Point> nextPosition(Point start, Point target,  WorldModel world, ImageStore imageStore)
    {

        //SingleStepPathingStrategy a = new SingleStepPathingStrategy();
        AStarPathingStrategy a = new AStarPathingStrategy();

        return a.computePath(start, target, p -> !(world.isOccupied(p)) && world.withinBounds(p),
                (p1, p2) -> ((Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) == 0) ||
                        Math.abs(p1.x - p2.x) == 0 && Math.abs(p1.y - p2.y) <=1),
                PathingStrategy.CARDINAL_NEIGHBORS);

    }



    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore)
    {
        if (getPosition().adjacent(target.getPosition()))
        {
            if(target.getClass().equals(Player.class))
            {
                if(loyal) {
                    Player p  = (Player)target;
                    incrementGoldNum();
                    p.decrementGoldNum();
                    p.decrementGoldNum();
                }
                else {
                    Player p = (Player)target;
                    p.incrementGoldNum();
                    p.incrementGoldNum();
                    p.incrementGoldNum();
                    p.incrementGoldNum();
                    decrementGoldNum();
                }

            }
            if(target.getClass().equals(Terminator.class)) {
                if(loyal) {
                    Terminator f = (Terminator) target;
                    decrementGoldNum();
                    f.incrementGoldNum();
                    f.incrementGoldNum();
                }
                else
                {
                    Terminator f = (Terminator)target;
                    f.decrementGoldNum();
                    f.decrementGoldNum();
                    f.decrementGoldNum();
                    f.decrementGoldNum();
                    incrementGoldNum();
                    incrementGoldNum();
                    slowTerminator(f);
                }
            }
            return true;
        }
        else
        {
            Point nextPos;
            List<Point> nextPositions = this.nextPosition(getPosition(), target.getPosition(), world, imageStore);

            if(nextPositions.size() != 0)
            {
                nextPos = nextPositions.get(0);


                if (!getPosition().equals(nextPos))
                {
                    Optional<Entity> occupant = world.getOccupant(nextPos);
                    if (occupant.isPresent())
                    {
                        scheduler.unscheduleAllEvents(occupant.get());
                    }

                    world.moveEntity(this, nextPos);
                }}
            return false;
        }
    }

    public void slowTerminator(Terminator t)
    {
        t.setActionPeriod(t.getActionPeriod()*2);
    }

    public static Terminator_Dog createDog(Point p, int actionPeriod, List<PImage> images)
    {
        return new Terminator_Dog(p, images.stream().map(shrinkImage).collect(Collectors.toList()), actionPeriod);
    }
}
