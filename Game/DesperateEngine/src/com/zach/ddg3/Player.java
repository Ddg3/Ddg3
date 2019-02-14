package com.zach.ddg3;

import com.ivan.xinput.XInputAxesDelta;
import com.ivan.xinput.XInputButtonsDelta;
import com.ivan.xinput.XInputComponentsDelta;
import com.ivan.xinput.XInputDevice;
import com.ivan.xinput.enums.XInputButton;
import com.zach.ddg3.components.AABBComponent;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

import java.awt.event.KeyEvent;

public class Player extends Object
{
    public static boolean isGoose = false;
    public static int playerNumber;
    public XInputDevice device;
    private XInputComponentsDelta delta;
    private XInputButtonsDelta buttons;
    private XInputAxesDelta axes;

    private boolean collidingTop = false;
    private boolean collidingBottom = false;
    private boolean collidingLeft = false;
    private boolean collidingRight = false;

    private float lStickX = 0f;
    private float lStickY = 0f;
    private float rStickX = 0f;
    private float rStickY = 0f;

    private boolean droppingIn = false;
    private boolean selecting = false;
    private boolean stickSelecting;
    private boolean selected = false;
    private Object selection = null;

    private int color = (int)(Math.random() * Integer.MAX_VALUE);

    public Player(String name, int width, int height, String path, int totalFrames, float frameLife, int playerNumber)
    {
        super(name, width, height, path, totalFrames, frameLife);

        this.playerNumber = playerNumber;
        device = GameManager.deviceManager.devices[playerNumber];
        delta = GameManager.deviceManager.deltas[playerNumber];
        buttons = GameManager.deviceManager.buttons[playerNumber];
        axes = GameManager.deviceManager.axes[playerNumber];
        //GameManager.deviceManager.addComponents(device, delta, buttons, axes);
        this.addComponent(new AABBComponent(this, "wall"));

        this.paddingTop = 15;
        this.paddingSide = 5;
        this.setInGame(false);
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        selected = false;
        collidingTop = false;
        collidingBottom = false;
        collidingLeft = false;
        collidingRight = false;

        if(device.poll())
        {
            lStickX += axes.getLXDelta();
            lStickY += axes.getLYDelta();
            rStickX += axes.getRXDelta();
            rStickY += axes.getRYDelta();

            if(this.isInGame() && !selecting)
            {
                moveController(dt);
                look();
            }
            if(selecting)
            {
                select(selection);
            }

            dropIn(dt);

            //System.out.println(offsetPos.x);
           /*this.offsetPos.x = main.getInput().getMouseX() - this.width;
           this.offsetPos.y = main.getInput().getMouseY() + this.height + 180;*/
        }
        else
        {
            moveKeyboard(main,dt);
        }

        this.offsetPos.x = (int)(this.position.x - (this.width / 2) + 320);
        this.offsetPos.y = (int)(this.position.y - (this.height / 2) + 180);
        this.updateComponents(main, gameManager, dt);
        //System.out.println(this.position.y);

        this.animate(dt);
    }

    @Override
    public void render(Main main, Renderer r)
    {
        super.render(main, r);
        //r.drawFillRectangle((int)offsetPos.x, (int)offsetPos.y, width, height, color);
        //this.renderComponents(main, r);
    }

    @Override
    public void collision(Object other)
    {
        //System.out.println(other.getTag());
        if(other.getTag().equalsIgnoreCase("Wall") && this.isInGame())
        {
            AABBComponent myC = (AABBComponent)this.findComponentBySubtag("wall");
            AABBComponent otherC = (AABBComponent)other.findComponentBySubtag("wall");
                //System.out.println(Math.abs(myC.getLastCenterX() - otherC.getLastCenterX()) + " < " + (myC.getHalfWidth() + otherC.getHalfWidth()));
                if (Math.abs(myC.getLastCenterX() - otherC.getLastCenterX()) < (myC.getHalfWidth() + otherC.getHalfWidth()) - 2) {
                    //Top/bottom collision because X values are closer than Y
                    //Top
                    if (myC.getCenterY() < otherC.getCenterY()) {
                        //System.out.println("AH");
                        int distance = myC.getHalfHeight() + otherC.getHalfHeight() - (otherC.getCenterY() - myC.getCenterY());
                        position.y -= distance;
                        offsetPos.y -= distance;
                        myC.setCenterY(myC.getCenterY() - distance);
                        collidingTop = true;
                    }

                    //Bottom
                    if (myC.getCenterY() > otherC.getCenterY()) {
                        //System.out.println("AH");
                        int distance = myC.getHalfHeight() + otherC.getHalfHeight() - (myC.getCenterY() - otherC.getCenterY());
                        position.y += distance;
                        offsetPos.y += distance;
                        myC.setCenterY(myC.getCenterY() + distance);
                        collidingBottom = true;
                    }
                } else
                    {
                    //Side collision bc vice versa
                    //Left
                    if (myC.getCenterX() < otherC.getCenterX()) {
                        //System.out.println("AH");
                        int distance = (myC.getHalfWidth() + otherC.getHalfWidth()) - (otherC.getCenterX() - myC.getCenterX());
                        position.x -= distance;
                        offsetPos.x -= distance;
                        myC.setCenterX(myC.getCenterX() - distance);
                        collidingRight = true;
                    }

                    //Right
                    if (myC.getCenterX() > otherC.getCenterX()) {
                        //System.out.println("AH");
                        int distance = (myC.getHalfWidth() + otherC.getHalfWidth()) - (myC.getCenterX() - otherC.getCenterX());
                        position.x += distance;
                        offsetPos.x += distance;
                        myC.setCenterX(myC.getCenterX() + distance);
                        collidingLeft = true;
                    }
                }
        }

        if(other.getTag().equalsIgnoreCase("Selection") && this.isInGame())
        {
            if(this.device.getDelta().getButtons().isPressed(XInputButton.A) && this.isInGame() && !selecting && !selected)
            {
                selecting = true;
                selection = other;
                selection.setFrame(1);
            }

            if(this.device.getDelta().getButtons().isPressed(XInputButton.B) && this.isInGame() && selecting)
            {
                selection.setFrame(0);
                selecting = false;
                selection = null;
            }
        }
    }

