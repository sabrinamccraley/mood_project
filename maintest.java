public class maintest
{
   public static void main(String [] args)
   {
      ProjectPanel test = new ProjectPanel();
      for(int i=0; i<test.down; i++)
      {
            for(int j=0; j<test.over; j++)
            {
               System.out.print(test.mapArray[i][j]);
            }
            System.out.println();
      }
   }
}