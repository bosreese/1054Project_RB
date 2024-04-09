import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.*;
import javafx.application.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.net.*;
import javafx.geometry.*;
import java.lang.Object;
import java.io.FileOutputStream;
import java.io.PrintWriter;


public class Main extends Application
{
   FlowPane fp; //creating root flowpane
   
   Canvas theCanvas = new Canvas(600,600); //creating the canvas everything takes place on
   
   //creating the player
   Player thePlayer = new Player(300,300);
   Player invisibleplayer = new Player(300,300); //creating a seperate invisble player that stays at (300,300) to calculate distance from
   
   //creating a mine drawable object
   Mines m;
   Mines a = new Mines(-1000,-1000);
   //creating an array for mines
   ArrayList<Mines> allmines = new ArrayList<Mines>();
   
   //adding the score label to the background
   Label score = new Label("Score is: " + thePlayer.distance(thePlayer));
   
   //creating a seperate timer for each a,w,s,d key and a generic timer
   float timera = 0;
   float timerw = 0;
   float timers = 0;
   float timerd = 0;
   
   //creating boolean for timer's of each key
   boolean countUpA = false;
   boolean countUpS = false;
   boolean countUpW = false;
   boolean countUpD = false;
   
   //creating a new animation 
   AnimationTimer at;
   
   //creating an inital highscore
   double highscore = 0;
   
   //creating a counter to stop the movement of the player once a crash takes place
   int counter = 0;
   
   //creating random number
   Random randNum = new Random();
   
   //creating a decimal format
   DecimalFormat format1 = new DecimalFormat("#0");


   //starting values of x,y for mines creating
   int xpos = 150;
   int ypos = 150;
   
   double highestscore = 0;  //creating a double to store the highestscore

   public void start(Stage stage)
   {
   
      fp = new FlowPane();
      fp.getChildren().add(theCanvas);
      gc = theCanvas.getGraphicsContext2D();
      
      
      //setting up keybaord settings
      fp.setOnKeyPressed(new KeyListenerDown());
      fp.setOnKeyReleased(new KeyListenerUp());
      
      //setting the animation handler
      at = new AnimationHandler();
      at.start();

      Scene scene = new Scene(fp, 600, 600);
      stage.setScene(scene);
      stage.setTitle("Project :)");
      stage.show();
      
      fp.requestFocus();
   }
   
   GraphicsContext gc;
   
   
   
