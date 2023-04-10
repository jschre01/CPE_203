import processing.core.PImage;

import java.util.List;
import java.util.stream.Collectors;

public class Gold extends Entity {



    public Gold(Point position, List<PImage> images) {
        super(position, images, 0);
    }


    public static Gold createGold(Point p, List<PImage> images)
    {
        return new Gold(p, images.stream().map(shrinkImageMax).collect(Collectors.toList()));
    }
}

