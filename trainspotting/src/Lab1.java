import TSim.*;
import java.util.concurrent.Semaphore;
import java.lang.Math;

public class Lab1 {

  public Lab1(int speed1, int speed2) {
    TSimInterface tsi = TSimInterface.getInstance();
    // Train t1 = new Train(tsi,speed1,true,1);
    // Train t2 = new Train(tsi,speed2,false,2);
    Thread t1 = new Thread(new Train(tsi, speed1, true, 1));
    Thread t2 = new Thread(new Train(tsi, speed2, false, 2));

    try {
      // tsi.setSpeed(2, speed2);
      /*
       * tsi.setSwitch(15, 9, 0x02);
       * tsi.setSwitch(4, 9, 0x01);
       * tsi.setSwitch(17, 7, 0x02);
       */
    } catch (Exception e) {
      // TODO: handle exception
    }

    t1.start();
    t2.start();
  }

  class Train implements Runnable {
    TSimInterface tsi;
    int speed;
    int id; // 1 för övre tåget, 2 för det undre
    boolean dir; // TRUE om tåg kör nedåt, FALSE om de kör uppåt
    
    Semaphore crossing = new Semaphore(1);
    Semaphore stn2_over = new Semaphore(1);
    Semaphore stn2_under = new Semaphore(1);
    Semaphore curve_right = new Semaphore(1);
    Semaphore curve_left = new Semaphore(1);
    Semaphore straight1_over = new Semaphore(1);
    Semaphore straight1_under = new Semaphore(1);
    Semaphore straight2_over = new Semaphore(1);
    Semaphore straight2_under = new Semaphore(1);

    Train(TSimInterface tsi, int speed, boolean dir, int id) {
      this.tsi = tsi;
      this.speed = speed;
      this.id = id;
      this.dir = dir;
    }

