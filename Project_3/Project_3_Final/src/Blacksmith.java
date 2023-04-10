import processing.core.PImage;

import java.util.List;

public class Blacksmith extends Entity {


    public Blacksmith(Point position, List<PImage> images) {
        super(position, images, 0);
    }


    public static Blacksmith createBlacksmith(Point p, List<PImage> images)
    {
        return new Blacksmith(p, images);
    }


}
