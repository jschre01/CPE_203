final class Viewport
{
   private int row;
   private int col;
   private int numRows;
   private int numCols;

   public Viewport(int numRows, int numCols)
   {
      this.numRows = numRows;
      this.numCols = numCols;
   }

   public int getRow()
   {
      return this.row;
   }

   public int getCol()
   {
      return this.col;
   }

   public int getNumRows()
   {
      return this.numRows;
   }

   public int getNumCols()
   {
      return this.numCols;
   }
   public Point viewportToWorld(int col, int row)
   {
      return new Point(col + this.col, row + this.row);
   }

   public Point worldToViewport(int col, int row)
   {
      return new Point(col - this.col, row - this.row);
   }

   public void shift(int col, int row)
   {
      this.col = col;
      this.row = row;
   }

   public boolean contains(Point pos)
   {
      return pos.y >= this.getRow() && pos.y < this.getRow() + this.getNumRows() &&
              pos.x >= this.getCol() && pos.x < this.getCol() + this.getNumCols();
   }



}
