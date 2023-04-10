import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;



final class Point
{
   public final int x;
   public final int y;




   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }



    public Optional<Entity> nearestEntity(List<Entity> entities)
    {
        if (entities.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            Entity nearest = entities.get(0);
            int nearestDistance = this.distanceSquared(nearest.getPosition());

            for (Entity other : entities)
            {
                int otherDistance = this.distanceSquared(other.getPosition());

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public int distanceSquared(Point p1)
    {
        int deltaX = this.x - p1.x;
        int deltaY = this.y - p1.y;

        return deltaX * deltaX + deltaY * deltaY;
    }


    public boolean adjacent(Point p1)
    {
        return (this.x == p1.x && Math.abs(this.y - p1.y) == 1) ||
                (this.y == p1.y && Math.abs(this.x - p1.x) == 1);
    }

}
