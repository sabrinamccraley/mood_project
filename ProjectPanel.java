/*Dr Mood please have mercy on me
*/

import java.util.Scanner;//import util stuff seperately for timer's sa ke
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import java.io.*;//for file
import java.awt.event.*;//for action listener, timer listener, etc


public class ProjectPanel extends JPanel
{
   int[][] mapArray;//holds map
   private int player_startx;//holds x and y of Mr. pink block
   private int player_starty;
   private ArrayList<ArrayList<GameObject>> blocks = new ArrayList < ArrayList <GameObject>>();//y-axis of map
   private ArrayList<ArrayList<Block>> blockyBlock = new ArrayList <ArrayList <Block>>();//x-axis of map
   Player myGuy;//the homie
   private int timeEventCount;//used in timer class for "every ___ seconds"
   private int timeEventCount_jump = 1;
   static int N = 1;//something with gravity
   static int max_low;
   static int max_right;//I have no idea when I thought up these two, but the program doesn't work without them and I don't feel like searching for their purpose (much like my own)
   
   int down;//self-explanatory
   int over;
   private boolean right;
   private boolean left;
   private boolean up = false;
   private boolean jumpIfTrue = false;
   private Color color;
   static Color dirt = new Color(51,34,0);
   private int jump;
   private int minusJump = 0;
   private int setJump = 0;
   private int threeSquaresAway_x;
   private int threeSquaresAway_y;
   private VictoryBlock victor;//the yellow homie
  
