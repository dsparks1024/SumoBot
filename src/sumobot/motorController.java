/*
 * Input1 -> Left Motor Forward
 * Input2 -> Left Motor Reverse
 * Input3 -> Right Motor Forward
 * Input4 -> Right Motor Reverse 
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
public class motorController {
    
    final GpioPinDigitalOutput input1;
    final GpioPinDigitalOutput input2;
    final GpioPinDigitalOutput input3;
    final GpioPinDigitalOutput input4;  
                    
    /**@
     * @param gpio
     * @param Input1
     * @param Input2
     * @param Input3
     * @param Input4 
     */
    /*
    public motorController(GpioController gpio,RaspiPin Input1,RaspiPin Input2,RaspiPin Input3,RaspiPin Input4){
        System.out.println("This constructor has not been implemented yet");
    }
    */
    
    /**@
     * 
     * @param gpio 
     */
    public motorController(GpioController gpio){
       input1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "input1", PinState.LOW);
       input2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "input2", PinState.LOW);
       input3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "input3", PinState.LOW);
       input4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "input4", PinState.LOW);
    }
    
    public void foward(){
        input1.high();
        input2.low();
        input3.high();
        input4.low();
    }
    
    public void reverse(){
        input1.low();
        input2.high();
        input3.low();
        input4.high();
    }
    
    public void left(){
        input1.low();
        input2.low();
        input3.high();
        input4.low();
    }
    
    public void right(){
        input1.high();
        input2.low();
        input3.low();
        input4.low();
    }
    
    public void stop(){
        input1.low();
        input2.low();
        input3.low();
        input4.low();
    }
    
}
