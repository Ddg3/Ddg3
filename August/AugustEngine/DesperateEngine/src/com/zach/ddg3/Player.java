package com.zach.ddg3;

import com.ivan.xinput.XInputAxesDelta;
import com.ivan.xinput.XInputButtonsDelta;
import com.ivan.xinput.XInputComponentsDelta;
import com.ivan.xinput.XInputDevice;
import com.zach.engine.Main;
import com.zach.engine.Renderman;

public class Player extends Object
{
    public static boolean isGoose = false;
    public static int playerNumber;
    public XInputDevice device;
    private XInputComponentsDelta delta;
    private XInputButtonsDelta buttons;
    private XInputAxesDelta axes;

    private float lStickX = 0f;
    private float lStickY = 0f;
    private float rStickX = 0f;
    private float rStickY = 0f;

    public Player(String name, int width, int height, String path, int totalFrames, float frameLife, Boolean target, int playerNumber)
    {
        super(name, width, height, path, totalFrames, frameLife, target);

        this.playerNumber = playerNumber;
        device = GameManager.deviceManager.devices[playerNumber];
        delta = GameManager.deviceManager.deltas[playerNumber];
        buttons = GameManager.deviceManager.buttons[playerNumber];
        axes = GameManager.deviceManager.axes[playerNumber];
        //GameManager.deviceManager.addComponents(device, delta, buttons, axes);
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        if(device.poll())
        {
            lStickX += axes.getLXDelta();
            lStickY += axes.getLYDelta();
            rStickX += axes.getRXDelta();
            rStickY += axes.getRYDelta();

            move();
            look();

            this.offsetPos.x = this.position.x - this.width / 2 + 320;
            this.offsetPos.y = this.position.y - this.height / 2 + 180;
        }
    }

    public void move()
    {
        if (lStickX > 0.4f)
        {
            this.position.x -= 2f;
            if (rStickX < 0.4f && rStickX > -0.4f)
            {
                this.setFrame(6);
            }
        }
        if (lStickX < -0.4f) {
            this.position.x +=  2f;
            if (rStickX < 0.4f && rStickX > -0.4f) {
                this.setFrame(2);
            }
        }

        if (lStickY > 0.4f) {
            this.position.y += 2f;
            if (rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(0);
            }
        }
        if (lStickY < -0.4f) {
            this.position.y -=  2f;
            if (rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(4);
            }
        }

        //Changes frame of animation on the diagonals if the right thumbstick is not being used
        if (lStickX > 0.4f && lStickY < -0.4f)
        {
            if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f)
            {
                this.setFrame(5);
            }
        }
        if (lStickX > 0.4f && lStickY > 0.4f) {
            if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(7);
            }
        }
        if (lStickY > 0.4f && lStickX < -0.4f) {
            if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(1);
            }
        }
        if (lStickY < -0.4f && lStickX < -0.4f) {
            if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(3);
            }
        }
    }

    public void look()
    {
        if (rStickX > 0.4f) {
            //Left
            this.setFrame(6);
        }
        if (rStickX < -0.4f) {
            //Right
            this.setFrame(2);
        }
        if (rStickY > 0.4f) {
            //Down
            this.setFrame(0);
        }
        if (rStickY < -0.4f) {
            //Up
            this.setFrame(4);
        }
        if (rStickX > 0.4f && rStickY < -0.4f) {
            //Left and Up
            this.setFrame(5);
        }
        if (rStickX > 0.4f && rStickY > 0.4f) {
            //Left and Down
            this.setFrame(7);
        }
        if (rStickY > 0.4f && rStickX < -0.4f) {
            //Right and Down
            this.setFrame(1);
        }
        if (rStickY < -0.4f && rStickX < -0.4f) {
            //Right and Up
            this.setFrame(3);
        }
    }
}
