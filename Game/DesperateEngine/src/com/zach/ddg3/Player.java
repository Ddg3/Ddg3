package com.zach.ddg3;

import com.ivan.xinput.XInputAxesDelta;
import com.ivan.xinput.XInputButtonsDelta;
import com.ivan.xinput.XInputComponentsDelta;
import com.ivan.xinput.XInputDevice;
import com.ivan.xinput.enums.XInputButton;
//import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.zach.ddg3.components.AABBComponent;
import com.zach.ddg3.components.WeaponComponent;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends Object
{
    private int playerNumber = 0;
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

    private float rTrigger = 0f;
    private float lTrigger = 0f;

    private boolean droppingIn = false;
    private boolean selecting = false;
    private boolean stickSelecting;
    private boolean selected = false;
    private Object selection = null;
    private boolean isReady = false;

    private int time = 60;
    private float second = 1.5f;
    private float tempSecond = 1.5f;
    private boolean isGoose = false;

    private int keyUp = KeyEvent.VK_W;
    private int keyDown = KeyEvent.VK_S;
    private int keyLeft = KeyEvent.VK_A;
    private int keyRight = KeyEvent.VK_D;
    private int keyDropIn = KeyEvent.VK_SPACE;
    private int keySelect = KeyEvent.VK_E;
    //private int keyShoot = KeyEvent.VK_SPACE;
    private int keyDeselect = KeyEvent.VK_R;
    private int keyColor1 = KeyEvent.VK_Z;
    private int keyColor2 = KeyEvent.VK_X;
    private boolean keyBoardSelecting = false;
    //private int keyAltShoot = KeyEvent.VK_SHIFT;

    private int baseHeight = 68;
    private int baseWidth = 63;
    private int basePaddingTop = 15;
    private int basePaddingSide = 10;
    private int[] skinColors = new int[8];
    private int skIndex = 1;
    private boolean rainbow;

    private boolean isRemoved = false;
    private boolean nearSelect = false;
    private boolean isTimedOut = false;
    private boolean isGrabbed = false;
    private boolean colorKeyUp = true;

    private boolean isStunned = false;
    private float stunTimer = 2.5f;
    private float tempStun = stunTimer;

    public boolean isKeyBoard() {
        return isKeyBoard;
    }

    public void setKeyBoard(boolean keyBoard) {
        isKeyBoard = keyBoard;
    }

    private boolean isKeyBoard = true;
    private Vector toMouse = new Vector(0,0);

    private ArrayList<Vector> frameHitboxOffsets = new ArrayList<>(1);

    private Object timer = new Object("timer" + playerNumber, 33, 21, "/numbers.png", 61, 0f);

    public Object getIndicator() {
        return indicator;
    }

    public void setIndicator(Object indicator) {
        this.indicator = indicator;
    }

    private Object indicator;

    private int widthDifference = 0;
    private int heightDifference = 0 ;

    private int color = (int)(Math.random() * Integer.MAX_VALUE);
    private Object stars;

    public Player(String name, int width, int height, String path, int totalFrames, float frameLife, int playerNumber)
    {
        super(name, width, height, path, totalFrames, frameLife);

        this.tag = "Player";
        this.playerNumber = playerNumber;
        device = GameManager.deviceManager.devices[playerNumber];
        delta = GameManager.deviceManager.deltas[playerNumber];
        buttons = GameManager.deviceManager.buttons[playerNumber];
        axes = GameManager.deviceManager.axes[playerNumber];
        //GameManager.deviceManager.addComponents(device, delta, buttons, axes);
        this.addComponent(new AABBComponent(this, "player"));

        this.paddingTop = 15;
        this.paddingSide = 10;
        this.setInGame(false);

        timer.visible = false;
        GameManager.objects.add(timer);
        GameManager.timers.add(playerNumber, timer);

        offsetHitboxes();

        skinColors[0] = 0xffFF0000;
        skinColors[1] = 0xffFF9700;
        skinColors[2] = 0xffFFFF00;
        skinColors[3] = 0xff00FF00;
        skinColors[4] = 0xff00FFFF;
        skinColors[5] = 0xff0000FF;
        skinColors[6] = 0xffFF00FF;
        skinColors[7] = 0xff8400DB;
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {

        nearSelect = false;
        selected = false;
        collidingTop = false;
        collidingBottom = false;
        collidingLeft = false;
        collidingRight = false;

        if(device.poll())
        {
            //System.out.println(rTrigger);
            lStickX += axes.getLXDelta();
            lStickY += axes.getLYDelta();
            rStickX += axes.getRXDelta();
            rStickY += axes.getRYDelta();

            rTrigger += axes.getRTDelta();
            lTrigger += axes.getLTDelta();

            if(this.isInGame() && !selecting && !isTimedOut && !isStunned)
            {
                cameraCollision();
                moveController(dt);
                look();
                changeSkin(gameManager, main);
                rainbowSkin();
            }
            //System.out.println(offsetPos.x);
           /*this.offsetPos.x = main.getInput().getMouseX() - this.width;
           this.offsetPos.y = main.getInput().getMouseY() + this.height + 180;*/
        }
        else if(isKeyBoard)
        {
            if(this.isInGame() && !selecting && !isTimedOut && !isStunned)
            {
                cameraCollision();
                moveKeyboard(main, dt);
                lookKeyboard(main, gameManager);
                changeSkin(gameManager, main);
            }
        }

        dropIn(dt, main);

        if(selecting)
        {
            select(selection, main);
        }

        this.offsetPos.x = (int)(this.position.x - (this.width / 2) + 320);
        this.offsetPos.y = (int)(this.position.y - (this.height / 2) + 180);
        this.updateComponents(main, gameManager, dt);
        this.animate(dt);

        if(GameManager.gameLevelManager.gameState == GameLevelManager.GameState.MAIN_STATE)
        {
            if(tempSecond <= 0 && time > 0)
            {
                time -= 1;
                tempSecond = second;
            }

            if(time <= 0 && GameManager.gameLevelManager.gameState == GameLevelManager.GameState.MAIN_STATE && !isTimedOut)
            {
                isTimedOut = true;
                GameManager.cameraPlayers.remove(this);
                changeSprite(102, 81, "/testDeadGoose.png", 16, 0.1f);
                this.getObjImage().changeColor(skinColors[1], skinColors[skIndex]);
                setFrame(0);
                this.zIndex = Integer.MAX_VALUE - 1;
            }

            if(time > 0)
            {
                timer.setFrame(time);
            }
            /*timer.setPosition(this.position.x, this.position.y - 50);
            timer.visible = true;*/
        }
        if(isGoose)
        {
            tempSecond -= dt;
        }

        if(isStunned)
        {
            stars.setFrameLife(0.075f);
            tempStun -= dt;
            if(tempStun <= 0)
            {
                isStunned = false;
                tempStun = stunTimer;
                GameManager.objects.remove(stars);
            }
        }
    }

    @Override
    public void render(Main main, Renderer r)
    {
        super.render(main, r);
        //r.drawFillRectangle((int)offsetPos.x, (int)offsetPos.y, width, height, color);
        //this.renderComponents(main, r);
    }

    @Override
    public void collision(Object other, Main main)
    {
        //System.out.println(other.getTag());
        if(other.getTag().equalsIgnoreCase("Wall") && this.isInGame() && !isTimedOut)
        {
            AABBComponent myC = (AABBComponent)this.findComponentBySubtag("player");
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
            AABBComponent otherC = (AABBComponent) other.findComponentBySubtag("selection");
            if (otherC.getDesignatedPlayer() == playerNumber)
            {
                nearSelect = true;
                if ((this.device.getDelta().getButtons().isPressed(XInputButton.A) || main.getInput().isKeyDown(keySelect)) && this.isInGame() && !selecting && !selected)
                {
                    selecting = true;
                    selection = other;
                    selection.setFrame(1);
                }

                if ((this.device.getDelta().getButtons().isPressed(XInputButton.B) || main.getInput().isKey(keyDeselect)) && this.isInGame() && selecting) {
                    selection.setFrame(0);
                    selecting = false;
                    selection = null;
                }
            }
        }
    }

    public void stun()
    {
        if(!isStunned)
        {
            isStunned = true;

            stars = new Object("stunStars", 29, 11, "/stunStars.png", 6, 0.075f);
            stars.position = new Vector(this.position.x, this.position.y - (this.height / 2));
            stars.zIndex = Integer.MAX_VALUE;
            stars.playInRange(0,6);
            GameManager.objects.add(stars);
        }
    }

    public void changeSkin(GameManager gameManager, Main main)
    {
        int oldSkindex = skIndex;
        if(GameManager.gameLevelManager.getGameState() == GameLevelManager.GameState.SELECTION_STATE)
        {
            if(!colorKeyUp && (isKeyBoard && (main.getInput().isKeyUp(keyColor1) || main.getInput().isKeyUp(keyColor2))))
            {
                colorKeyUp = true;
            }
            if(buttons.isPressed(XInputButton.X) || (isKeyBoard && main.getInput().isKey(keyColor1) && colorKeyUp))
            {
                if(skIndex == 0)
                {
                    skIndex = 7;
                }
                else
                    {
                        skIndex--;
                        colorKeyUp = false;
                    }
            }
            if(buttons.isPressed(XInputButton.Y)|| (isKeyBoard && main.getInput().isKey(keyColor2) && colorKeyUp))
            {
                if (skIndex == 7)
                {
                    skIndex = 0;
                }
                else
                    {
                      skIndex++;
                        colorKeyUp = false;
                    }
            }
            this.getObjImage().changeColor(skinColors[oldSkindex], skinColors[skIndex]);

            GameManager.timePedestals.get(playerNumber).getObjImage().changeColor(skinColors[oldSkindex],
                    skinColors[skIndex]);

            if(gameManager.gameLevelManager.getGameState() == GameLevelManager.GameState.SELECTION_STATE)
            {
                selectionLevel.getExplosiveGuns()[playerNumber].getObjImage().changeColor(skinColors[oldSkindex], skinColors[skIndex]);
            }
        }

    }

    public void rainbowSkin()
    {
        if(buttons.isPressed(XInputButton.BACK))
        {
            if(rainbow)
            {
                rainbow = false;
                return;
            }
            else
            {
                rainbow = true;
            }
        }
        if(rainbow)
        {
            int oldSkindex = skIndex;
            if(skIndex == 7)
            {
                skIndex = 0;
                this.getObjImage().changeColor(skinColors[oldSkindex], skinColors[skIndex]);
                return;
            }
            else
                {
                    skIndex++;
                    this.getObjImage().changeColor(skinColors[oldSkindex], skinColors[skIndex]);
                }



        }
    }
    public void cameraCollision()
    {
        if(this.position.x - (this.width / 2) < GameManager.center.position.x - (GameManager.center.width / 2))
        {
            for(int i = 0; i < GameManager.players.size(); i++)
            {
                if(!GameManager.players.get(i).collidingRight)
                {
                    collidingRight = true;
                    return;
                }
            }
        }
        if(this.position.x + (this.width / 2) > GameManager.center.position.x + (GameManager.center.width / 2))
        {
            for(int i = 0; i < GameManager.players.size(); i++)
            {
                if(!GameManager.players.get(i).collidingLeft)
                {
                    collidingLeft = true;
                    return;
                }
            }
        }
        /*if(this.position.y - (this.height / 2) < GameManager.center.position.y - (GameManager.center.height / 2))
        {
            for(int i = 0; i < GameManager.players.size(); i++)
            {
                if(!GameManager.players.get(i).collidingBottom)
                {
                    collidingBottom = true;
                    return;
                }
            }
        }*/
        if(this.position.y + (this.height / 2) > GameManager.center.position.y + (GameManager.center.height / 2))
        {
            for(int i = 0; i < GameManager.players.size(); i++)
            {
                if(!GameManager.players.get(i).collidingTop)
                {
                    collidingTop = true;
                    return;
                }
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
                this.position.x -= 150f * dt;
            }
            if (rStickX < 0.4f && rStickX > -0.4f)
            {
                this.setFrame(6 + this.getFrameOffset());
            }
        }
        //Right
        if (lStickX < -0.4f)
        {
            if(!collidingLeft)
            {
                this.position.x += 150f * dt;
            }
            if (rStickX < 0.4f && rStickX > -0.4f) {
                this.setFrame(2 + this.getFrameOffset());
            }
        }

        //Down
        if (lStickY > 0.4f)
        {
            if(!collidingTop)
            {
                this.position.y += 150f * dt;
            }
            if (rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(0 + this.getFrameOffset());
            }
        }

        //Up
        if (lStickY < -0.4f)
        {
            if(!collidingBottom)
            {
                this.position.y -= 150f * dt;
            }
            if (rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(4 + this.getFrameOffset());
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
                this.setFrame(5 + this.getFrameOffset());
            }
        }
        if (lStickX > 0.4f && lStickY > 0.4f) {
            if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(7 + this.getFrameOffset());
            }
        }
        if (lStickY > 0.4f && lStickX < -0.4f) {
            if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(1 + this.getFrameOffset());
            }
        }
        if (lStickY < -0.4f && lStickX < -0.4f) {
            if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(3 + this.getFrameOffset());
            }
        }
    }

    public void look()
    {
        if (rStickX > 0.4f) {
            //Left
            this.setFrame(6 + this.getFrameOffset());
        }
        if (rStickX < -0.4f) {
            //Right
            this.setFrame(2 + this.getFrameOffset());
        }
        if (rStickY > 0.4f) {
            //Down
            this.setFrame(0 + this.getFrameOffset());
        }
        if (rStickY < -0.4f) {
            //Up
            this.setFrame(4 + this.getFrameOffset());
        }
        if (rStickX > 0.4f && rStickY < -0.4f) {
            //Left and Up
            this.setFrame(5 + this.getFrameOffset());
        }
        if (rStickX > 0.4f && rStickY > 0.4f) {
            //Left and Down
            this.setFrame(7 + this.getFrameOffset());
        }
        if (rStickY > 0.4f && rStickX < -0.4f) {
            //Right and Down
            this.setFrame(1 + this.getFrameOffset());
        }
        if (rStickY < -0.4f && rStickX < -0.4f) {
            //Right and Up
            this.setFrame(3 + this.getFrameOffset());
        }
    }

    public void moveKeyboard(Main main, float dt)
    {
        if (main.getInput().isKey(keyLeft))
        {
            if(!collidingRight)
            {
                this.position.x += -200f * dt;
            }
            this.setFrame(6 + this.getFrameOffset());

        }
        if (main.getInput().isKey(keyRight))
        {
            if(!collidingLeft)
            {
                this.position.x += 200f * dt;
            }
            this.setFrame(2 + this.getFrameOffset());
        }

        if (main.getInput().isKey(keyDown))
        {
            if(!collidingTop)
            {
                this.position.y += 200f * dt;
            }
            this.setFrame(0 + this.getFrameOffset());
        }
        if (main.getInput().isKey(keyUp))
        {
            if(!collidingBottom)
            {
                this.position.y += -200f * dt;
            }
            this.setFrame(4 + this.getFrameOffset());
        }

        //Changes frame of animation on the diagonals if the right thumbstick is not being used
        if (main.getInput().isKey(keyLeft) && main.getInput().isKey(keyUp))
        {
            this.setFrame(5 + this.getFrameOffset());
        }
        if (main.getInput().isKey(keyLeft) && main.getInput().isKey(keyDown))
        {
            this.setFrame(7 + this.getFrameOffset());
        }
        if (main.getInput().isKey(keyRight) && main.getInput().isKey(keyDown))
        {
            this.setFrame(1 + this.getFrameOffset());
        }
        if (main.getInput().isKey(keyRight) && main.getInput().isKey(keyUp))
        {
            this.setFrame(3 + this.getFrameOffset());
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

    public void lookKeyboard(Main main, GameManager gameManager)
    {
        double mousePosX = ((main.getInput().getMouseX() - main.getWidth() / 2) - (this.position.x - gameManager.center.position.x));
        double mousePosY = ((main.getInput().getMouseY() - main.getHeight() / 2) - (this.position.y - gameManager.center.position.y));
        double angle = Math.toDegrees(Math.atan2(mousePosY, mousePosX));
        if ((angle < -150 && angle > -180) || (angle > 150 && angle < 180)) {
            //Left
            this.setFrame(6 + this.getFrameOffset());
        }
        if ((angle > -30 && angle < 0) || (angle < 30 && angle > 0)) {
            //Right
            this.setFrame(2 + this.getFrameOffset());
        }
        if ((angle > 60 && angle < 120)) {
            //Down
            this.setFrame(0 + this.getFrameOffset());
        }
        if (angle < -60 && angle > -120) {
            //Up
            this.setFrame(4 + this.getFrameOffset());
        }
        if (angle > -150 && angle < -120) {
            //Left and Up
            this.setFrame(5 + this.getFrameOffset());
        }
        if (angle < 150 && angle > 120) {
            //Left and Down
            this.setFrame(7 + this.getFrameOffset());
        }
        if (angle > 30 && angle < 60) {
            //Right and Down
            this.setFrame(1 + this.getFrameOffset());
        }
        if (angle < -30 && angle > -60) {
            //Right and Up
            this.setFrame(3 + this.getFrameOffset());
        }
    }

    public void dropIn(float dt, Main main)
    {
        if((this.device.getDelta().getButtons().isPressed(XInputButton.START) || ( isKeyBoard && main.getInput().isKey(keyDropIn))) && !this.isInGame() && !this.droppingIn)
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
            if(GameManager.firstTime)
            {
                GameManager.players.add(this);
                GameManager.cameraPlayers.add(this);
                GameManager.timePedestals.set(playerNumber, new Object("playerFrame" + (playerNumber), 160, 58, "/playerFrameNew.png", 2, 0));
                GameManager.objects.add(GameManager.timePedestals.get(playerNumber));
                GameManager.timePedestals.get(playerNumber).zIndex = Integer.MAX_VALUE - 1;
                GameManager.timePedestals.get(playerNumber).getObjImage().changeColor(this.getSkinColors()[1],
                        this.getSkinColors()[this.getSkIndex()]);
                //tempPlayers++;
            }
        }

        if(this.droppingIn)
        {
            this.position.x += 90 * dt;
            this.position.y += 90 * dt;
        }
    }

    public void select(Object selection, Main main)
    {
        //Left
        if(!stickSelecting)
        {
            if (lStickX > 0.4f)
            {
                if (selection.getFrame() == 1) {
                    selection.setFrame(3);
                    stickSelecting = true;
                    return;
                } else
                    {
                    selection.goToPrevFrame();
                    stickSelecting = true;
                }
            }

            //Right
            if (lStickX < -0.4f) {
                if (selection.getFrame() == 3)
                {
                    selection.setFrame(1);
                    stickSelecting = true;
                    return;
                }
                else
                    {
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
            if (main.getInput().isKeyDown(keyLeft))
            {
                if (selection.getFrame() == 1)
                {
                    selection.setFrame(3);
                    return;

                }
                else
                {
                    selection.goToPrevFrame();
                }
            }

            //Right
            if (main.getInput().isKeyDown(keyRight)) {
                if (selection.getFrame() == 3)
                {
                    selection.setFrame(1);
                    return;
                }
                else
                {
                    selection.goToNextFrame();
                }
            }
        if(this.device.getDelta().getButtons().isPressed(XInputButton.A) || (isKeyBoard && main.getInput().isKeyDown(keySelect)))
        {
            int newWidth = 0;
            int newHeight = 0;
            String newPath = null;
            int newFrames = 0;
            int indWidth = 0;
            int indHeight = 0;
            String indPath = null;

            removeComponent("Weapon");
            switch (selection.getFrame())
            {
                case 1:
                    this.addComponent(new WeaponComponent(this, "rocketLauncher"));
                    newWidth = 102;
                    newHeight = 81;
                    newPath = "/Duck_rocketLauncher.png";
                    newFrames = 16;

                    indWidth = 43;
                    indHeight = 29;
                    indPath = "/missileIndicator.png";
                    break;

                case 2:
                    this.addComponent(new WeaponComponent(this, "grenadeLauncher"));
                    newWidth = 78;
                    newHeight = 69;
                    newPath = "/Duck_grenadeLauncher.png";
                    newFrames = 16;

                    indWidth = 36;
                    indHeight = 43;
                    indPath = "/mirvIndicator.png";
                    break;

                case 3:
                    this.addComponent(new WeaponComponent(this, "cannon"));
                    newWidth = 101;
                    newHeight = 103;
                    newPath = "/Duck_cannon.png";
                    newFrames = 8;

                    indWidth = 23;
                    indHeight = 30;
                    indPath = "/stunBombIndicator.png";
                    break;
            }
            widthDifference = newWidth - this.width;
            heightDifference = newHeight - this.height;
            this.paddingSide += (widthDifference / 2);
            this.paddingTop += (heightDifference / 2);

            if((this.width / 2) - this.paddingSide < (this.baseWidth / 2) - basePaddingSide)
            {
                this.paddingSide = basePaddingSide;
            }
            if((this.height / 2) - this.paddingTop < (this.baseHeight / 2) - basePaddingTop)
            {
                this.paddingTop = basePaddingTop;
            }
            this.selection.setFrame(0);
            selecting = false;
            this.selection = null;
            selected = true;
            isReady = true;

            this.changeSprite(newWidth, newHeight, newPath, newFrames, 0.1f);
            this.getObjImage().changeColor(skinColors[1], skinColors[skIndex]);
            indicator = new Object("indicator" + playerNumber, indWidth, indHeight, indPath, 1, 1);
            GameManager.objects.add(indicator);
            GameManager.indicators.set(playerNumber, indicator);
            frameHitboxOffsets.clear();
            offsetHitboxes();
            offsetHitboxes();
        }
    }

    public void changeSpecies()
    {
        int newWidth = 0;
        int newHeight = 0;
        String newPath = null;
        int newFrames = 0;

        WeaponComponent weapon = (WeaponComponent) this.findComponent("Weapon");
        if(isGoose)
        {
            switch (weapon.getSubTag())
            {
                case "rocketLauncher":
                newWidth = 114;
                newHeight = 92;
                newPath = "/Goose_rocketLauncher.png";
                newFrames = 16;
                baseHeight = 100;
                break;

                case "grenadeLauncher":
                    newWidth = 91;
                    newHeight = 93;
                    newPath = "/Goose_grenadeLauncher.png";
                    newFrames = 16;
                    baseHeight = 100;
                    break;

                case "cannon":
                    newWidth = 101;
                    newHeight = 127;
                    newPath = "/Goose_cannon.png";
                    newFrames = 8;
                    break;
            }
            widthDifference = newWidth - this.width;
            heightDifference = newHeight - this.height;
            this.paddingSide += (int)(widthDifference * 1.5);
            this.paddingTop += (int)(heightDifference * 1.5);
            changeSprite(newWidth, newHeight, newPath, newFrames, 0.1f);
            this.getObjImage().changeColor(skinColors[1], skinColors[skIndex]);
            frameHitboxOffsets.clear();
            offsetHitboxes();
            offsetHitboxes();
            if(GameManager.timePedestals.size() != 0)
            {
                GameManager.timePedestals.get(playerNumber).setFrame(1);
            }
            return;
        }
        else
            {
                switch (weapon.getSubTag())
                {
                    case "rocketLauncher":
                        newWidth = 102;
                        newHeight = 81;
                        newPath = "/Duck_rocketLauncher.png";
                        newFrames = 16;
                        baseHeight = 68;
                        break;

                    case "grenadeLauncher":
                        newWidth = 78;
                        newHeight = 69;
                        newPath = "/Duck_grenadeLauncher.png";
                        newFrames = 16;
                        baseHeight = 68;
                        break;

                    case "cannon":
                        newWidth = 101;
                        newHeight = 103;
                        newPath = "/Duck_cannon.png";
                        newFrames = 8;
                        break;
                }
                widthDifference = newWidth - this.width;
                heightDifference = newHeight - this.height;
                this.paddingSide += (int)(widthDifference * 1.5);
                this.paddingTop += (int)(heightDifference * 1.5);
                changeSprite(newWidth, newHeight, newPath, newFrames, 0.1f);
                this.getObjImage().changeColor(skinColors[1], skinColors[skIndex]);
                frameHitboxOffsets.clear();
                offsetHitboxes();
                offsetHitboxes();
                if(GameManager.timePedestals.size() != 0)
                {
                    GameManager.timePedestals.get(playerNumber).setFrame(0);
                }
                return;
            }
    }

    public void offsetHitboxes()
    {
        frameHitboxOffsets.add(0, new Vector(0,0));
        frameHitboxOffsets.add(1, new Vector(0,0));
        frameHitboxOffsets.add(2, new Vector(-(width / 8), 0));
        frameHitboxOffsets.add(3, new Vector(0,0));
        frameHitboxOffsets.add(4, new Vector(0, 0));
        frameHitboxOffsets.add(5, new Vector(0,0));
        frameHitboxOffsets.add(6, new Vector((width / 8),0));
        frameHitboxOffsets.add(7, new Vector(0,0));
    }

    public int getKeyColor1() {
        return keyColor1;
    }

    public void setKeyColor1(int keyColor1) {
        this.keyColor1 = keyColor1;
    }

    public int getKeyColor2() {
        return keyColor2;
    }

    public void setKeyColor2(int keyColor2) {
        this.keyColor2 = keyColor2;
    }

    public boolean isGrabbed() {
        return isGrabbed;
    }

    public void setGrabbed(boolean grabbed) {
        isGrabbed = grabbed;
    }

    public boolean isTimedOut() {
        return isTimedOut;
    }

    public void setTimedOut(boolean timedOut) {
        isTimedOut = timedOut;
    }
    /*public int getKeyAltShoot() {
        return keyAltShoot;
    }

    public void setKeyAltShoot(int keyAltShoot) {
        this.keyAltShoot = keyAltShoot;
    }*/

    public boolean isNearSelect() {
        return nearSelect;
    }

    public void setNearSelect(boolean nearSelect) {
        this.nearSelect = nearSelect;
    }
    public boolean isSelecting() {
        return selecting;
    }

    public void setSelecting(boolean selecting) {
        this.selecting = selecting;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public int[] getSkinColors() {
        return skinColors;
    }

    public void setSkinColors(int[] skinColors) {
        this.skinColors = skinColors;
    }

    public int getSkIndex() {
        return skIndex;
    }

    public void setSkIndex(int skIndex) {
        this.skIndex = skIndex;
    }
    public ArrayList<Vector> getFrameHitboxOffsets() {
        return frameHitboxOffsets;
    }

    public void setFrameHitboxOffsets(ArrayList<Vector> frameHitboxOffsets) {
        this.frameHitboxOffsets = frameHitboxOffsets;
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    public void setBaseHeight(int baseHeight) {
        this.baseHeight = baseHeight;
    }

    public void addTime(float time)
    {
        this.time += time;
    }

    public void depleteTime(float time)
    {
        this.time -= time;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public float getrTrigger() {
        return rTrigger;
    }

    public void setrTrigger(float rTrigger) {
        this.rTrigger = rTrigger;
    }

    public float getlTrigger() {
        return lTrigger;
    }

    public void setlTrigger(float lTrigger) {
        this.lTrigger = lTrigger;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getSecond() {
        return second;
    }

    public void setSecond(float second) {
        this.second = second;
    }

    public float getTempSecond() {
        return tempSecond;
    }

    public void setTempSecond(float tempSecond) {
        this.tempSecond = tempSecond;
    }

    public boolean isGoose() {
        return isGoose;
    }

    public void setGoose(boolean goose) {
        isGoose = goose;
    }

    /*public int getKeyShoot() {
        return keyShoot;
    }

    public void setKeyShoot(int keyShoot) {
        this.keyShoot = keyShoot;
    }*/
    public int getKeyUp() {
        return keyUp;
    }

    public void setKeyUp(int keyUp) {
        this.keyUp = keyUp;
    }

    public int getKeyDown() {
        return keyDown;
    }

    public void setKeyDown(int keyDown) {
        this.keyDown = keyDown;
    }

    public int getKeyLeft() {
        return keyLeft;
    }

    public void setKeyLeft(int keyLeft) {
        this.keyLeft = keyLeft;
    }

    public int getKeyRight() {
        return keyRight;
    }

    public void setKeyRight(int keyRight) {
        this.keyRight = keyRight;
    }

    public int getKeyDropIn() {
        return keyDropIn;
    }

    public void setKeyDropIn(int keyDropIn) {
        this.keyDropIn = keyDropIn;
    }

    public int getKeySelect() {
        return keySelect;
    }

    public void setKeySelect(int keySelect) {
        this.keySelect = keySelect;
    }
}
