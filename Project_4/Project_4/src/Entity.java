import processing.core.PImage;

import java.util.List;

public abstract class  Entity {

    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(Point pos, List<PImage> imgs, int imgind)
    {
        position = pos;
        images = imgs;
        imageIndex = imgind;
    }


    public Point getPosition()
    {
        return this.position;
    }



    public void setPosition(Point p)
    {
        this.position = p;
    }

    public List<PImage> getImages()
    {
        return images;
    }


    public int getImageIndex()
    {
        return imageIndex;
    }

    public void setImageIndex(int i)
    {
        imageIndex = i;
    }




    public PImage getCurrentImage()
    {
        return images.get(imageIndex);
    }



}
