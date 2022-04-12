import java.util.*;
import java.awt.*;
import javax.swing.*;
public class GameObject
{
   protected int x_pos;//center
   protected int y_pos;
   protected Color object_color; 
   static int[][] gameBoard; 
   
   public GameObject(int x_pos_in, int y_pos_in, Color object_color_in)//used to set up every block
   {
      x_pos = x_pos_in;
      y_pos = y_pos_in;
      object_color = object_color_in;
   }
    
   public void draw (Graphics g)//takes in graphic from panel, draws every single square besides player
   {
      g.setColor(object_color);
      g.fillRect(y_pos - 12, x_pos - 12, 25,25);
   }
   
   public void drawPlayer (Graphics g)//draws player exact same way as regular block
   {
      g.setColor(object_color);
      g.fillRect(x_pos - 12, y_pos - 12, 25, 25);
   }
  
   public int getX()
   {
      return this.y_pos;
   }
   
   public int getY()
   {
      return this.x_pos;
   }
     
   public Color getColor()
   {
      return this.object_color;
   }
   
  
}