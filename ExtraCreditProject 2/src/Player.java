import processing.core.PImage;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

public class Player extends Entity {

    private int goldNum;

    public Player(Point position, List<PImage> images) {
        super(position, images, 0);
        goldNum = 0;


    }

    public void incrementGoldNum()
    {
        goldNum += 1;
    }

    public void decrementGoldNum(){
        goldNum -= 1;
    }

    public int getGoldNum()
    {
        return goldNum;
    }

    public void shift(WorldModel world, ImageStore imageStore, int dx, int dy)
    {
        Point pos = new Point(getPosition().x + dx, getPosition().y + dy);
        if(world.isOccupied(pos))
        {
            if(checkForGold(world,pos))
            {
                setPosition(pos);
                incrementGoldNum();
            }
        }
        else if(world.getBackgroundImage(pos).isPresent())
        {
            if(world.withinBounds(pos) && canPassThrough.test(world.getBackgroundImage(pos).get(), imageStore))
            {
                setPosition(pos);
            }
        }

    }

    public boolean checkForGold(WorldModel world, Point pos)
    {
        Optional<Entity> gold = world.findNearest(pos, Gold.class);
        if(gold.isPresent())
        {
            if(pos.equals(gold.get().getPosition()))
            {
                world.removeEntity(gold.get());
                world.decrementGoldCount();
                incrementGoldNum();
                return true;
            }
        }
        return false;
    }

    public void attack(WorldModel world, EventScheduler scheduler) {
        boolean flag = false;
        Optional<Entity> entity = world.findNearest(getPosition(), Tree.class);
        if (entity.isPresent()) {
            Entity e = entity.get();
            if (getPosition().adjacent(e.getPosition())) {
                world.removeEntity(entity.get());
                scheduler.unscheduleAllEvents(entity.get());
                flag = true;

            }
        }
        if(!flag)
        {
            entity = world.findNearest(getPosition(), Terminator_Dog.class);
            if(entity.isPresent()){
                Terminator_Dog e = (Terminator_Dog)entity.get();
                if(getPosition().adjacent(e.getPosition())){
                    e.changeAllegiance();
                }
            }
        }
        if(!flag)
        {
            entity = world.findNearest(getPosition(), Mountain.class);
            if(entity.isPresent()){
                Entity e = entity.get();
                if(getPosition().adjacent(e.getPosition())){
                    world.removeEntity(entity.get());
                    scheduler.unscheduleAllEvents(entity.get());
                }
            }
        }

    }

    public static Player createPlayer(Point p, List<PImage> images)
    {
        return new Player(p, images.stream().map(shrinkImage).collect(Collectors.toList()));
    }
}
