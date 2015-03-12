/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sumobot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;



/**
 *
 * @author Dominick Sparks
 */
public class SumoBot {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        final GpioController gpio = GpioFactory.getInstance();
       // motorController motor = new motorController(gpio);
        
        
        Pin sensor1Pin = RaspiPin.GPIO_01; 
        Pin sensor2Pin = RaspiPin.GPIO_04;
        
        LineSensor sensor1 = new LineSensor("test",sensor1Pin,gpio);
        LineSensor sensor2 = new LineSensor("test",sensor2Pin,gpio);
        
        
        sensor1.run();
        sensor2.run();
        
        for(int i=0;i<10000000;i++){
            
        }
        
        /*
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
        
        gpio.shutdown();
    }
}
