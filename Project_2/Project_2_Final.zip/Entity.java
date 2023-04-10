import processing.core.PImage;

public interface Entity {
    Point getPosition();
    void setPosition(Point p);
    void nextImage();
    void removeEntity(WorldModel world);
    PImage getCurrentImage();


}
