import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class Block extends GameObject//doesnt do much, mostly just for getting values and instantiating objects
{ 
   public Block(int x_in, int y_in, Color c)
   {
      super(x_in,y_in,c);
   }
   public int getX()
   {
      return this.y_pos;
   }
   
   public int getY()
   {
      return this.x_pos;
   }
}