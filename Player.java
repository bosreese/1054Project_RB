import javafx.scene.paint.*;
import javafx.scene.canvas.*;


public class Player extends DrawableObject
{
	//takes in its position
   public Player(float x, float y)
   {
      super(x,y);
   }
   //draws itself at the passed in x and y.
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      gc.setFill(Color.PINK);
      gc.fillOval(x-14,y-14,35,35);
      gc.setFill(Color.GREEN);
      gc.fillOval(x-4,y-4,15,15);
      
   }
}
