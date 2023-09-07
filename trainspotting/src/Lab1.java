import TSim.*;
import java.util.concurrent.Semaphore;

public class Lab1 {

  public Lab1(int speed1, int speed2) {
    TSimInterface tsi = TSimInterface.getInstance();

    try {
      tsi.setSpeed(1,speed1);
      tsi.setSpeed(2,speed2);
      tsi.setSwitch(17, 7,0x02);
      tsi.setSwitch(15, 9,0x02);
      tsi.setSwitch(3, 11,0x02);
      

    }
    catch (CommandException e) {
      e.printStackTrace();    // or only e.getMessage() for the error
      System.exit(1);
    }
  }
}
//Semaphores
Semaphore track1 = new Semaphore(1);
Semaphore track2 = new Semaphore(1);

class shared{
  static int counter = 0;
}

//Threads för tågen
class Train extends Thread {
  SensorEvent SenEve = TSim.TSimInterface.getSensor();

  public void main(String[] args) {
    
    Train train1 = new Train();
    train1.start();
  }
  public void run() {
    System.out.println("Running"); 
    }
  }
}

