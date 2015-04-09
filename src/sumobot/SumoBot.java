
package sumobot;

import com.pi4j.component.sensor.DistanceSensorChangeEvent;
import com.pi4j.component.sensor.DistanceSensorListener;
import com.pi4j.component.sensor.impl.DistanceSensorComponent;
import com.pi4j.gpio.extension.ads.ADS1015GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1015Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.io.gpio.*;
import com.pi4j.io.i2c.I2CBus;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dominick Sparks
 */

public class SumoBot {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    
    /**
     *  Global Variables (decrease memory usage...)
     */
    static GpioController gpio;
    // Define motor controller pins   
    static Pin input1 = RaspiPin.GPIO_00;
    static Pin input2 = RaspiPin.GPIO_02;
    static Pin input3 = RaspiPin.GPIO_03; 
    static Pin input4 = RaspiPin.GPIO_12;
    static MotorController motor;
    
    // Define program LED indicator
    static Pin programLed = RaspiPin.GPIO_26;
    
    // Define line sensor pins
    static Pin frontLeftLinePin = RaspiPin.GPIO_27;
    static Pin frontRightLinePin = RaspiPin.GPIO_24;
    static Pin rearLeftLinePin = RaspiPin.GPIO_25;
    static Pin rearRightLinePin = RaspiPin.GPIO_28;
    
    // Define line sensor objects
    static digitalSensor frontLeftLine;
    static digitalSensor frontRightLine;
    static digitalSensor rearLeftLine;
    static digitalSensor rearRightLine;
    
    // Define short range pins
    static Pin leftShortRangePin = RaspiPin.GPIO_04;
    static Pin rightShortRangePin = RaspiPin.GPIO_05;
    // Define short range sensor objects
    static digitalSensor leftShortRange;
    static digitalSensor rightShortRange;
    
    // Define long range sensor object
    static LongRangeDistanceSensor longRange;
    
    // Global vars to store sensor data 
    static byte lineSensorFlags;
    static byte shortSensorFlags;
    static double longRangeValue;
    static Random rand;
    static int count;
    static double[] vals;
    
    public static void main(String[] args) throws InterruptedException, IOException {
        
        System.out.println("Running...");
        // Initialize variables
        lineSensorFlags = 0x00;
        shortSensorFlags = 0x00;
        longRangeValue = 0;
        rand = new Random();
        
        // average long range sensor data
        count = 0;
        vals = new double[10];
        
        gpio = GpioFactory.getInstance();
        
        // Construct motor controller object
        motor = new MotorController(gpio, input4, input3, input2, input1);
        motor.stop();

        // Construct the line sensor objects
        frontLeftLine = new digitalSensor(0,"frontLeftLine",frontLeftLinePin,gpio);
        frontRightLine = new digitalSensor(0,"frontRightLine",frontRightLinePin,gpio);
        rearLeftLine = new digitalSensor(0,"rearLeftLine",rearLeftLinePin,gpio);
        rearRightLine = new digitalSensor(0,"rearRightLine",rearRightLinePin,gpio);
        
        // Construct the short range sensor objects
        leftShortRange = new digitalSensor(1,"leftShortRange",leftShortRangePin,gpio);
        rightShortRange = new digitalSensor(1,"rightShortRange",rightShortRangePin,gpio);
        
        // Construct the long range sensor object
        longRange = new LongRangeDistanceSensor(gpio);
        
        // Create gpio output to indicate program is running
        gpio.provisionDigitalOutputPin(programLed,PinState.HIGH);
        //System.out.println(lineSensorFlags);
        
        
        while(true){
            //System.out.println(frontLeftLine.getState() + " " + frontRightLine.getState());
            setLineSensorFlags();
            
            //System.out.println(lineSensorFlags);
            //System.out.println("FLeft " + isBlack(frontLeftLine) + " FRight " + isBlack(frontRightLine) + " RRight " + isBlack(rearRightLine) + " RLeft " + isBlack(rearLeftLine));
           // System.out.println("LeftShort: " + leftShortRange.getState() + " RightShort: " + rightShortRange.getState());
          //  System.out.println("\n");
           // Thread.sleep(3000);
            
            switch(lineSensorFlags){
                case 0b1111: // all sensors see black
                    //look for oponent...
                    System.out.println(longRange.getValue());
                    searchForEnemy();
                    break;
                case 0b0111: // front left sensor sees white
                    motor.right(700); //for some time...
                    break;
                case 0b1011: // front right sensor sees white
                    motor.left(700); //for some time...
                    break;
                case 0b1101: // rear left sensor sees white
                    motor.foward();
                    Thread.sleep(1000);
                    break;
                case 0b1110: // rear right sensor sees white
                    motor.foward();
                    Thread.sleep(1000);
                    break;
                case 0b0011: // front sensors see white
                    motor.turnAround();
                    break;
                case 0b1100: // rear sensors see white
                    break;
            }
            
       } 
        
    }
    
