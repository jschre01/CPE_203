import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

final class Entity {
   private EntityKind kind;
   private String id;
   private Point position;
   private List<PImage> images;
   private int imageIndex;
   private int resourceLimit;
   private int resourceCount;
   private int actionPeriod;
   private int animationPeriod;

   private static final Random rand = new Random();

   private static final String BLOB_KEY = "blob";
   private static final String BLOB_ID_SUFFIX = " -- blob";
   private static final int BLOB_PERIOD_SCALE = 4;
   private static final int BLOB_ANIMATION_MIN = 50;
   private static final int BLOB_ANIMATION_MAX = 150;

   private static final String ORE_ID_PREFIX = "ore -- ";
   private static final int ORE_CORRUPT_MIN = 20000;
   private static final int ORE_CORRUPT_MAX = 30000;

   private static final String QUAKE_KEY = "quake";
   private static final String ORE_KEY = "ore";

   public Entity(EntityKind kind, String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod) {
      this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }


   public EntityKind getKind() {
      return this.kind;
   }

   public String getId() {
      return this.id;
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
      return this.images;
   }

   public int getImageIndex()
   {
      return this.imageIndex;
   }

   public int getResourceLimit()
   {
      return this.resourceLimit;
   }

   public void incrementResourceCount(int i)
   {
      this.resourceCount += i;
   }
   public int getResourceCount()
   {
      return this.resourceCount;
   }

   public int getActionPeriod()
   {
      return this.actionPeriod;
   }

   public int getAnimationPeriod()
   {
      switch (this.kind)
      {
         case MINER_FULL:
         case MINER_NOT_FULL:
         case ORE_BLOB:
         case QUAKE:
            return this.animationPeriod;
         default:
            throw new UnsupportedOperationException(
                    String.format("getAnimationPeriod not supported for %s",
                            this.kind));
      }
   }

   public void nextImage()
   {
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }


   public Action createAnimationAction(int repeatCount)
   {
      return new Action(ActionKind.ANIMATION, this, null, null, repeatCount);
   }

   public void executeMinerFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> fullTarget = world.findNearest(this.position, EntityKind.BLACKSMITH);

      if (fullTarget.isPresent() &&
              world.moveToFull(this, fullTarget.get(), scheduler))
      {
         world.transformFull(this, scheduler, imageStore);
      }
      else
      {
         scheduler.scheduleEvent(this,
                 this.createActivityAction(world, imageStore),
                 this.actionPeriod);
      }
   }

   public void executeMinerNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> notFullTarget = world.findNearest(this.position, EntityKind.ORE);

      if (!notFullTarget.isPresent() ||
              !world.moveToNotFull(this, notFullTarget.get(), scheduler) ||
              !world.transformNotFull(this, scheduler, imageStore))
      {
         scheduler.scheduleEvent(this,
                 this.createActivityAction(world, imageStore),
                 this.actionPeriod);
      }
   }

   public void executeOreActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Point pos = this.position;  // store current position before removing

      this.removeEntity(world);
      scheduler.unscheduleAllEvents(this);

      Entity blob = pos.createOreBlob(this.id + BLOB_ID_SUFFIX,
              this.actionPeriod / BLOB_PERIOD_SCALE,
              BLOB_ANIMATION_MIN +
                      rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
              imageStore.getImageList(BLOB_KEY));

      world.addEntity(blob);
      scheduler.scheduleActions(blob, world, imageStore);
   }

   public void executeOreBlobActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> blobTarget = world.findNearest(this.position, EntityKind.VEIN);
      long nextPeriod = this.actionPeriod;

      if (blobTarget.isPresent())
      {
         Point tgtPos = blobTarget.get().position;

         if (world.moveToOreBlob(this, blobTarget.get(), scheduler))
         {
            Entity quake = tgtPos.createQuake(imageStore.getImageList(QUAKE_KEY));

            world.addEntity(quake);
            nextPeriod += this.actionPeriod;
            scheduler.scheduleActions(quake, world, imageStore);
         }
      }

      scheduler.scheduleEvent(this,
              this.createActivityAction(world, imageStore),
              nextPeriod);
   }

   public void executeQuakeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(this);
      this.removeEntity(world);
   }

   public void executeVeinActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = world.findOpenAround(this.position);

      if (openPt.isPresent())
      {
         Entity ore = openPt.get().createOre(ORE_ID_PREFIX + this.id,
                  ORE_CORRUPT_MIN +
                         rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                 imageStore.getImageList(ORE_KEY));
         world.addEntity(ore);
         scheduler.scheduleActions(ore, world, imageStore);
      }

      scheduler.scheduleEvent(this,
              this.createActivityAction(world, imageStore),
              this.actionPeriod);
   }


   public Action createActivityAction(WorldModel world, ImageStore imageStore)
   {
      return new Action(ActionKind.ACTIVITY, this, world, imageStore, 0);
   }

   public void removeEntity(WorldModel world)
   {
      world.removeEntityAt(this.position);
   }

   public PImage getCurrentImage()
   {
      /*if (entity instanceof Background)
      {
         return ((Background)entity).images
                 .get(((Background)entity).imageIndex);
      }*/


      return this.getImages().get(this.getImageIndex());

     /* else
      {
         throw new UnsupportedOperationException(
                 String.format("getCurrentImage not supported for %s",
                         entity));
      }*/
   }

   public Point nextPositionOreBlob(Point pos, WorldModel world)
   {
      int horiz = Integer.signum(pos.x - this.getPosition().x);
      Point newPos = new Point(this.getPosition().x + horiz,
              this.getPosition().y);

      Optional<Entity> occupant = world.getOccupant(newPos);

      if (horiz == 0 ||
              (occupant.isPresent() && !(occupant.get().getKind() == EntityKind.ORE)))
      {
         int vert = Integer.signum(pos.y - this.getPosition().y);
         newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
         occupant = world.getOccupant(newPos);

         if (vert == 0 ||
                 (occupant.isPresent() && !(occupant.get().getKind() == EntityKind.ORE)))
         {
            newPos = this.getPosition();
         }
      }

      return newPos;
   }

   public Point nextPositionMiner(Point pos,  WorldModel world)
   {
      int horiz = Integer.signum(pos.x - this.getPosition().x);
      Point newPos = new Point(this.getPosition().x + horiz,
              this.getPosition().y);

      if (horiz == 0 || world.isOccupied(newPos))
      {
         int vert = Integer.signum(pos.y - this.getPosition().y);
         newPos = new Point(this.getPosition().x,
                 this.getPosition().y + vert);

         if (vert == 0 || world.isOccupied(newPos))
         {
            newPos = this.getPosition();
         }
      }

      return newPos;
   }

}
