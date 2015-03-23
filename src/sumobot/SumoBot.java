/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sumobot;

import com.pi4j.gpio.extension.ads.ADS1015GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1015Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Dominick Sparks
 */
public class SumoBot implements Observer{

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        
        final GpioController gpio = GpioFactory.getInstance();
    
        // Motor Controller
       /* Pin input1 = RaspiPin.GPIO_00;
        Pin input2 = RaspiPin.GPIO_01;
        Pin input3 = RaspiPin.GPIO_02;
        Pin input4 = RaspiPin.GPIO_03;
        MotorController motor = new MotorController(gpio, input1, input2, input3, input4);
        motor.foward();
        Thread.sleep(3000);
        motor.stop();
        Thread.sleep(500);
        motor.left();
        Thread.sleep(500);
        motor.right();
        Thread.sleep(500);
        motor.reverse();
        Thread.sleep(3000);
        motor.stop();
        */
        
        
        // Hardware thread testing... (not possible)
        /*
        Pin sensor1Pin = RaspiPin.GPIO_01; 
        Pin sensor2Pin = RaspiPin.GPIO_04;
        
        LineSensor sensor1 = new LineSensor("test",sensor1Pin,gpio);
        LineSensor sensor2 = new LineSensor("test",sensor2Pin,gpio);
        */
        
        //sensor1.run();
        //sensor2.run();
       
        /* Testing the short range digital sensor 
        **
        */
       /* Pin shortRangeSensor = RaspiPin.GPIO_29;
        digitalSensor shortSensor = new digitalSensor("shortRange","sensor1",shortRangeSensor,gpio);
        
        Pin shortRangeSensor2 = RaspiPin.GPIO_28;
        digitalSensor shortSensor2 = new digitalSensor("shortRange","sensor2",shortRangeSensor2,gpio);
        
        shortSensor.addObserver(new SumoBot());
        shortSensor2.addObserver(new SumoBot());
        */
        LongRangeDistanceSensor longRangeSensor = new LongRangeDistanceSensor(gpio);
        longRangeSensor.run();
        longRangeSensor.addObserver(new SumoBot());
        
       while(true){
           
       }
 
       // gpio.shutdown();
    }
    
    /*
    **  Method that is called whenever an event occurs from an
    **  "Observable" class. 
    **  - Will have to set up variable to track the state of the sensors?
    **  - Consider (ehh) with a polling implmentation to get sensor data
    */
    @Override
    public void update(Observable o, Object arg){
        //digitalSensor dS = (digitalSensor) o;
        //System.out.println(dS.getName());
        LongRangeDistanceSensor DS = (LongRangeDistanceSensor) o;
        System.out.println(DS.getDistance());
    }
 
}