    public void moveController(float dt)
    {
        //Left
        if (lStickX > 0.4f)
        {
            if(!collidingRight)
            {
                this.position.x -= 200f * dt;
            }
            if (rStickX < 0.4f && rStickX > -0.4f)
            {
                this.setFrame(6);
            }
        }
        //Right
        if (lStickX < -0.4f)
        {
            if(!collidingLeft)
            {
                this.position.x += 200f * dt;
            }
            if (rStickX < 0.4f && rStickX > -0.4f) {
                this.setFrame(2);
            }
        }

        //Down
        if (lStickY > 0.4f)
        {
            if(!collidingTop)
            {
                this.position.y += 200f * dt;
            }
            if (rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(0);
            }
        }

        //Up
        if (lStickY < -0.4f)
        {
            if(!collidingBottom)
            {
                this.position.y -= 200f * dt;
            }
            if (rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(4);
            }
        }

        if(lStickY < 0.4f && lStickY > -0.4f)
        {
            //Idle on Y
            collidingTop = false;
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

    public void moveKeyboard(Main main, float dt)
    {
        if (main.getInput().isKey(KeyEvent.VK_A))
        {
            this.position.x += -100f * dt;
            this.setFrame(6);

        }
        if (main.getInput().isKey(KeyEvent.VK_D))
        {
            this.position.x += 100f * dt;
            this.setFrame(2);
        }

        if (main.getInput().isKey(KeyEvent.VK_S))
        {
            this.position.y += 100f * dt;
            this.setFrame(0);
        }
        if (main.getInput().isKey(KeyEvent.VK_W))
        {
            this.position.y +=  -100f * dt;
            this.setFrame(4);
        }

        //Changes frame of animation on the diagonals if the right thumbstick is not being used
        if (main.getInput().isKey(KeyEvent.VK_A) && main.getInput().isKey(KeyEvent.VK_W))
        {
            this.setFrame(5);
        }
        if (main.getInput().isKey(KeyEvent.VK_A) && main.getInput().isKey(KeyEvent.VK_S))
        {
            this.setFrame(7);
        }
        if (main.getInput().isKey(KeyEvent.VK_D) && main.getInput().isKey(KeyEvent.VK_S))
        {
            this.setFrame(1);
        }
        if (main.getInput().isKey(KeyEvent.VK_D) && main.getInput().isKey(KeyEvent.VK_W))
        {
            this.setFrame(3);
        }

        //VERY IMPORTANT
        /*if(this.position.x >= 320 || this.position.x <= -320)
        {
            moveDir.x *= -1;
        }
        if(this.position.y >= 180 || this.position.y <= -180)
        {
            moveDir.y *= -1;
        }*/
    }

    public void dropIn(float dt)
    {
        if(this.device.getDelta().getButtons().isPressed(XInputButton.START) && !this.isInGame() && !this.droppingIn)
        {
            this.droppingIn = true;
            this.visible = true;
            this.playInRangeAndBack(8, 22);
        }

        if(this.position.y >= GameManager.center.position.y && droppingIn)
        {
            droppingIn = false;
            this.stop();
            this.setInGame(true);
            this.setFrame(1);
            GameManager.players.add(this);
        }

        if(this.droppingIn)
        {
            this.position.x += 90 * dt;
            this.position.y += 90 * dt;
        }
    }

    public void select(Object selection)
    {
        //Left
        if(!stickSelecting)
        {
            if (lStickX > 0.4f) {
                if (selection.getFrame() == 1) {
                    selection.setFrame(3);
                    stickSelecting = true;
                    return;
                } else {
                    selection.goToPrevFrame();
                    stickSelecting = true;
                }
            }

            //Right
            if (lStickX < -0.4f) {
                if (selection.getFrame() == 3) {
                    selection.setFrame(1);
                    stickSelecting = true;
                    return;
                } else {
                    selection.goToNextFrame();
                    stickSelecting = true;
                }
            }
        }
        else
            {
                if (lStickX < 0.4f && lStickX > -0.4f)
                {
                    stickSelecting = false;
                }
            }
        if(this.device.getDelta().getButtons().isPressed(XInputButton.A))
        {
            System.out.println(selection.anim);
            this.selection.setFrame(0);
            selecting = false;
            this.selection = null;
            selected = true;
        }
    }
}
