import TSim.*;
import java.util.concurrent.*;

public class Lab1 {

  public Lab1(int speed1, int speed2) {
    TSimInterface tsi = TSimInterface.getInstance();

    try {
      tsi.setSpeed(1,speed1);
      tsi.setSpeed(2,speed2);
<<<<<<< HEAD
      tsi.setSwitch(17, 7,0x02);
      tsi.setSwitch(15, 9,0x02);
      tsi.setSwitch(3, 11,0x02);
      
=======
>>>>>>> a2fdcf9 (hej)
    }
    catch (CommandException e) {
      e.printStackTrace();    // or only e.getMessage() for the error
      System.exit(1);
    }
  }
}

<<<<<<< HEAD
class shared{
  static int counter = 0;
}

//Threads för tågen
class Train implements Runnable{
  public void run(){
    System.out.println("Running");
    

  }
}
=======
>>>>>>> a2fdcf9 (hej)
