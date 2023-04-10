import processing.core.PImage;
import java.util.stream.Collectors;

import java.util.List;

public class Tree extends Entity {


    public Tree(Point position, List<PImage> images) {
        super(position, images, 0);
    }


    public static Tree createTree(Point p, List<PImage> images) {
        return new Tree(p, images.stream().map(shrinkImage).collect(Collectors.toList()));
    }
}
