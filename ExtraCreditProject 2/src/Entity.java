import processing.core.PImage;
import java.util.function.BiPredicate;
import java.util.List;
import java.util.function.Function;

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

    public static Function<PImage, PImage> shrinkImage = i ->
    {
        i.resize(32, 32);
        return i.get();
    };

    public static Function<PImage, PImage> shrinkImageRec = i ->
    {
        i.resize(16, 32);
        return i.get();
    };

    public static Function<PImage, PImage> shrinkImageMax = i ->
    {
        i.resize(16, 16);
        return i.get();
    };

    public static BiPredicate <PImage, ImageStore> canPassThrough = (p,i) -> !(p.equals(i.getImageList("water").get(0))) && !(p.equals(i.getImageList("lava").get(0)));

    public void setPosition(Point p)
    {
        this.position = p;
    }

    public List<PImage> getImages()
    {
        return images;
    }


    public PImage getCurrentImage()
    {
        return images.get(imageIndex);
    }



}