    public void run(){      
          
      try {
      tsi.setSpeed(id,speed);
      if(id==1){
        straight1_over.acquire();
      }
      else{
        stn2_over.acquire();
      }
      
      
        while(true){ //längst upp
          SensorEvent sens;
          sens = tsi.getSensor(this.id);
          int xPos = sens.getXpos();
          int yPos = sens.getYpos();
          int stat = sens.getStatus();
          
          
          //switch 4
          //Q
          if(xPos==5 & yPos==11 & stat==0x01){
            if(dir==false){
              tsi.setSpeed(id,0);
              curve_left.acquire();
              tsi.setSwitch(3,11,0x01);
              tsi.setSpeed(id, speed);
              
            }
            else{
              curve_left.release();
              Thread.sleep(1000 + (20 * Math.abs(speed)));
              tsi.setSpeed(id,0);
              Thread.sleep(1000 + (20 * Math.abs(speed)));
              speed = - this.speed;
              dir = !this.dir;
              tsi.setSpeed(id,speed);
            }
          }

          //R
          if(xPos==3 & yPos==13 & stat==0x01){
            if(dir==false){
              tsi.setSpeed(id,0);
              curve_left.acquire();
              tsi.setSwitch(3,11,0x02);
              tsi.setSpeed(id,speed);
              
            }
            else{
              curve_left.release();
              Thread.sleep(1000 + (20 * Math.abs(speed)));
              tsi.setSpeed(id,0);
              Thread.sleep(1000 + (20 * Math.abs(speed)));
              speed = - this.speed;
              dir = !this.dir;
              tsi.setSpeed(id,speed);
            }
          }

          //P
          if(xPos==1 & yPos==11 & stat==0x01){
            if(dir==true){              
              if(stn2_over.tryAcquire()){
                tsi.setSwitch(3, 11, 0x01);
                
              }
                else{
                stn2_under.acquire();
                tsi.setSwitch(3,11,0x02);
                 
              }
            }
            else{
              if(stn2_over.availablePermits()==0){
                stn2_over.release();                
              }
              else{
                stn2_under.release();
              }
            } 
          }

          //O
          if(xPos==2 & yPos==9 & stat==0x01){
            if(dir==false){
              if(straight2_over.tryAcquire()){
                tsi.setSwitch(4, 9, 0x01);                
              }
              else{
                straight2_under.acquire();
                tsi.setSwitch(4, 9, 0x02);                
              }
            }
            else if(dir==true){
              if(straight2_over.availablePermits()==0){
                straight2_over.release();
              }
              else{
                straight2_under.release();
              }
              
            }
          }

          //J
          if(xPos==17 & yPos==9 & stat==0x01){
            if(dir==true){
              if(straight2_over.tryAcquire()){
                tsi.setSwitch(15, 9, 0x02);
                
              }
              else{
                straight2_under.acquire();
                tsi.setSwitch(15, 9, 0x01);                
              }
            }
            else{
              if(straight2_over.availablePermits()==0){
                straight2_over.release();
              }
              else{
                straight2_under.release();
              }
            }
          }
          
          //H
          if(xPos==19 & yPos==8 & stat==0x01){
            if(dir==false){
              if(straight1_over.tryAcquire()){
                tsi.setSwitch(17, 7, 0x02);
                
              }
              else{
                straight1_under.acquire();
                tsi.setSwitch(17, 7, 0x01);
                
              }
            }
            else{
              if(straight1_over.availablePermits()==0){
                straight1_over.release();
              }
              else{
                straight1_under.release();
              }

            }
          }

          //M
          if(xPos==7 & yPos==9 & stat==0x01){
            if(dir==true){
              tsi.setSpeed(id,0); 
              curve_left.acquire();
              tsi.setSwitch(4,9,0x01);
              tsi.setSpeed(id, speed);
                              
            }
            else{
              curve_left.release();
            }
          }

          //N
          if(xPos==7 & yPos==10 & stat==0x01){
            if(dir==true){
              tsi.setSpeed(id,0);
              curve_left.acquire();
              tsi.setSwitch(4,9,0x02);                tsi.setSpeed(id, speed);
              straight2_under.release();                
            }
            else{
              curve_left.release();
            }
          }

          //K
          if(xPos==13 & yPos==9 & stat==0x01){
            if(dir==false){
              tsi.setSpeed(id,0);
              curve_right.acquire();
              tsi.setSwitch(15,9,0x02);
              tsi.setSpeed(id, speed);
                              
            }
            else{
              curve_right.release();
            }
          }

          //L
          if(xPos==13 & yPos==10 & stat==0x01){
            if(dir==false){
              tsi.setSpeed(id,0);
              curve_right.acquire();
              tsi.setSwitch(15,9,0x01);
              tsi.setSpeed(id, speed);
                             
            }
            else{
              curve_right.release();
            }
          }

          //G
          if(xPos==15 & yPos==7 & stat==0x01){
            if(dir==true){  
              tsi.setSpeed(id, 0);            
              curve_right.acquire();
              tsi.setSwitch(17,7,0x02);
              tsi.setSpeed(id, speed);                             
            }
            else{
              curve_right.release();
            }
          }

          //I
          if(xPos==16 & yPos==8 & stat==0x01){
            if(dir==true){
              tsi.setSpeed(id,0);
              curve_right.acquire();
              tsi.setSwitch(17,7,0x01);
              tsi.setSpeed(id, speed);                              
            }
            else{
              curve_right.release();
            }
          }

          //B
          if(xPos==6 & yPos==6 & stat==0x01){
            if(dir==true){
              tsi.setSpeed(id, 0);
              crossing.acquire();
              tsi.setSpeed(id, speed);
            }
            else{
              crossing.release();
              Thread.sleep(1000 + (20 * Math.abs(speed)));
              tsi.setSpeed(id,0);
              Thread.sleep(1000 + (20 * Math.abs(speed)));
              speed = - this.speed;
              dir = !this.dir;
              tsi.setSpeed(id,speed);
            }
          }

          //D
          if(xPos==10 & yPos==7 & stat==0x01){
            if(dir==false){
              tsi.setSpeed(id, 0);
              crossing.acquire();
              tsi.setSpeed(id, speed);
            }
            else{
              crossing.release();
            }

          }

          //C
          if(xPos==9 & yPos==5 & stat==0x01){
            if(dir==true){
              tsi.setSpeed(id, 0);
              crossing.acquire();
              tsi.setSpeed(id, speed);
            }
            else{
              crossing.release();
              Thread.sleep(1000 + (20 * Math.abs(speed)));
              tsi.setSpeed(id,0);
              Thread.sleep(1000 + (20 * Math.abs(speed)));
              speed = - this.speed;
              dir = !this.dir;
              tsi.setSpeed(id,speed);
            }
          }

          //E
          if(xPos==10 & yPos==7 & stat==0x01){
            if(dir==false){
              tsi.setSpeed(id, 0);
              crossing.acquire();
              tsi.setSpeed(id, speed);
            }
            else{
              crossing.release();
            }

          }
        }
      } catch (Exception e) {
          // TODO: handle exception
      }
    }
  }
}
