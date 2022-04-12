import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Player extends GameObject
{
  private boolean right;
  private boolean left;
   
   Player(int x_pos_in, int y_pos_in, Color c_in)
   {
      super(x_pos_in, y_pos_in, c_in);
   }
   
   public boolean isOnGround( Block  b )//simple comparison to test if on ground
   {
         boolean retval = false;
         int topOfBlock = b.x_pos - 12;
         int bottomOfMe = this.x_pos + 12;
       
         if(bottomOfMe == topOfBlock )
         {
            retval =  true;//on block
         }
        
      return retval;
   }
   
   public boolean hitsRoof(Block b)//if hits roof
   {
      int bottomOfBlock = b.x_pos+12;
      int topOfMe = this.x_pos -12;   
      boolean retval = false;
      
      if(topOfMe <= bottomOfBlock)
      {
         retval = true; //collision
      }
      return retval;
   }
  
   public boolean isCollidingLeft(Block b)
   {
      int b_rightOfBlock = (b.y_pos + 12);
      int m_leftOfMe = (y_pos - 12);
      boolean retval = false;
      
      if(b_rightOfBlock >= m_leftOfMe)
      {
         retval = true;
      }
      return retval;  
   }
   
   public boolean isCollidingRight(Block b)//test on right, very easy method
   {
      int b_leftOfBlock = (b.y_pos - 13);
      int m_rightOfMe = (y_pos + 12);  
      
      boolean retval = false;
      
      if(b_leftOfBlock <= m_rightOfMe)
      {
         retval = true;
      }
      return retval;
   }
   
   public void moveX(int plusOrMinus)//takes in +- 1, moves accordingly right or left
   { 
       y_pos += plusOrMinus;  
   }
   
   public void moveY(int plusOrMinus)//takes in +- 1, moves accordingly up or down
   {
      x_pos += plusOrMinus;
   }
   
   public int getX()
   {
      return y_pos;
   }
   
   public int getY()
   {
      return x_pos;
   }
   
   public void printMe ()
   {
     //System.out.println ("Myguy " + x_pos /25 + " " + y_pos /25);
   }
  
   
  
}