import TSim.*;
import java.util.concurrent.Semaphore;

public class Lab1 {

  public Lab1(int speed1, int speed2) {
    TSimInterface tsi = TSimInterface.getInstance();
    Train t1 = new Train(tsi,speed1,0,1);
    Train t2 = new Train(tsi,speed2,1,2);
    while(true){
      t1.run();
      t2.run();
    }
    
  }

  class Train implements Runnable{
  TSimInterface tsi;
  int speed;
  int id;
  int dir;
  Semaphore sem;

    Train(TSimInterface tsi, int speed, int dir, int id){
      this.tsi = tsi;
      this.speed = speed;
      this.id = id;
      this.dir = dir;
      this.sem = sem;
    }

    public void run(){
      try {
        /*tsi.setSpeed(1,15);*/
        SensorEvent sens;
        tsi.setSpeed(this.id, this.speed);
        sens = tsi.getSensor(this.id);
        int xPos = sens.getXpos();
        int yPos = sens.getYpos();
        int stat = sens.getStatus();
        if(this.speed > 0 & this.id == 1){
          dir = 1;
        }
        else if(this.speed > 0 & this.id == 2){
          dir = 0;
        }
        //kommer från vänster
        else if(this.speed < 0 & this.id == 1){
          dir = 0;
        }
        else if(this.speed < 0 & this.id == 2){
          dir = 1;
        }
        //kommer från höger
        try {
          sem.acquire();

        } catch (Exception e) {
          // TODO: handle exception
        }
        if(xPos == 5 & yPos == 11 & stat == 0x01){ //s1
          tsi.setSwitch(3,11,0x01);
        }
        else if(xPos == 15 & yPos == 7 & stat == 0x01){ //sw1,s1
          tsi.setSwitch(17,7,0x02);
        }
        else if(xPos == 2 & yPos == 9 & stat == 0x01){ //s2
          tsi.setSwitch(4,9,0x01);
        }
        else if(xPos == 13 & yPos == 9 & stat == 0x01){
          tsi.setSwitch(15,9,0x02);
        }

        } 
      catch (Exception e) {
        // TODO: handle exception
      }
    }
  }
}

//tracks




/*public boolean getDir(trainId){
    if (speed1 < 0);
      return 0;
    
    elseif(speed1 > 0);
      return 1;
  
}
*/
//Semaphores
/* 
Semaphore track1 = new Semaphore(1);
Semaphore track2 = new Semaphore(1);
Semaphore track3 = new Semaphore(1);
Semaphore track4 = new Semaphore(1);
Semaphore track5 = new Semaphore(1);
Semaphore track6 = new Semaphore(1);
Semaphore track7 = new Semaphore(1);
Semaphore track8 = new Semaphore(1);
Semaphore track9 = new Semaphore(1);
Semaphore track10 = new Semaphore(1);

class shared{
  static int counter = 0;
}
boolean train1dir = 1
if (speed1 < 0){
  train1dir = 0
}elseif(speed1 > 0){
  traind1dir = 1
}
boolean train2dir = 1
if (speed2 < 0){
  train2dir = 1
}elseif(speed2 > 0){
  traind2dir = 0
}
//traindir = 1 är nedåt, traindir = 0 är uppåt oavsett trainId
//Threads för tågen
class Train extends Thread {
  public void main(String[] args){
    
    getSensor(1){
      if(home1 == active && train1dir){
        tsi.setSwitch(xpos,ypos,0x02);
      }
      if(home1 == active && !train1dir){
        Thread.sleep(1000 + (20 * |train_speed|))
      }
    }
    getSensor(2){
      if()
    }

  }
}
 */

