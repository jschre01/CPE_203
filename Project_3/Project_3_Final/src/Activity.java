public class Activity implements Action{

    private Entity_Action entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(Entity_Action entity, WorldModel world,
                  ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler)
    {

        this.entity.executeActivity(this.world, this.imageStore, scheduler);

    }

    public static Action createActivityAction(Entity_Action entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore);
    }

}
