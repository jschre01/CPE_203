import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity {



    public Obstacle(Point position, List<PImage> images) {
        super(position, images, 0);
    }

    public static Obstacle createObstacle(Point p, List<PImage> images)
    {
        return new Obstacle(p, images);
    }
}
