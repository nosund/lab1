import TSim.*;
import java.util.concurrent.Semaphore;
import java.lang.Math;


public class Lab1 {

  public Lab1(int speed1, int speed2) {
    TSimInterface tsi = TSimInterface.getInstance();
    //Train t1 = new Train(tsi,speed1,true,1);
    //Train t2 = new Train(tsi,speed2,false,2);
    Thread t1 = new Thread(new Train(tsi,speed1,true,1));
    Thread t2 = new Thread(new Train(tsi,speed2,false,2));

    try {
      //tsi.setSpeed(2, speed2);
      tsi.setSwitch(15, 9, 0x02);
      tsi.setSwitch(4, 9, 0x01);
      tsi.setSwitch(17, 7, 0x02);
      
    } catch (Exception e) {
      // TODO: handle exception
    }
    t1.start();
    t2.start();
    

  }

  class Train implements Runnable{
  TSimInterface tsi;
  int speed;
  int id; //1 för övre tåget, 2 för det undre
  boolean dir; //TRUE om tåg kör nedåt, FALSE om de kör uppåt
  Semaphore stn1_over = new Semaphore(0);
  Semaphore stn1_under = new Semaphore(1);
  Semaphore stn2_over = new Semaphore(0);
  Semaphore stn2_under = new Semaphore(1);
  Semaphore curve_right = new Semaphore(1);
  Semaphore curve_left = new Semaphore(1);
  Semaphore straight1_over = new Semaphore(1);
  Semaphore straight1_under = new Semaphore(1);
  Semaphore straight2_over = new Semaphore(1);
  Semaphore straight2_under = new Semaphore(1);

    Train(TSimInterface tsi, int speed, boolean dir, int id){
      this.tsi = tsi;
      this.speed = speed;
      this.id = id;
      this.dir = dir;
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
        try {
          while(true){ //längst upp
            //tågstation 1, övre spåret
            if(xPos==6 & yPos==6 & stat==0x01){
              if(dir==true){
                //Thread.sleep(1000 + (20 * Math.abs(speed)));
                //tsi.setSpeed(id,0);                
              }
              else{
                stn1_over.release();
              }

            }
            //switch 4
            //Q
            if(xPos==5 & yPos==11 & stat==0x01){
              if(dir==false){
                curve_left.acquire();
                tsi.setSwitch(3,11,0x01);
                stn2_over.release();
              }
              else{
                Thread.sleep(1000 + (20 * Math.abs(speed)));
                tsi.setSpeed(id,0);
                Thread.sleep(1000 + (20 * Math.abs(speed)));
                speed = - this.speed;
                tsi.setSpeed(id,speed);
              }
            }
            //R
            if(xPos==3 & yPos==11 & stat==0x01){
              if(dir==false){
                curve_left.acquire();
                tsi.setSwitch(3,11,0x02);
                stn2_under.release();
              }
              else{
                Thread.sleep(1000 + (20 * Math.abs(speed)));
                tsi.setSpeed(id,0);
                Thread.sleep(1000 + (20 * Math.abs(speed)));
                speed = - this.speed;
                tsi.setSpeed(id,speed);
              }
            }
            //P
            if(xPos==1 & yPos==11 & stat==0x01){
              if(dir==true){
                tsi.setSwitch(3, 11, 0x02);
                /*if(stn2_over.tryAcquire()){
                  tsi.setSwitch(3, 11, 0x02);
                  curve_left.release();

                }
                else{
                  stn2_under.acquire();
                  tsi.setSwitch(3,11,0x01);
                  curve_left.release(); 
                
                }*/
              }
              
            }

          }
        } catch (Exception e) {
          // TODO: handle exception
        }

        

        } 
      catch (Exception e) {
        // TODO: handle exception
      }
    }
  }
}


