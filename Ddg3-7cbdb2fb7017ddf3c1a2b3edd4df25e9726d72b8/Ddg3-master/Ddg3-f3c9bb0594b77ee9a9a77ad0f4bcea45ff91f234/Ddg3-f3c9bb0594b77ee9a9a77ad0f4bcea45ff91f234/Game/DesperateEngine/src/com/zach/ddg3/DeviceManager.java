package com.zach.ddg3;

import com.ivan.xinput.XInputAxesDelta;
import com.ivan.xinput.XInputButtonsDelta;
import com.ivan.xinput.XInputComponentsDelta;
import com.ivan.xinput.XInputDevice;
import com.zach.engine.Main;

import java.util.ArrayList;

public class DeviceManager extends GameLevel
{
    public static XInputDevice[] devices = new XInputDevice[4];
    public static XInputComponentsDelta[] deltas = new XInputComponentsDelta[4];
    public static XInputButtonsDelta[] buttons = new XInputButtonsDelta[4];
    public static XInputAxesDelta[] axes = new XInputAxesDelta[4];
    @Override
    public void init(Main main)
    {
        //Runs through every device to check if its working and adds deltas, buttons, and axes to each
        for(int i = 0; i < devices.length; i++)
        {
            DeviceCheck(i);

            deltas[i] = devices[i].getDelta();
            buttons[i] = deltas[i].getButtons();
            axes[i] = deltas[i].getAxes();
        }
    }

    @Override
    public void update(Main main, float dt)
    {
        //Checks if any device is disconnected
        //if so it calls u a silly goose
        for(int i = 0; i < devices.length; i++)
        {
            if(!devices[i].isConnected())
            {
                //System.out.println("Device " + i + " is disconnected, silly goose!");
            }
        }
    }

    @Override
    public void uninit()
    {

    }
    public void addComponents(XInputDevice device, XInputComponentsDelta delta, XInputButtonsDelta buttons, XInputAxesDelta axes)
    {
        delta = device.getDelta();
        buttons = delta.getButtons();
        axes = delta.getAxes();
        return;
    }
    public void DeviceCheck(int devicenum)
    {
        //Checks if device exists
        try
        {
            devices[devicenum] = XInputDevice.getDeviceFor(devicenum);
        }
        catch (com.ivan.xinput.exceptions.XInputNotLoadedException e)
        {
            System.out.println("Exception thrown" + devicenum);
        }
    }
}
