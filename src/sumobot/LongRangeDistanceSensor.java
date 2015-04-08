package sumobot;

import com.pi4j.component.sensor.impl.DistanceSensorComponent;
import com.pi4j.gpio.extension.ads.ADS1015GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1015Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider.ProgrammableGainAmplifierValue;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.i2c.I2CBus;
import java.io.IOException;


public class LongRangeDistanceSensor{
    
    ADS1015GpioProvider gpioProvider;
    GpioPinAnalogInput distanceSensorPin;
    DistanceSensorComponent distanceSensor;
       
    public LongRangeDistanceSensor(GpioController gpio) throws IOException{
        
        gpioProvider = new ADS1015GpioProvider(I2CBus.BUS_1, ADS1015GpioProvider.ADS1015_ADDRESS_0x48);
        distanceSensorPin = gpio.provisionAnalogInputPin(gpioProvider, ADS1015Pin.INPUT_A3, "DistanceSensor-A0");
        gpioProvider.setProgrammableGainAmplifier(ADS1x15GpioProvider.ProgrammableGainAmplifierValue.PGA_4_096V, ADS1015Pin.ALL);
        gpioProvider.setEventThreshold(150, ADS1015Pin.ALL);
        gpioProvider.setMonitorInterval(100);

        gpioProvider.setProgrammableGainAmplifier(ProgrammableGainAmplifierValue.PGA_4_096V, ADS1015Pin.ALL);

        distanceSensor = new DistanceSensorComponent(distanceSensorPin);
        
    }
    
    public double getValue(){
        return this.distanceSensor.getValue();
    }
    
}