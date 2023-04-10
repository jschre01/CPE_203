import processing.core.PImage;

import java.util.*;

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;
   private int goldCount;

   private static final int PROPERTY_KEY = 0;
   private static final int ORE_REACH = 1;

   private static final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static final String PLAYER_KEY = "player";
   private static final int PLAYER_NUM_PROPERTIES = 3;
   private static final int PLAYER_COL = 1;
   private static final int PLAYER_ROW = 2;

   private static final String TREE1_KEY = "tree1";
   private static final String TREE2_KEY = "tree2";
   private static final int TREE_NUM_PROPERTIES = 4;
   private static final int TREE_COL = 2;
   private static final int TREE_ROW = 3;

   private static final String GOLD_KEY = "gold";
   private static final int GOLD_NUM_PROPERTIES = 4;
   private static final int GOLD_COL = 2;
   private static final int GOLD_ROW = 3;

   private static final String DETECTIVE_KEY = "detective";
   private static final int DETECTIVE_NUM_PROPERTIES = 5;
   private static final int DETECTIVE_COL = 1;
   private static final int DETECTIVE_ROW = 2;
   private static final int DETECTIVE_ACTION_PERIOD = 3;

   private static final String FBI_KEY = "fbi";
   private static final int FBI_NUM_PROPERTIES = 5;
   private static final int FBI_COL = 1;
   private static final int FBI_ROW = 2;
   private static final int FBI_ACTION_PERIOD = 3;

   private static final String TERMINATOR_KEY = "terminator";
   private static final int TERMINATOR_NUM_PROPERTIES = 5;
   private static final int TERMINATOR_COL = 1;
   private static final int TERMINATOR_ROW = 2;
   private static final int TERMINATOR_ACTION_PERIOD = 3;

    private static final String WOLF_KEY = "wolf";
    private static final int WOLF_NUM_PROPERTIES = 5;
    private static final int WOLF_COL = 1;
    private static final int WOLF_ROW = 2;
    private static final int WOLF_ACTION_PERIOD = 3;

    private static final String DOG_KEY = "terminator_dog";
    private static final int DOG_NUM_PROPERTIES = 5;
    private static final int DOG_COL = 1;
    private static final int DOG_ROW = 2;
    private static final int DOG_ACTION_PERIOD = 3;

   private static final String MOUNTAIN_KEY = "mountain";
   private static final int MOUNTAIN_NUM_PROPERTIES = 4;
   private static final int MOUNTAIN_COL = 2;
   private static final int MOUNTAIN_ROW = 3;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();
      goldCount = 0;

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public int getNumRows()
   {
      return this.numRows;
   }

   public int getNumCols()
   {
      return this.numCols;
   }

   public Set<Entity> getEntities()
   {
      return this.entities;
   }

   public void setBackgroundCell(Point pos, Background background)
   {
      this.background[pos.y][pos.x] = background;
   }

   public void setOccupancyCell(Point pos, Entity entity)
   {
      this.occupancy[pos.y][pos.x] = entity;
   }

   public void setBackground(Point pos, Background background)
   {
      if (this.withinBounds(pos))
      {
         this.setBackgroundCell(pos, background);
      }
   }

   public void removeEntityAt(Point pos)
   {
      if (this.withinBounds(pos)
              && this.getOccupancyCell(pos) != null)
      {
         Entity entity = this.getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point (-1, -1));
         this.entities.remove(entity);
         this.setOccupancyCell(pos, null);
      }
   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (this.withinBounds(pos) && !pos.equals(oldPos))
      {
         this.setOccupancyCell(oldPos, null);
         this.removeEntityAt(pos);
         this.setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void tryAddEntity(Entity entity)
   {
      if (this.isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      this.addEntity(entity);
   }

      /*
      Assumes that there is no entity currently occupying the
      intended destination cell.
   */
   public void addEntity(Entity entity)
   {
      if (this.withinBounds(entity.getPosition()))
      {
         this.setOccupancyCell(entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

   public boolean parseFBI(String [] properties, ImageStore imageStore)
   {
      if (properties.length == FBI_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[FBI_COL]),
                 Integer.parseInt(properties[FBI_ROW]));
         FBI entity = FBI.createFBI(pt, Integer.parseInt(properties[FBI_ACTION_PERIOD]),
                 imageStore.getImageList(FBI_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == FBI_NUM_PROPERTIES;
   }

    public boolean parseWolf(String [] properties, ImageStore imageStore)
    {
        if (properties.length == WOLF_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[WOLF_COL]),
                    Integer.parseInt(properties[WOLF_ROW]));
            Wolf entity = Wolf.createWolf(pt, Integer.parseInt(properties[WOLF_ACTION_PERIOD]),
                    imageStore.getImageList(WOLF_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == WOLF_NUM_PROPERTIES;
    }

    public boolean parseTerminatorDog(String [] properties, ImageStore imageStore)
    {
        if (properties.length == DOG_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[DOG_COL]),
                    Integer.parseInt(properties[DOG_ROW]));
            Terminator_Dog entity = Terminator_Dog.createDog(pt, Integer.parseInt(properties[DOG_ACTION_PERIOD]),
                    imageStore.getImageList(DOG_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == DOG_NUM_PROPERTIES;
    }

   public boolean parseBackground(String [] properties, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         this.setBackground(pt, new Background(imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public boolean parseDetective(String [] properties, ImageStore imageStore)
   {
      if (properties.length == DETECTIVE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[DETECTIVE_COL]),
                 Integer.parseInt(properties[DETECTIVE_ROW]));
         Detective entity = Detective.createDetective(pt,
                 Integer.parseInt(properties[DETECTIVE_ACTION_PERIOD]),
                 imageStore.getImageList(DETECTIVE_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == DETECTIVE_NUM_PROPERTIES;
   }

   public boolean parseTreeOne(String [] properties, ImageStore imageStore)
   {
      if (properties.length == TREE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[TREE_COL]),
                 Integer.parseInt(properties[TREE_ROW]));
         Tree entity = Tree.createTree(pt,
                 imageStore.getImageList(TREE1_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == TREE_NUM_PROPERTIES;
   }

   public boolean parseTreeTwo(String [] properties, ImageStore imageStore)
   {
      if (properties.length == TREE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[TREE_COL]),
                 Integer.parseInt(properties[TREE_ROW]));
         Tree entity = Tree.createTree(pt,
                 imageStore.getImageList(TREE2_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == TREE_NUM_PROPERTIES;
   }

    public boolean parseMountain(String [] properties, ImageStore imageStore)
    {
        if (properties.length == MOUNTAIN_NUM_PROPERTIES)
        {
            Point pt = new Point(
                    Integer.parseInt(properties[MOUNTAIN_COL]),
                    Integer.parseInt(properties[MOUNTAIN_ROW]));
            Mountain entity = Mountain.createMountain(pt,
                    imageStore.getImageList(MOUNTAIN_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == MOUNTAIN_NUM_PROPERTIES;
    }

   public boolean parseGold(String [] properties, ImageStore imageStore)
   {
      if (properties.length == GOLD_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[GOLD_COL]),
                 Integer.parseInt(properties[GOLD_ROW]));
         Gold entity = Gold.createGold(pt, imageStore.getImageList(GOLD_KEY));
         this.tryAddEntity(entity);
      }
      goldCount += 1;
      return properties.length == GOLD_NUM_PROPERTIES;
   }

   public boolean parsePlayer(String [] properties, ImageStore imageStore)
   {
     if (properties.length == PLAYER_NUM_PROPERTIES)
     {
        Point pt = new Point(Integer.parseInt(properties[PLAYER_COL]),
                Integer.parseInt(properties[PLAYER_ROW]));
         Player entity = Player.createPlayer(pt, imageStore.getImageList(PLAYER_KEY));
        this.tryAddEntity(entity);
      }
      return properties.length == PLAYER_NUM_PROPERTIES;
  }

  public boolean parseTerminator(String [] properties, ImageStore imageStore)
  {
      if (properties.length == TERMINATOR_NUM_PROPERTIES)
      {
          Point pt = new Point(Integer.parseInt(properties[TERMINATOR_COL]),
                  Integer.parseInt(properties[TERMINATOR_ROW]));
          Terminator entity = Terminator.createTerminator(pt, Integer.parseInt(properties[TERMINATOR_ACTION_PERIOD]),
                  imageStore.getImageList(TERMINATOR_KEY));
          this.tryAddEntity(entity);
      }
      return properties.length == TERMINATOR_NUM_PROPERTIES;
  }

   public boolean processLine(String line, ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return this.parseBackground(properties, imageStore);
            case PLAYER_KEY:
               return this.parsePlayer(properties, imageStore);
            case TREE1_KEY:
               return this.parseTreeOne(properties, imageStore);
            case TREE2_KEY:
               return this.parseTreeTwo(properties, imageStore);
             case MOUNTAIN_KEY:
                 return this.parseMountain(properties, imageStore);
            case GOLD_KEY:
               return this.parseGold(properties, imageStore);
            case DETECTIVE_KEY:
              return this.parseDetective(properties, imageStore);
            case FBI_KEY:
               return this.parseFBI(properties, imageStore);
             case WOLF_KEY:
                 return this.parseWolf(properties, imageStore);
             case TERMINATOR_KEY:
                 return this.parseTerminator(properties, imageStore);
             case DOG_KEY:
                 return this.parseTerminatorDog(properties, imageStore);
         }
      }

      return false;
   }





   public int getGoldCount()
   {
       return goldCount;
   }

   public void decrementGoldCount()
   {
       goldCount -= 1;
   }




   public Entity getOccupancyCell(Point p)
   {
      return this.occupancy[p.y][p.x];
   }

   public Background getBackgroundCell(Point p)
   {
      return this.background[p.y][p.x];
   }

   public boolean withinBounds(Point pos)
   {
      return pos.y >= 0 && pos.y < this.getNumRows() &&
              pos.x >= 0 && pos.x < this.getNumCols();
   }



   public Optional<Entity> getOccupant(Point pos)
   {
      if (this.isOccupied(pos))
      {
         return Optional.of(this.getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
      {
         for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (this.withinBounds(newPt) &&
                    !this.isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public Optional<Entity> findNearest(Point pos, Class kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.getEntities())
      {
         if (entity.getClass() == kind)
         {
            ofType.add(entity);
         }
      }

      return pos.nearestEntity(ofType);
   }

   public Player getPlayer(ImageStore imageStore)
   {
       Entity player = null;
       for(Entity entity : this.getEntities())
       {
           if(entity.getClass() == Player.class)
           {
               player = entity;
           }
       }
       if(player != null) {

           return (Player)player;
       }
       return Player.createPlayer(new Point(0,0), imageStore.getImageList(PLAYER_KEY));
   }

   public Detective getDetective(ImageStore imageStore)
   {
       Entity detective = null;
       for(Entity entity : this.getEntities())
       {
           if(entity.getClass() == Detective.class)
           {
               detective = entity;
           }
       }
       if(detective != null)
       {
           return (Detective)detective;
       }
       return Detective.createDetective(new Point(29, 19), 250, imageStore.getImageList(DETECTIVE_KEY));
   }

    public FBI getFBI(ImageStore imageStore)
    {
        Entity fbi = null;
        for(Entity entity : this.getEntities())
        {
            if(entity.getClass() == FBI.class)
            {
                fbi = entity;
            }
        }
        if(fbi != null)
        {
            return (FBI)fbi;
        }
        return FBI.createFBI(new Point(29, 19), 250,  imageStore.getImageList(FBI_KEY));
    }

    public Terminator getTerminator(ImageStore imageStore)
    {
        Entity terminator = null;
        for(Entity entity : this.getEntities())
        {
            if(entity.getClass() == Terminator.class)
            {
                terminator = entity;
            }
        }
        if(terminator != null)
        {
            return (Terminator)terminator;
        }
        return Terminator.createTerminator(new Point(29, 19), 250,  imageStore.getImageList(TERMINATOR_KEY));
    }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (this.withinBounds(pos))
      {
         return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

   public boolean isOccupied(Point pos)
   {
      return this.withinBounds(pos) &&
              this.getOccupancyCell(pos) != null;
   }

   public void removeEntity(Entity entity)
   {
      this.removeEntityAt(entity.getPosition());
   }





}
