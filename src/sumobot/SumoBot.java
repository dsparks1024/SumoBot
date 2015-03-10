/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sumobot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;



/**
 *
 * @author Dominick Sparks
 */
public class SumoBot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        final GpioController gpio = GpioFactory.getInstance();
        motorController motor = new motorController(gpio);
        
        
        
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
        
        gpio.shutdown();
    }
}
