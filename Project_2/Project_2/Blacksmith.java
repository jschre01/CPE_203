import processing.core.PImage;

import java.util.List;

public class Blacksmith implements Entity {
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Blacksmith(Point position, List<PImage> images) {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
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

    public static Blacksmith createBlacksmith(Point p, List<PImage> images)
    {
        return new Blacksmith(p, images);
    }


}
