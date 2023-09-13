import TSim.*;
import java.util.concurrent.Semaphore;
import java.lang.Math;

public class Lab1 {

  public Lab1(int speed1, int speed2) {
    TSimInterface tsi = TSimInterface.getInstance();
    
    Semaphore crossing = new Semaphore(1);
    Semaphore stn2_over = new Semaphore(1);
    Semaphore stn2_under = new Semaphore(1);
    Semaphore curve_right = new Semaphore(1);
    Semaphore curve_left = new Semaphore(1);
    Semaphore straight1_over = new Semaphore(1);
    Semaphore straight1_under = new Semaphore(1);
    Semaphore straight2_over = new Semaphore(1);
    Semaphore straight2_under = new Semaphore(1);
      
  class Train implements Runnable {
    TSimInterface tsi;
    int speed;
    int id; // 1 för övre tåget, 2 för det undre
    boolean dir; // TRUE om tåg kör nedåt, FALSE om de kör uppåt
    //SWITCHAR: true om switchDir==right, false om switchDir==left
    boolean sw1; //switch längst upp till höger
    boolean sw2; //switch mitten höger
    boolean sw3; //switch mitten vänster
    boolean sw4; //switch längst ner vänster
    
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
      sw1 = false;
      sw2 = false;
      sw3 = false;
      sw4 = false;

        while(true){ //längst upp
          SensorEvent sens;
          sens = tsi.getSensor(this.id);
          int xPos = sens.getXpos();
          int yPos = sens.getYpos();
          int stat = sens.getStatus();         
          
          //switch 4
          //B
          if(xPos==6 && yPos==5 && stat==0x01){
            if(dir==true){
              tsi.setSpeed(id, 0);
              crossing.acquire();
              tsi.setSpeed(id, speed);
            }
            else{
              crossing.release();
            }
          }

          //BB
          if(xPos==16 && yPos==3 && stat==0x01){
            tsi.setSpeed(id,0);
            Thread.sleep(2000);
              speed = - this.speed;
              dir = !this.dir;
              tsi.setSpeed(id,speed);
          }

          //C
          if(xPos==10 && yPos==5 && stat==0x01){
            if(dir==true){
              tsi.setSpeed(id, 0);
              crossing.acquire();
              tsi.setSpeed(id, speed);
            }
            else{
              crossing.release();
            }
          }

          //CC
          if(xPos==16 && yPos==5 && stat==0x01){
            tsi.setSpeed(id,0);
            Thread.sleep(2000);
              speed = - this.speed;
              dir = !this.dir;
              tsi.setSpeed(id,speed);
          }
          
          //D
          if(xPos==11 && yPos==7 && stat==0x01){
            if(dir==false){
              tsi.setSpeed(id, 0);
              crossing.acquire();
              tsi.setSpeed(id, speed);
            }
            else{
              crossing.release();
            }
          }

          //E
          if(xPos==11 && yPos==8 && stat==0x01){
            if(dir==false){
              tsi.setSpeed(id, 0);
              crossing.acquire();
              tsi.setSpeed(id, speed);
            }
            else{
              crossing.release();
            }
          }

          //G
          if(xPos==14 && yPos==7 && stat==0x01){
            if(dir==true){  
              tsi.setSpeed(id, 0);            
              curve_right.acquire();
              tsi.setSwitch(17,7,0x02);
              sw1 = true;
              tsi.setSpeed(id, speed);                                     
            }
            else{
              curve_right.release();
            }
          }

          //H
          if(xPos==19 && yPos==8 && stat==0x01){
            if(dir==false){
              if(straight1_over.tryAcquire()){
                tsi.setSwitch(17, 7, 0x02);
                sw1 = true;
              }
              else{
                straight1_under.acquire();
                tsi.setSwitch(17, 7, 0x01);
                sw1 = false;                
              }
            }
            else{
              if(sw1){
                straight1_over.release();
              }
              else{
                straight1_under.release();
              }
            }
          }

          //I
          if(xPos==14 && yPos==8 && stat==0x01){
            if(dir==true){
              tsi.setSpeed(id,0);
              curve_right.acquire();
              tsi.setSwitch(17,7,0x01);
              sw1 = false;
              tsi.setSpeed(id, speed);                              
            }
            else{
              curve_right.release();
            }
          }

          //J
          if(xPos==17 && yPos==9 && stat==0x01){
            if(dir==true){
              if(straight2_over.tryAcquire()){
                tsi.setSwitch(15, 9, 0x02);
                sw2 = true;                
              }
              else{
                straight2_under.acquire();
                tsi.setSwitch(15, 9, 0x01);
                sw2 = false;                
              }
            }
            else{
              if(sw2){
                straight2_over.release();
              }
              else{
                straight2_under.release();
              }
            }
          }

          //K
          if(xPos==12 && yPos==9 && stat==0x01){
            if(dir==false){
              tsi.setSpeed(id,0);
              curve_right.acquire();
              tsi.setSwitch(15,9,0x02);
              sw2 = true; 
              tsi.setSpeed(id, speed);                              
            }
            else{
              curve_right.release();
            }
          }

          //L
          if(xPos==12 && yPos==10 && stat==0x01){
            if(dir==false){
              tsi.setSpeed(id,0);
              curve_right.acquire();
              tsi.setSwitch(15,9,0x01);
              sw2 = false; 
              tsi.setSpeed(id, speed);                             
            }
            else{
              curve_right.release();
            }
          }

          //M
          if(xPos==7 && yPos==9 && stat==0x01){
            if(dir==true){
              tsi.setSpeed(id,0); 
              curve_left.acquire();
              tsi.setSwitch(4,9,0x01);
              sw3 = false; 
              tsi.setSpeed(id, speed);                              
            }
            else{
              curve_left.release();
            }
          }

          //N
          if(xPos==7 && yPos==10 && stat==0x01){
            if(dir==true){
              tsi.setSpeed(id,0);
              curve_left.acquire();
              tsi.setSwitch(4,9,0x02);
              sw3 = true;
              tsi.setSpeed(id, speed);
              straight2_under.release();                
            }
            else{
              curve_left.release();
            }
          }

          //O
          if(xPos==2 && yPos==9 && stat==0x01){
            if(dir==false){
              if(straight2_over.tryAcquire()){
                tsi.setSwitch(4, 9, 0x01);
                sw3 = false;            
              }
              else{
                straight2_under.acquire();
                tsi.setSwitch(4, 9, 0x02);
                sw3 = true;              
              }
            }
            else if(dir==true){
              if(!sw3){
                straight2_over.release();
              }
              else{
                straight2_under.release();
              }              
            }
          }
          
          //P
          if(xPos==1 && yPos==11 && stat==0x01){
            if(dir==true){              
              if(stn2_over.tryAcquire()){
                tsi.setSwitch(3, 11, 0x01);
                sw4 = false;              
              }
                else{
                stn2_under.acquire();
                tsi.setSwitch(3,11,0x02);
                sw4 = true;                 
              }
            }
            else{
              if(!sw4){
                stn2_over.release();                
              }
              else{
                stn2_under.release();
              }
            } 
          }

          //Q
          if(xPos==6 && yPos==11 && stat==0x01){
            if(dir==false){
              tsi.setSpeed(id,0);
              curve_left.acquire();
              tsi.setSwitch(3,11,0x01);
              sw4 = false;
              tsi.setSpeed(id, speed);              
            }
            else{
              curve_left.release();
            }
          }

          //QQ
          if(xPos==16 && yPos==11 && stat==0x01){
            tsi.setSpeed(id,0);
            Thread.sleep(2000);
              speed = - this.speed;
              dir = !this.dir;
              tsi.setSpeed(id,speed);
          }

          //R
          if(xPos==4 && yPos==13 && stat==0x01){
            if(dir==false){
              tsi.setSpeed(id,0);
              curve_left.acquire();
              tsi.setSwitch(3,11,0x02);
              sw4 = true;
              tsi.setSpeed(id,speed);              
            }
            else{
              curve_left.release();
            }
          }      
          
          //RR
          if(xPos==16 && yPos==13 && stat==0x01){
            tsi.setSpeed(id,0);
            Thread.sleep(2000);
              speed = - this.speed;
              dir = !this.dir;
              tsi.setSpeed(id,speed);
          }
        }

      } catch (Exception e) {
        // TODO: handle exception
      }
    }
  }
  Thread t1 = new Thread(new Train(tsi, speed1, true, 1));
  Thread t2 = new Thread(new Train(tsi, speed2, false, 2));
  t1.start();
  t2.start();
  }
}