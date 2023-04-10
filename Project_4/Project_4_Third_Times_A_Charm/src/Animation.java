public class Animation implements Action{

    private Entity_Animation entity;
    private int repeatCount;

    public Animation(Entity_Animation entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;

    }


    public void executeAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity,
                    createAnimationAction(this.entity, Math.max(this.repeatCount - 1, 0)),
                    this.entity.getAnimationPeriod());
        }
    }


    public static Action createAnimationAction(Entity_Animation entity, int repeatCount)
    {
        return new Animation(entity, repeatCount);
    }


}