   Image background = new Image("stars.png");
   Image overlay = new Image("starsoverlay.png");
   Random backgroundRand = new Random();
   //this piece of code doesn't need to be modified
   public void drawBackground(float playerx, float playery, GraphicsContext gc)
   {
     
	  //re-scale player position to make the background move slower. 
      playerx*=.1;
      playery*=.1;
   
	//figuring out the tile's position.
      float x = (playerx) / 400;
      float y = (playery) / 400;
      
      int xi = (int) x;
      int yi = (int) y;
      
	  //draw a certain amount of the tiled images
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(background,-playerx+i*400,-playery+j*400);
         }
      }
      
	  //below repeats with an overlay image
      playerx*=2f;
      playery*=2f;
   
      x = (playerx) / 400;
      y = (playery) / 400;
      
      xi = (int) x;
      yi = (int) y;
      
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(overlay,-playerx+i*400,-playery+j*400);
         }
      }
   }
   

   public class AnimationHandler extends AnimationTimer
   {
      public void handle(long currentTimeInNanoSeconds) 
      {
         gc.clearRect(0,0,600,600);

         //USE THIS CALL ONCE YOU HAVE A PLAYER
         drawBackground(thePlayer.getX(),thePlayer.getY(),gc); 
         
	      //example calls of draw - this should be the player's call for draw
         thePlayer.draw(300,300,gc,true); //all other objects will use false in the parameter.
         
         //example call of a draw where m is a non-player object. Note that you are passing the player's position in and not m's position.
         //m.draw(thePlayer.getX(),thePlayer.getY(),gc,false);
         
         
         
         int cgridx = ((int)thePlayer.getX())/100; //getting the position of thePlayer X position
         int cgridy = ((int)thePlayer.getY())/100; //getting the position of thePlayer Y position
         
         
         if(cgridx != xpos || cgridy != ypos)
         {
            ypos = cgridy;
            xpos = cgridx;
            
            
            //CREATING MINES FOR THE TOP ROW
            for(int i=0;i<10;i++)
            {
               //finding ydistance
               int ydistancePart1 = (ypos-5);
               int ydistanceFinal = Math.abs(ydistancePart1-3);
               
               //finding xdistance
               int xdistancePart1 = Math.abs(i-5);
               int xdistancePart2 = (xpos-3);
               int xdistanceFinal;
               if(i<= 5)
               {
                  xdistanceFinal = xdistancePart1 - xdistancePart2;
               }
               else
               {
                  xdistanceFinal = Math.abs(xdistancePart1 + xdistancePart2);
               }
               
               //pythagorean theorem to calculate how total distance from 3,3 to use to for how many possible mines to create
               int distanceFirst = (int)Math.sqrt(((xdistanceFinal)*(xdistanceFinal))+((ydistanceFinal)*(ydistanceFinal)));
               int distanceFinal = distanceFirst/10;
               
               //finding actual pixel position of boxes
               int pixelXposition = (xpos+(i-5))*100;
               int pixelYposition = (ypos-5)*100;
               
               for(int j=0; j<distanceFinal; j++)
               {
                  int num = randNum.nextInt(10);
                  if(num <= 2)
                  {
                     double randxnumposition = pixelXposition - randNum.nextInt(1000);
                     double randynumposition = pixelYposition - randNum.nextInt(1000);
                     
                     //creating a mine 
                     Mines m = new Mines((float)randxnumposition,(float)randynumposition);
                     
                     //adding all the mines to an arraylist
                     allmines.add(m);   
                  }
               }
          } 
          
          //CREATING MINES FOR THE RIGHT ROW
          for(int i=0;i<10;i++)
          {
               //finding ydistance
               int ydistancePart1 = Math.abs(i-5);
               int ydistancePart2 = (ypos-3);
               int ydistanceFinal;
               if(i<= 5)
               {
                  ydistanceFinal = Math.abs(ydistancePart1 - ydistancePart2);
               }
               else
               {
                  ydistanceFinal = Math.abs(ydistancePart1 + ydistancePart2);
               }
               
               //finding xdistance
               int xdistanceFinal= Math.abs((xpos+4)-3);
               
               //pythagorean theorem to calculate how total distance from 3,3 to use to for how many possible mines to create
               int distanceFirst = (int)Math.sqrt(((xdistanceFinal)*(xdistanceFinal))+((ydistanceFinal)*(ydistanceFinal)));
               int distanceFinal = distanceFirst/10;
               
               //finding actual pixel position of boxes
               int pixelYposition = (ypos+5)*100;
               int pixelXposition = (xpos+(i-5))*100;
               
               for(int j=0; j<distanceFinal; j++)
               {  
                  int num = randNum.nextInt(10);
                  if(num <= 2)
                  {
                     double randxnumposition = pixelXposition + randNum.nextInt(100);
                     double randynumposition = pixelYposition + randNum.nextInt(100);     
                     
                     //creating a mine 
                     Mines m = new Mines((float)randxnumposition,(float)randynumposition);
                     
                     //adding all the mines to an arraylist
                     allmines.add(m);   
                  }
               }
          }
            
          //CREATING MINES FOR THE RIGHT ROW
            for(int i=0;i<10;i++)
            {
                  //finding ydistance
                  int ydistancePart1 = Math.abs(i-5);
                  int ydistancePart2 = (ypos-3);
                  int ydistanceFinal;
                  if(i<= 5)
                  {
                     ydistanceFinal = Math.abs(ydistancePart1 - ydistancePart2);
                  }
                  else
                  {
                     ydistanceFinal = Math.abs(ydistancePart1 + ydistancePart2);
                  }
                  
                  //finding xdistance
                  int xdistanceFinal= Math.abs((xpos+4)-3);
                  
                  //pythagorean theorem to calculate how total distance from 3,3 to use to for how many possible mines to create
                  int distanceFirst = (int)Math.sqrt(((xdistanceFinal)*(xdistanceFinal))+((ydistanceFinal)*(ydistanceFinal)));
                  int distanceFinal = distanceFirst/10;
                  
                  //finding actual pixel position of boxes
                  int pixelYposition = (ypos+(i-5))*100;
                  int pixelXposition = (xpos+5)*100;
                  
                  for(int j=0; j<distanceFinal; j++)
                  {  
                     int num = randNum.nextInt(10);
                     if(num <= 2)
                     {
                        double randxnumposition = pixelXposition + randNum.nextInt(100);
                        double randynumposition = pixelYposition + randNum.nextInt(100);     
                        
                        //creating a mine 
                        Mines m = new Mines((float)randxnumposition,(float)randynumposition);
                        
                        //adding all the mines to an arraylist
                        allmines.add(m);   
                     }
                  }
            }
            
            //CREATING MINES FOR THE LEFT ROW
            for(int i=0;i<10;i++)
            {
                //finding ydistance
                  int ydistancePart1 = Math.abs(i-5);
                  int ydistancePart2 = (ypos-3);
                  int ydistanceFinal;
                  if(i<= 5)
                  {
                     ydistanceFinal = Math.abs(ydistancePart1 - ydistancePart2);
                  }
                  else
                  {
                     ydistanceFinal = Math.abs(ydistancePart1 + ydistancePart2);
                  }
                  
                  //finding xdistance
                  int xdistanceFinal= Math.abs((xpos+5)-3);
                  
                  //pythagorean theorem to calculate how total distance from 3,3 to use to for how many possible mines to create
                  int distanceFirst = (int)Math.sqrt(((xdistanceFinal)*(xdistanceFinal))+((ydistanceFinal)*(ydistanceFinal)));
                  int distanceFinal = distanceFirst/10;
                  
                  //finding actual pixel position of boxes
                  int pixelXposition = (xpos-5)*100;
                  int pixelYposition = (ypos+(i-5))*100;
                  
                  for(int j=0; j<distanceFinal; j++)
                  {  
                    int num = randNum.nextInt(10);
                    if(num <= 2)
                    {
                      double randxnumposition = pixelXposition + randNum.nextInt(100);
                      double randynumposition = pixelYposition + randNum.nextInt(100);
                        
                      //creating a mine 
                      Mines m = new Mines((float)randxnumposition,(float)randynumposition);
                        
                      //adding all the mines to an arraylist
                      allmines.add(m);   
                    }
                  }
            }
         } 


         if(countUpA == true)
         {
            timera = timera-0.1f;
            if(timera < -5)
            {
               timera = -5;
            }
         }
         if(countUpA == false)
         {
            timera = timera+0.025f;
            if(timera >= -0.25f)
            {
               timera = 0;
            }
         }
         if(countUpW == true)
         {
            timerw = timerw-0.1f;
            if(timerw < -5)
            {
               timerw = -5;
            }
         }
         if(countUpW == false)
         {
            timerw = timerw+0.025f;
            if(timerw >= -0.25f)
            {
               timerw = 0;
            }
         }
         if(countUpS == true)
         {
            timers+=0.1f;
            if(timers > 5)
            {
               timers = 5;
            }
         }
         if(countUpS == false)
         {
            timers = timers-0.025f;
            if(timers <= .25f)
            {
               timers = 0;
            }
         }
         if(countUpD == true)
         {
            timerd+=0.1f;
            if(timerd > 5)
            {
               timerd = 5;
            }
         }
         if(countUpD == false)
         {
            timerd = timerd-0.025f;
            if(timerd <= .25f)
            {
               timerd = 0;
            }
         }
         
         if(counter == 0)
         {
         thePlayer.act(timera, timerw, timers, timerd); //sending the forces to act method in drawable object 
         }
         else
         {
            int trash = 0;
         }
                  
         //calculating and printing out what the score is in pink
         double score = thePlayer.distance(invisibleplayer); //calculating score between thePlayer and m who just stays at (300,300)
         gc.setFill(Color.PINK);
         
         if(score > highscore)
         {
            highscore = score;
         } 
         if(score > highestscore)
         {
            highestscore = score;
            gc.fillText("Highscore: " + format1.format(highestscore-1), 20, 40);
         }
          
         gc.fillText("Score is: " + (int)highscore, 20,20); //putting the high score on the canvas
         
         try
         {
            Scanner readFile = new Scanner(new File("Highscore.txt"));
                      
            while(readFile.hasNextDouble())
            {
               
               double scaninscore = readFile.nextDouble();
              
               if(scaninscore > highestscore)
               {
                 highestscore = scaninscore;
               }  
            }
            
            gc.fillText("Highscore: " + format1.format(highestscore), 20, 40);
         }
         catch(FileNotFoundException fnfe)
         {
            int trash = 0;      
            gc.fillText("Highscore: "  , 20, 40);    
         }
         
         //putting the mines on the canvas 
         for(int k=0; k<allmines.size();k++)
         {
            m = allmines.get(k);
                  
            //removing mines from arraylist if they are greater than 800 away
            if(Math.abs(thePlayer.getX()) - Math.abs(m.getX()) > 800 || Math.abs(thePlayer.getY()) - Math.abs(m.getY()) > 800)
            {
                 allmines.remove(k);
            }
            else
            {  
                  m.draw(thePlayer.getX(),thePlayer.getY(),gc,false);
            }
         } 
        //checking to see if you hit a mine
        for(int i=0; i<allmines.size();i++)
        {
            m = allmines.get(i);
            double hitmine = thePlayer.distance(m);
            if(hitmine <= 20)
            {
               counter = 1;
               try
               {
                  
                  //creating an output file
                  FileOutputStream fos = new FileOutputStream("HighScore.txt", true);
                  
                  PrintWriter pw = new PrintWriter(fos);
                  
                  //writing into new file
                  pw.println(highscore);

                  pw.close();
                  
                  
               
               }
               catch(FileNotFoundException fnfe)
               {
                  System.out.println("File is not found!");
               }
            }
        }
     }
   }
   
   public class KeyListenerDown implements EventHandler<KeyEvent>  //direction of the object
   {
      public void handle(KeyEvent event) 
      {
          if (event.getCode() == KeyCode.A) //do this if A is pressed
          {
            countUpA = true; //changing the timer for A to countup
          }
          if (event.getCode() == KeyCode.W) //do this if W is pressed
          {
            countUpW = true; //changing the timer for W to countup
          }
          if (event.getCode() == KeyCode.S) //do this if S is pressed
          {
            countUpS = true; //changing the timer for S to countup
          }
          if (event.getCode() == KeyCode.D) //do this if D is pressed
          {
            countUpD = true; //changing the timer for D to countup
          }
      }
   }
   
    public class KeyListenerUp implements EventHandler<KeyEvent>  //direction of the object
   {
      public void handle(KeyEvent event) 
      {
          if (event.getCode() == KeyCode.A) //do this if A is lifted
          {
             countUpA = false; //changing the timer for W to countdown
          }
          if (event.getCode() == KeyCode.W) //do this if W is lifted
          {
             countUpW = false; //changing the timer for W to countdown
          }
          if (event.getCode() == KeyCode.S) //do this if S is lifted
          {
             countUpS = false; //changing the timer for S to countdown
          }
          if (event.getCode() == KeyCode.D) //do this if D is lifted
          {
             countUpD = false; //changing the timer for D to countdown
          }
      }
   }

      

   public static void main(String[] args)
   {
      launch(args);
   }
}

