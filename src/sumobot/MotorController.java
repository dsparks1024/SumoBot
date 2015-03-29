/*
* Input1 -> Left Motor Forward
* Input2 -> Left Motor Reverse
* Input3 -> Right Motor Forward
* Input4 -> Right Motor Reverse
*/
package sumobot;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
/**
*
* @author Dominick Sparks
*/
public class MotorController {
final GpioController gpio;
final GpioPinDigitalOutput input1;
final GpioPinDigitalOutput input2;
final GpioPinDigitalOutput input3;
final GpioPinDigitalOutput input4;
/**@
* @param gpio
* @param in1 -> Left Motor Forward
* @param in2 -> Left Motor Reverse
* @param in3 -> Right Motor Forward
* @param in4 -> Right Motor Reverse
*/
    public MotorController(GpioController gpio,Pin in1,Pin in2,Pin in3,Pin in4){
        this.gpio = gpio;
        this.input1 = this.gpio.provisionDigitalOutputPin(in1,PinState.LOW);
        this.input2 = this.gpio.provisionDigitalOutputPin(in2,PinState.LOW);
        this.input3 = this.gpio.provisionDigitalOutputPin(in3,PinState.LOW);
        this.input4 = this.gpio.provisionDigitalOutputPin(in4,PinState.LOW);
    }
    public void foward() throws InterruptedException{
        input1.high();
        input2.low();
        input3.high();
        input4.low();
    }
    public void reverse() throws InterruptedException{
        input1.low();
        input2.high();
        input3.low();
        input4.high();
    }
    public void left() throws InterruptedException{
        input1.low();
        input2.low();
        input3.high();
        input4.low();
    }
    public void right() throws InterruptedException{
        input1.high();
        input2.low();
        input3.low();
        input4.low();
    }
    public void stop() {
        input1.low();
        input2.low();
        input3.low();
        input4.low();
    }
}