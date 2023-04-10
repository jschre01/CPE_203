import processing.core.PImage;

import java.util.List;
import java.util.stream.Collectors;

public class Mountain extends Entity {


    public Mountain(Point position, List<PImage> images) {
        super(position, images, 0);
    }


    public static Mountain createMountain(Point p, List<PImage> images) {
        return new Mountain(p, images.stream().map(shrinkImage).collect(Collectors.toList()));
    }
}