    public static void searchForEnemy() throws InterruptedException{
        longRangeValue = longRange.getValue();
        boolean i = rand.nextBoolean();
        
        /* idea of the "count" variable is to have
        ** the robot turn right x number of times 
        ** then begin the turn left x number of times
        ** so that it is not spinning in circles looking
        ** for the enemy.
        */
       // vals[count] = longRangeValue;
        
        
        /* WORKING CODE... USE THIS if short range does not work */
       /* if(longRangeValue>300){
            motor.foward();
        }else{
            if(count < 50 ){
                motor.left(30);
            }else{
                motor.right(30);
            }
            if(count == 99 ){
                count= -1;
            }
            count++;
        }
        */
        
        /*      WORKING CODE... */
        /*System.out.println(i);
        if(longRangeValue > 300){
            motor.foward();
        }else{
            if(i)
                motor.right(10);
            else
                motor.left(10);
        }
       */
        
        /*  MIGHT WORK, SHORTEN THE DELAYS
        if(longRangeValue > 500){
            motor.foward();
        }else{
            if(count < 5){
                motor.right(100);
            }
            if(count>=10){
                motor.left(100);
            }
            if(count==10){
                count = 0;
            }
            count++;
        }
       */
        
        
        /* THIS IS NOT WORKING... Probably due to the fact that
        ** the flags are being set and reset when the shouldn't be
        **          Setting the flags when the pin state changes
        **              should solve this issue.
        setShortSensorFlags();
        switch(shortSensorFlags){
            case 0b0011:
                motor.foward();
                //System.out.println("Both See Object");
                break;
            case 0b0001:
                motor.left(5);
               // System.out.println("Left See Object");
                break;
            case 0b0010:
               // System.out.println("Right See Object");
                motor.right(5);
                break;
            case 0b0000:
              //  System.out.println("None See Object");
                if(longRangeValue > 400){
                    motor.foward();
                }else{
                    if(i <1){
                        motor.left(25);
                    }else{
                        motor.right(25);
                    }
                }
                break;
        }
        */
        
        /* WORKING CODE... uses the short range sensor to stay locked on target */
        // Both short range sensors see an object
        if( (leftShortRange.getState() == PinState.LOW) && (rightShortRange.getState() == PinState.LOW) ){
            motor.foward();
        }
        // left sensor sees object, right sensor does not
        if( (leftShortRange.getState() == PinState.LOW) && (rightShortRange.getState() == PinState.HIGH) ){
            motor.right(10);
        }
        // right sensor sees object, left sensor does not
        if( (leftShortRange.getState() == PinState.HIGH) && (rightShortRange.getState() == PinState.LOW) ){
            motor.left(10);
        }
        // short range sensors do not see objcet, Check long range sensor
        if( (leftShortRange.getState() == PinState.HIGH) && (rightShortRange.getState() == PinState.HIGH) ){
            if(longRangeValue>250){
            motor.foward();
            }else{
                if(count < 50 ){
                    motor.left(30);
                }else{
                    motor.left(30);
                }
                if(count == 99 ){
                    count= -1;
                }
                count++;
            }
        }
        
    }
    
    public static boolean isBlack(digitalSensor sensor){
        return sensor.getState() == PinState.HIGH;
    }
    
    /* Updates the global variable that holds the line sensor's current state
    **
    **  True = Black detected
    **  False = White Detected
    **
    ** [0b1000] -> frontLeftSensor
    ** [0b0100] -> frontRightSensor
    ** [0b0010] -> rearLeftSensor
    ** [0b0001] -> rearRightSensor
    */
    public static void setLineSensorFlags(){
        // set front left sensor if black is detected 
        if(isBlack(frontLeftLine)){
            lineSensorFlags = (byte) (lineSensorFlags | 0b1000) ;
        }else{
            lineSensorFlags = (byte) (lineSensorFlags ^ 0b1000) ;
        }
        // set front right sensor if black is detected 
        if(isBlack(frontRightLine)){
            lineSensorFlags = (byte) (lineSensorFlags | 0b0100) ;
        }else{
            lineSensorFlags = (byte) (lineSensorFlags ^ 0b0100) ;
        }
        // set rear left sensor if black is detected 
        if(isBlack(rearLeftLine)){
            lineSensorFlags = (byte) (lineSensorFlags | 0b0010) ;
        }else{
            lineSensorFlags = (byte) (lineSensorFlags ^ 0b0010) ;
        }
        // set rear right sensor if black is detected 
        if(isBlack(rearRightLine)){
            lineSensorFlags = (byte) (lineSensorFlags | 0b0001) ;
        }else{
            lineSensorFlags = (byte) (lineSensorFlags ^ 0b0001) ;
        }
    }
    
    /* Updates the global variable that holds the short range sensor's current state
    ** [0b0001] -> left sensor sees object
    ** [0b0010] -> right sensor sees object
    ** [0b0011] -> both sensors see object
    ** [0b0000] -> both sensors DO NOT see object
    */
    public static void setShortSensorFlags(){
        if(leftShortRange.getState() == PinState.LOW){
            shortSensorFlags = (byte) (shortSensorFlags | 0b0001); 
        }else{
            shortSensorFlags = (byte) (shortSensorFlags ^ 0b0001);
        }
        if(rightShortRange.getState() == PinState.HIGH){
            shortSensorFlags = (byte) (shortSensorFlags | 0b0010);
        }else{
            shortSensorFlags = (byte) (shortSensorFlags ^ 0b0010);
        }
    }
}
