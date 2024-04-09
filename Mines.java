import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import java.util.Random;

public class Mines extends DrawableObject
{
   //creating a new random number generator
   Random numGenerator = new Random();
   
   public Mines(float x, float y)
   {
      super(x,y);
   }
   
   public void drawMe(float x, float y, GraphicsContext gc)
   {
       float randnum = numGenerator.nextFloat();
       gc.setFill(Color.PINK.interpolate(Color.BLUE,randnum));
       gc.fillOval(x,y,12,12);
   }
}