   public ProjectPanel()
   {  
      super();
      Timer t = new Timer(10, new TimeListener());//I can always count on him *ba-dum-tssss*
      t.start();
      addKeyListener(new KeyPressedReleased());//key listener for WAD
      setFocusable(true);//not needed but will keep here for future reference just in case
      try//try/catch for reading file
      {
         Scanner read = new Scanner(new File("map.txt"));
         player_startx = read.nextInt();
         player_starty = read.nextInt();
         myGuy = new Player(player_startx+24+12, player_starty+24+12, Color.PINK);//HE IS BORN!!!!
         down = read.nextInt();
         over = read.nextInt();
         mapArray = new int[down][over];//Map is transposed - all future x and y vals really refer to opposite val, so getX() returns y and getY() returns x. My bad. 
         int i = 0;
         int j = 0;
         for(i=0; i<down; i++)
         {
            for(j=0; j<over; j++)
            {
               mapArray[i][j] = read.nextInt();//filling array map to be printed
            }
         }
         max_low = ((i - 1) * 25) - 12;
         max_right = (j * 25) - 12;//these fellas apparently do something important
      }
      catch(FileNotFoundException fnfe)
      {
         System.out.println("File not found you fool.");
      }
      for(int i=0; i<down; i++)
      {
         ArrayList<GameObject> inner = new ArrayList<GameObject>();//made one for game object and one for block, ended up mostly using block one
         blocks.add(inner);
         ArrayList<Block> inner2 = new ArrayList<Block>();
         blockyBlock.add(inner2);
         
      }
      
      //create blocks
      for(int i=0; i<down; i++)//designating colors,x, y, bringing victor to life (much like Frankenstein to his monster; will he seek vengence??)
      {
         for(int j=0; j<over; j++)
         { 
            if(mapArray[i][j] == 0)
            {
               color = Color.BLUE;
            }
            else if(mapArray[i][j] == 1)
            {
               color = Color.GREEN;
            }
            else if(mapArray[i][j] == 2)
            {
               color = Color.YELLOW;
               victor = new VictoryBlock(i*25+12,j*25+12, color);
            }
            
            Block myBlock = new Block(i*25+12,j*25+12,color);//giving him personality 
            if(mapArray[i][j] == 0)
            {
               blocks.get(i).add(null); //if 0 make null in block array for spacing
               blockyBlock.get(i).add(null);
            }
            else
            {
               blocks.get(i).add(myBlock);//if 1 or victory block add to block array for collision and stuff
               blockyBlock.get(i).add(myBlock);
            }
            
         }
         setOpaque(true);//not necessary either, but again keeping for reference
         setPreferredSize(new Dimension(800,600));//panel size, fits blocks perfectly
         setBackground(Color.BLUE);//essentially, wherever void in array make blue
      }
      
      
   }
   
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);//obligatory call to super
      
      for(int i=0; i<blocks.size(); i++)
      {
         for(int j=0; j<blocks.get(i).size(); j++)
         {
           if(blocks.get(i).get(j) != null)//only if there is actually something there
           {
               blocks.get(i).get(j).draw(g);//here is where map got transposed, I think
           }
         }
      }
      myGuy.draw(g);//send g off to the far away land of GameObject and draw there
   }
 
   public boolean hitRoof_falseIfNot()//mathod for hit roof
   {
      boolean falseIfNotHitRoof = false;//assume false
      int  testBlock_X =  myGuy.getX() / 25;//divide by 25 to find overall block position of X and Y
      int  testBlock_Y = myGuy.getY() / 25 - 1;
      int rightBound_me = myGuy.getX() +12;//exact right and left bounds for testing
      int leftBound_me = myGuy.getX() -12;
      
      Block testBlock = null;//this block will be used to test if victory block


      
      if (blockyBlock.size() > 0)//alleviates random out of bounds error that was being annoying
       {
           if(blockyBlock.get(testBlock_Y).get(testBlock_X) != null)//only if green block there
           { 
 
               int rightBound_roof = blockyBlock.get(testBlock_Y).get(testBlock_X).getX()+12;//x and y of block in question
               int leftBound_roof = blockyBlock.get(testBlock_Y).get(testBlock_X).getX()-12 ;
                            
               if(leftBound_me <= rightBound_roof && rightBound_me >= leftBound_roof) //making sure that myGuy is in the span of block, doesn't test if goes over the corners
               {
                  falseIfNotHitRoof = myGuy.hitsRoof(blockyBlock.get(testBlock_Y).get(testBlock_X));//Test in Player class with block in question
               }
               
               testBlock =  blockyBlock.get(testBlock_Y).get(testBlock_X);//set potential victory block to one above for yellow test
               
            }
            
            if( blockyBlock.get(testBlock_Y).get(testBlock_X + 1) != null) //doing similar approach for neighboring blocks helps fix falsely determined free space issue
            {
                  int  rightBound_roof   = blockyBlock.get(testBlock_Y).get(testBlock_X +1).getX() +12;//get right and left bound of roof block for comparison
                  int  leftBound_roof   = blockyBlock.get(testBlock_Y).get(testBlock_X +1).getX() -12;

                  if(leftBound_me<=rightBound_roof && rightBound_me >= leftBound_roof)//condition
                  {
                     falseIfNotHitRoof = myGuy.hitsRoof(blockyBlock.get(testBlock_Y).get(testBlock_X +1));//in Player class
                  }
                  testBlock = blockyBlock.get(testBlock_Y).get(testBlock_X+1);//designate test block for yellow test
            }
                
               if(blockyBlock.get(testBlock_Y).get(testBlock_X -1) != null) ///do same for left side too
               {
                  int rightBound_roof   = blockyBlock.get(testBlock_Y).get(testBlock_X -1).getX() +11; //get right and left bound of roof block for comparison
                  int leftBound_roof    = blockyBlock.get(testBlock_Y).get(testBlock_X -1).getX() -12;
                  if(leftBound_me<=rightBound_roof && rightBound_me >= leftBound_roof)
                  {
                      falseIfNotHitRoof = myGuy.hitsRoof(blockyBlock.get(testBlock_Y).get(testBlock_X -1));
                  }
                   testBlock = blockyBlock.get(testBlock_Y).get(testBlock_X-1);
               }
                     
             if(falseIfNotHitRoof)//if it does hit roof, ensure not yellow (not victory block)
               {
                  if(testBlock.getColor() == Color.YELLOW)
                  {
                     repaint();//repaint makes it so no gap between me and victor
                     JOptionPane.showMessageDialog(null, "The real winners are the friends we made along the way", "meow", JOptionPane.QUESTION_MESSAGE);//the second most stupid message I could think of
                     System.exit(1);//aborts run of program
                  }
               }       
       }
      return falseIfNotHitRoof;
  }
 
   public boolean testOnGround_falseIfNot()//tests if on ground, no more falling
   {
      boolean falseIfNotOnGround = false;//assume not on ground
      int testBlock_X = myGuy.getX() / 25;
      int testBlock_Y = myGuy.getY() / 25 + 1;//plus one is for block under me
      
      int leftBound_me = myGuy.getX() -12;
      int rightBound_me = myGuy.getX() +12;
      int bottom_me = myGuy.getY() +12;
      
      Block testBlock = null;//for yellow victory block test
       if (blockyBlock.size() > 0)//avoids error
       {
           try//I kept getting an index out of bounds exception for no discernable reason, so here I tell Java to shut up
           {
               if(blockyBlock.get(testBlock_Y).get(testBlock_X) != null)
               {
                  int rightBound_floor = blockyBlock.get(testBlock_Y).get(testBlock_X).getX()+12;
                  int leftBound_floor = blockyBlock.get(testBlock_Y).get(testBlock_X).getX()-12;
                            
                  if(leftBound_me <= rightBound_floor && rightBound_me >= leftBound_floor) //bounds, self explanatory
                  {   
                     falseIfNotOnGround = myGuy.isOnGround(blockyBlock.get(testBlock_Y).get(testBlock_X));//in Player class
                  }
                  testBlock = blockyBlock.get(testBlock_Y).get(testBlock_X);//set test block accordingly                
               }
               else//if one directly below me is null (alleviates bound error from before)
               {        
                  if( blockyBlock.get(testBlock_Y).get(testBlock_X +1) != null)//test 1 to right
                  {
                        int rightBound_floor   = blockyBlock.get(testBlock_Y).get(testBlock_X +1).getX() +12; 
                        int leftBound_floor    = blockyBlock.get(testBlock_Y).get(testBlock_X +1).getX() -12;
                        int topBound_floor = blockyBlock.get(testBlock_Y).get(testBlock_X +1).getY() -12;
             
                        if(leftBound_me<=rightBound_floor && rightBound_me >= leftBound_floor && topBound_floor == bottom_me)//similar check of bounds, compensate for possible movement
                        {
                           falseIfNotOnGround = myGuy.isOnGround(blockyBlock.get(testBlock_Y).get(testBlock_X +1));    
                        }
                        testBlock = blockyBlock.get(testBlock_Y).get(testBlock_X+1);   
                   }
                   
                   if(blockyBlock.get(testBlock_Y).get(testBlock_X -1) != null)//test on left
                   {
                         int rightBound_floor   = blockyBlock.get(testBlock_Y).get(testBlock_X -1).getX() +11; 
                         int leftBound_floor    = blockyBlock.get(testBlock_Y).get(testBlock_X -1).getX() -12;
                         int topBound_floor = blockyBlock.get(testBlock_Y).get(testBlock_X -1).getY() -12;
                
                         if(leftBound_me<=rightBound_floor && rightBound_me >= leftBound_floor &&topBound_floor == bottom_me)
                         {
                            falseIfNotOnGround = myGuy.isOnGround(blockyBlock.get(testBlock_Y).get(testBlock_X -1));
                         }
                         testBlock = blockyBlock.get(testBlock_Y).get(testBlock_X-1);
                      }
                  } 

                 if(falseIfNotOnGround)//same victory block test
                   {
                     
                     if(testBlock!=null)
                     {
                        if(testBlock.getColor() == Color.YELLOW)
                         {
                            repaint();
                            JOptionPane.showMessageDialog(null, "The real winners are the friends we made along the way", "meow", JOptionPane.QUESTION_MESSAGE);
                            System.exit(1);
                        }
                     }
                   }           
            }
            catch(IndexOutOfBoundsException ioobe)
            {
               System.out.println("I, Java, am throwing a stupid random error for no reason again. Continue on.");
            }
        }    
        return falseIfNotOnGround;
   }
   
   public boolean testOnLeft_falseIfNot()//to allow left
   {
       boolean OnLeft = false;//assume can move left

       int myY = myGuy.getY()/25;
       int bottomOfGuy = myGuy.getY() - 12;
       int BlockOnLeft = myGuy.getX()/25 - 1;
       
       Block winnerBlockTest = null;
      
       if (blockyBlock.get(myY).get(BlockOnLeft) != null)//test here if not null regardless
        {  
           OnLeft = myGuy.isCollidingLeft(blockyBlock.get(myY).get(BlockOnLeft));//method in Player
           winnerBlockTest = blockyBlock.get(myY).get(BlockOnLeft);
        }  
        
        Block testBlock = blockyBlock.get(myY + 1).get(BlockOnLeft);  
        
       if (testOnGround_falseIfNot() == false && testBlock != null)//helps relieve a super duper bug where I could enter into blocks from certain angles. Only use if one above doesnt
       { 
          if(bottomOfGuy < testBlock.getY() +12)//if I am anywhere under the top of the one below
          {
               OnLeft = myGuy.isCollidingLeft(testBlock);
          }
          winnerBlockTest = testBlock;
       }
        
        if(OnLeft)//once again, the lovely copy-pasted yellow test
        {
           if(winnerBlockTest != null)
           {
               if(winnerBlockTest.getColor() == Color.YELLOW)
               {
                  JOptionPane.showMessageDialog(null, "The real winners are the friends we made along the way", "meow", JOptionPane.QUESTION_MESSAGE);
                  System.exit(1);
               }
            }
        }
        
        return OnLeft;     
   }
   
      public boolean testOnRight_falseIfNot()//mirror of above method, refer to above comments
      {
         boolean falseIfNotOnRight = false;
  
         int myY = myGuy.getY()/25; 
         int bottomOfGuy = myGuy.getY() - 12;
         int BlockOnRight = myGuy.getX()/25 + 1;
       
         Block winnerBlockTest = null;
       
         if (blockyBlock.get(myY).get(BlockOnRight) != null)
         {  
             falseIfNotOnRight = myGuy.isCollidingRight(blockyBlock.get(myY).get(BlockOnRight));
             winnerBlockTest = blockyBlock.get(myY).get(BlockOnRight);
         }  
        
         Block testBlock = blockyBlock.get(myY + 1).get(BlockOnRight);  
         if (testOnGround_falseIfNot() == false && testBlock != null)
         { 
            if(bottomOfGuy < testBlock.getY() +12)
            {
                 falseIfNotOnRight = myGuy.isCollidingRight(testBlock);
            }
            winnerBlockTest = testBlock;
         }
        
        if(falseIfNotOnRight)
        {
           if(winnerBlockTest != null)
           {
               if(winnerBlockTest.getColor() == Color.YELLOW)
               {
                  JOptionPane.showMessageDialog(null, "The real winners are the friends we made along the way", "meow", JOptionPane.QUESTION_MESSAGE);
                  System.exit(1);
               }
            }
        }
        
        return falseIfNotOnRight;
      } 
      
   public class KeyPressedReleased implements KeyListener //for holding down, letting up WASD
   {
      //3 abstract methods of class:
      public void keyTyped(KeyEvent e){}//not needed here
      public void keyReleased(KeyEvent e) //for when release key
      {
         if(e.getKeyCode() == KeyEvent.VK_D)
         {
            right = false;
         }
         if(e.getKeyCode() == KeyEvent.VK_A)
         {
            left = false;   
         }
         if(e.getKeyCode() == KeyEvent.VK_W)
         {
           up = false;
         }      
      }
      public void keyPressed(KeyEvent e)//for when key held down
      { 
         if(e.getKeyCode() == KeyEvent.VK_D)
         {
            right = true;  
         }
         if(e.getKeyCode() == KeyEvent.VK_A)
         {
               left = true;   
         }
         if(e.getKeyCode() == KeyEvent.VK_W)
         {
           if(testOnGround_falseIfNot() == true)//each time jump set variables back to how they were before jump
           {
              up = true;
              jump = 7;
              jumpIfTrue = true;          
              minusJump = 0;
           }      
         }
      }
    }

   public class TimeListener implements ActionListener//timer connected to this, fires every 10 milliseconds
   {
      public void actionPerformed(ActionEvent e)//abstract method of ActionListener class
      {

        timeEventCount++; //every 20 times increase N by 1
        timeEventCount_jump++;//similar concept, utilized by jump function instead
        
         if (myGuy == null) //kept getting null pointer exception, now Java just yaps at me instead, like a muzzled chihuahua
         {   
            System.out.println ("Error in myGuy");
            return;
         }
         
         if(testOnGround_falseIfNot() == false)//first must test to make sure not on ground
         {
            if(timeEventCount % 20 == 0)//only do it every 20 ticks of timer 
            {
              if(N<7)//increase N however often, use it to accelerate gravity
              {
                 N++; 
              }
            }
            
            for(int i=0; i<N; i++)
            {
               if(testOnGround_falseIfNot() == false && up == false && (myGuy.getY() - 1 < max_low))//I don't remember why I need this... too scared to find out
               {
                   myGuy.moveY(1);//move down
               } 
            }                    
         }
         else
         {    
           N = 1;//set N back, time event back even though doesn't really matter for % funct
           timeEventCount = 1;
         }         
     
         if(right)
         {
            if(testOnRight_falseIfNot() == false)//test then move, same with left but opposite direction
            {  
               myGuy.moveX(1);
            }
         }
         if(left)
         {
            if(testOnLeft_falseIfNot() == false)
            {
               myGuy.moveX(-1);
            }
         }
       
         if(jumpIfTrue)//test if not in air in W key pressed method
         {   
            if(timeEventCount_jump % 10 == 0)//every ten timer ticks decrease velocity of jump
            {
               if(minusJump<7)
               {
                  minusJump++; 
               }
            }
            for(int i=0; i<jump; i++)//this formula worked. Could explain why if I thought hard enough
            {   
              if( hitRoof_falseIfNot()==false)
              {
                myGuy.moveY(-1);
                jump = 7-minusJump;   
              }
              else
              {
                  jump = 0;//if hits roof, set jump back to 0 like when started
              }        
            }
            
            if(jump ==0)//then once jump hits 0, set boolean back
            {
               jumpIfTrue = false;                    
            }
         
            up=false;//set or else myGuy goes soaring   
         } 
         repaint();//repaint any updates then leave method  
      }
    }
   public static void main(String [] args)//setting up of frame and stuffs
   {
      JFrame frame = new JFrame("Project");
      frame.setSize(new Dimension(860,680));
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new FlowLayout());//all typical setting of frame stuff
      
      Container contents = frame.getContentPane();
      
      contents.setBackground(dirt);
      
      ProjectPanel gamePanel = new ProjectPanel();//all the magic encased in one little panel     
      
      contents.add(gamePanel);//open sesame!!!
      gamePanel.requestFocus();//(with permission of course)
   }
}
