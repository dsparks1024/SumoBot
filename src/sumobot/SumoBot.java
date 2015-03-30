
package sumobot;

import com.pi4j.io.gpio.*;
import java.io.IOException;

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
    static Pin input1 = RaspiPin.GPIO_12;
    static Pin input2 = RaspiPin.GPIO_13;
    static Pin input3 = RaspiPin.GPIO_14;
    static Pin input4 = RaspiPin.GPIO_21;
    static MotorController motor;
    
    // Define line sensor pins
    static Pin frontLeftLinePin = RaspiPin.GPIO_27;
    static Pin frontRightLinePin = RaspiPin.GPIO_26;
    static Pin rearLeftLinePin = RaspiPin.GPIO_28;
    static Pin rearRightLinePin = RaspiPin.GPIO_25;
    // Define line sensor objects
    static digitalSensor frontLeftLine;
    static digitalSensor frontRightLine;
    static digitalSensor rearLeftLine;
    static digitalSensor rearRightLine;
    
     // Define short range pins
    static Pin leftShortRangePin = RaspiPin.GPIO_29;
    static Pin rightShortRangePin = RaspiPin.GPIO_24;
    // Define short range sensor objects
    static digitalSensor leftShortRange;
    static digitalSensor rightShortRange;
    
    
    // Global vars to store sensor data 
    static byte lineSensorFlags;
    
    public static void main(String[] args) throws InterruptedException, IOException {
        
        System.out.println("Running");
        // Initialize variables
        lineSensorFlags = 0x03;
        gpio = GpioFactory.getInstance();
        
        // Construct motor controller object
        motor = new MotorController(gpio, input1, input2, input3, input4);
        motor.stop();

        // Construct the line sensor objects
        frontLeftLine = new digitalSensor(0,"frontLeftLine",frontLeftLinePin,gpio);
        frontRightLine = new digitalSensor(0,"frontRightLine",frontRightLinePin,gpio);
        rearLeftLine = new digitalSensor(0,"rearLeftLine",rearLeftLinePin,gpio);
        rearRightLine = new digitalSensor(0,"rearRightLine",rearRightLinePin,gpio);
        
        // Construct the short range sensor objects
        leftShortRange = new digitalSensor(1,"leftShortRange",leftShortRangePin,gpio);
        rightShortRange = new digitalSensor(1,"rightShortRange",rightShortRangePin,gpio);
        
        System.out.println(lineSensorFlags);
       
        while(true){
        
            //System.out.println(frontLeftLine.getState() + " " + frontRightLine.getState());
            setLineSensorFlags();
            
            System.out.println(lineSensorFlags);
            System.out.println("Left " + isBlack(frontLeftLine) + " Right " + isBlack(frontRightLine));
            System.out.println("\n");
           // Thread.sleep(3000);
            
            switch(lineSensorFlags){
                case 0b1111: // all sensors see black
                    //look for oponent...
                    motor.foward();
                    break;
                case 0b0111: // front left sensor sees white
                    motor.right(500); //for some time...
                    break;
                case 0b1011: // front right sensor sees white
                    motor.left(500); //for some time...
                    break;
                case 0b1101: // rear left sensor sees white
                    break;
                case 0b1110: // rear right sensor sees white
                    break;
                case 0b0011: // front sensors see white
                    motor.turnAround();
                    break;
                case 0b1100: // rear sensors see white
                    break;
            }
                
           
       }
 
        //gpio.shutdown();
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
        /*if(isBlack(rearLeftLine)){
            lineSensorFlags = (byte) (lineSensorFlags | 0b0010) ;
        }else{
            lineSensorFlags = (byte) (lineSensorFlags ^ 0b0010) ;
        }
        // set rear right sensor if black is detected 
        if(isBlack(rearRightLine)){
            lineSensorFlags = (byte) (lineSensorFlags | 0b0001) ;
        }else{
            lineSensorFlags = (byte) (lineSensorFlags ^ 0b0001) ;
        }*/
    }
}
