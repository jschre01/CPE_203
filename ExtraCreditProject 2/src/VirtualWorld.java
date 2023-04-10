import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
import processing.core.*;

public final class VirtualWorld
   extends PApplet
{
   public static final int TIMER_ACTION_PERIOD = 100;

   public static final int VIEW_WIDTH = 960;
   public static final int VIEW_HEIGHT = 640;
   public static final int TILE_WIDTH = 32;
   public static final int TILE_HEIGHT = 32;

   public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   public static final int WORLD_COLS = VIEW_COLS;
   public static final int WORLD_ROWS = VIEW_ROWS;

   public static final String DEFAULT_IMAGE_NAME = "background_default";
   public static final int DEFAULT_IMAGE_COLOR = 0x808080;


   public static final String FAST_FLAG = "-fast";
   public static final String FASTER_FLAG = "-faster";
   public static final String FASTEST_FLAG = "-fastest";
   public static final double FAST_SCALE = 0.5;
   public static final double FASTER_SCALE = 0.25;
   public static final double FASTEST_SCALE = 0.10;

   public static double timeScale = 1.0;

   public ImageStore imageStore;
   public WorldModel world;
   public WorldView view;
   public EventScheduler scheduler;

   public String difficulty;

   public long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      worldGeneratorOne();
      loadImages("imageList", imageStore, this);
      loadWorld(world, "gaia1.sav", imageStore);
      difficulty = "easy";

      scheduleActions(world, scheduler, imageStore);
      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
      System.out.println("Welcome to goldSeeker2.0! Your mission is to collect more gold than your opponent before time runs out.");
      System.out.println("Watch out for water and lava - you can't pass through these tiles!");
      System.out.println("Move your character with the ARROW keys");
      System.out.println("You can't go through obstacles like trees or mountains, but you can remove them by clicking on the SPACEBAR");
      System.out.println("Watch out for wolfs and terminator dogs! They are trying to steal your gold and give it to your opponent!");
      System.out.println("Good luck, there are 3 levels.");


   }

   public void setupTwo()
   {
      this.imageStore = new ImageStore(
              createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
              createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
              TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      worldGeneratorTwo();
      loadImages("imageList", imageStore, this);
      loadWorld(world, "gaia2.sav", imageStore);
      difficulty = "medium";

      scheduleActions(world, scheduler, imageStore);
      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;



   }

   public void setupThree()
   {
      this.imageStore = new ImageStore(
              createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
              createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
              TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      worldGeneratorThree();
      loadImages("imageList", imageStore, this);
      loadWorld(world, "gaia3.sav", imageStore);
      difficulty = "hard";

      scheduleActions(world, scheduler, imageStore);
      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }


   public static void worldGeneratorOne()
   {
      try {
         FileWriter fileWriter = new FileWriter(new File("gaia1.sav"));
         PrintWriter printWriter = new PrintWriter(fileWriter);
         for(int i = 0; i < 30; i++)
         {
            for(int j = 0; j < 20; j++)
            {
               int chance1 = (int)(Math.random() * 100);
               if(chance1 <= 4)
               {
                  printWriter.printf("background water " + i + " " + j + "%n");
               }
               else if(chance1 <= 20)
               {
                  printWriter.printf("background dirt " + i + " " + j + "%n");
               }
               else
               {
                  printWriter.printf("background grass " + i + " " + j + "%n");
               }
               int chance2 = (int)(Math.random() * 100);
               if(chance2 <=3 && chance1 <= 20 && chance1 > 4 && (i != 0 | j != 0) && (i != 29 | j != 19))
               {
                  printWriter.printf("tree1 tree_" + i + "_" + j + " "+ i + " "+ j + "%n");
               }
               else if(chance2 <= 6 && !(chance1 <=4) && (i != 0 | j != 0) && (i != 29 | j != 19))
               {
                  printWriter.printf("tree2 tree_" + i + "_" + j + " " + i + " "+ j+ "%n");
               }
               int chance3 = (int)(Math.random() * 100);
               if(!(chance2 <=6) && !(chance1 <=4) && chance3 <=15 && (i != 0 | j != 0) && (i != 29 | j != 19))
               {


                  printWriter.printf("gold gold_" + i + "_" + j + " " + i + " "+ j + "%n");

               }
            }
         }
         printWriter.printf("player 0 0" + "%n");
         printWriter.printf("detective 29 19 1000 250");
         printWriter.close();
      }
      catch(IOException e)
      {
         System.out.print(e.getMessage());
      }


   }

   public static void worldGeneratorTwo()
   {
      try {
         FileWriter fileWriter = new FileWriter(new File("gaia2.sav"));
         PrintWriter printWriter = new PrintWriter(fileWriter);
         for(int i = 0; i < 30; i++)
         {
            for(int j = 0; j < 20; j++)
            {
               int chance1 = (int)(Math.random() * 100);
               if(chance1 <= 6)
               {
                  printWriter.printf("background water " + i + " " + j + "%n");
               }
               else if(chance1 <= 20)
               {
                  printWriter.printf("background snow " + i + " " + j + "%n");
               }
               else if(chance1 <= 35)
               {
                  printWriter.printf("background stone " + i + " " + j + "%n");
               }
               else
               {
                  printWriter.printf("background dirt " + i + " " + j + "%n");
               }
               int chance2 = (int)(Math.random() * 100);
               if(chance2 <=6 && chance1 > 6 && (i != 0 | j != 0) && (i != 29 | j != 19) && (i != 29 | j !=18))
               {
                  printWriter.printf("mountain mountain_" + i + "_" + j + " "+ i + " "+ j + "%n");
               }
               else if(chance2 <= 12 && !(chance1 <=6) && (i != 0 | j != 0) && (i != 29 | j != 19) && (i != 29 | j != 18))
               {
                  printWriter.printf("tree2 tree_" + i + "_" + j + " " + i + " "+ j+ "%n");
               }
               int chance3 = (int)(Math.random() * 100);
               if(!(chance2 <=12) && !(chance1 <=6) && chance3 <=12 && (i != 0 | j != 0) && (i != 29 | j != 19) && (i != 29 | j != 18))
               {


                  printWriter.printf("gold gold_" + i + "_" + j + " " + i + " "+ j + "%n");

               }
            }
         }
         printWriter.printf("player 0 0" + "%n");
         printWriter.printf("fbi 29 19 500 250 %n");
         printWriter.printf("wolf 29 18 1000 250");
         printWriter.close();
      }
      catch(IOException e)
      {
         System.out.print(e.getMessage());
      }
   }

   public static void worldGeneratorThree()
   {
      try {
         FileWriter fileWriter = new FileWriter(new File("gaia3.sav"));
         PrintWriter printWriter = new PrintWriter(fileWriter);
         for(int i = 0; i < 30; i++)
         {
            for(int j = 0; j < 20; j++)
            {
               int chance1 = (int)(Math.random() * 100);
               if(chance1 <= 4)
               {
                  printWriter.printf("background water " + i + " " + j + "%n");
               }
               else if(chance1 <= 12)
               {
                  printWriter.printf("background lava " + i + " " + j + "%n");
               }
               else if(chance1 <= 40)
               {
                  printWriter.printf("background stone " + i + " " + j + "%n");
               }
               else
               {
                  printWriter.printf("background dirt " + i + " " + j + "%n");
               }
               int chance2 = (int)(Math.random() * 100);
               if(chance2 <=12 && chance1 > 12 && (i != 0 | j != 0) && (i != 29 | j != 19) && (i != 29 | j != 18))
               {
                  printWriter.printf("mountain mountain_" + i + "_" + j + " "+ i + " "+ j + "%n");
               }
               int chance3 = (int)(Math.random() * 100);
               if(!(chance2 <=12) && !(chance1 <=12) && chance3 <=12 && (i != 0 | j != 0) && (i != 29 | j != 19) && (i != 29 | j != 18))
               {


                  printWriter.printf("gold gold_" + i + "_" + j + " " + i + " "+ j + "%n");

               }
            }
         }
         printWriter.printf("player 0 0" + "%n");
         printWriter.printf("terminator 29 19 250 250 %n");
         printWriter.printf("terminator_dog 29 18 750 250 %n");
         printWriter.close();
      }
      catch(IOException e)
      {
         System.out.print(e.getMessage());
      }
   }


   public void draw()
   {
      if(difficulty.equals("easy")) {
         if (world.getGoldCount() != 0) {
            long time = System.currentTimeMillis();
            if (time >= next_time) {
               this.scheduler.updateOnTime(time);
               next_time = time + TIMER_ACTION_PERIOD;
            }

            view.drawViewport();
         } else if ((world.getPlayer(imageStore).getGoldNum() > world.getDetective(imageStore).getGoldNum())) {
            System.out.println(world.getPlayer(imageStore).getGoldNum() + " | " + world.getDetective(imageStore).getGoldNum());
            System.out.println("You passed level one.");
            setupTwo();
         } else {
            System.out.println(world.getPlayer(imageStore).getGoldNum() + " | " + world.getDetective(imageStore).getGoldNum());
            System.out.println("Try level one again");
            setup();
         }
      }
      else if(difficulty.equals("medium"))
      {
         if(world.getGoldCount() != 0) {
            long time = System.currentTimeMillis();
            if (time >= next_time) {
               this.scheduler.updateOnTime(time);
               next_time = time + TIMER_ACTION_PERIOD;
            }

            view.drawViewport();
         }
         else if((world.getPlayer(imageStore).getGoldNum() > world.getFBI(imageStore).getGoldNum()))
         {
            System.out.println(world.getPlayer(imageStore).getGoldNum() + " | " + world.getFBI(imageStore).getGoldNum());
            System.out.println("You passed level two.");
            setupThree();
         }
         else
         {
            System.out.println(world.getPlayer(imageStore).getGoldNum() + " | " + world.getFBI(imageStore).getGoldNum());
            System.out.println("Try level two again");
            setupTwo();
         }
      }
      else if(difficulty.equals("hard"))
      {
         if(world.getGoldCount() != 0) {
            long time = System.currentTimeMillis();
            if (time >= next_time) {
               this.scheduler.updateOnTime(time);
               next_time = time + TIMER_ACTION_PERIOD;
            }

            view.drawViewport();
         }
         else if((world.getPlayer(imageStore).getGoldNum() > world.getTerminator(imageStore).getGoldNum()))
         {
            System.out.println(world.getPlayer(imageStore).getGoldNum() + " | " + world.getTerminator(imageStore).getGoldNum());
            System.out.println("You passed level three! You win!" + ":" + "Game over.");
            noLoop();
         }
         else
         {
            System.out.println(world.getPlayer(imageStore).getGoldNum() + " | " + world.getTerminator(imageStore).getGoldNum());
            System.out.println("Try level three again");
            setupThree();
         }
      }



   }

   public void keyPressed() {

      if (key == CODED) {
         int dx = 0;
         int dy = 0;

         switch (keyCode) {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }

         world.getPlayer(imageStore).shift(world, imageStore, dx, dy);

      }
      else
      {
         if(key == 32)
         {
            world.getPlayer(imageStore).attack(world, scheduler);
         }
      }
   }



   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }


   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.load(in, world);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities())
      {
         if(entity instanceof Entity_Action)
         {
            ((Entity_Action)entity).scheduleActions(scheduler, world, imageStore);
         }
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
