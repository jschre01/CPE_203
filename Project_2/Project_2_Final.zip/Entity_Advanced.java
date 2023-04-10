public interface Entity_Advanced extends Entity_Animation {
    Point nextPosition(Point pos, WorldModel world);
    boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
}
