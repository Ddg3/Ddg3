package com.zach.ddg3;

import com.ivan.xinput.*;
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

    public XInputComponentsDelta getDelta() {
        return delta;
    }

    public void setDelta(XInputComponentsDelta delta) {
        this.delta = delta;
    }

    public XInputButtonsDelta getButtons() {
        return buttons;
    }

    public void setButtons(XInputButtonsDelta buttons) {
        this.buttons = buttons;
    }

    public XInputAxesDelta getAxes() {
        return axes;
    }

    public void setAxes(XInputAxesDelta axes) {
        this.axes = axes;
    }

    private XInputAxesDelta axes;

    private boolean lockedEven = false;
    private boolean collidingTop = false;
    private boolean collidingBottom = false;
    private boolean collidingLeft = false;
    private boolean collidingRight = false;

    public float getlStickX() {
        return lStickX;
    }

    public void setlStickX(float lStickX) {
        this.lStickX = lStickX;
    }

    public float getlStickY() {
        return lStickY;
    }

    public void setlStickY(float lStickY) {
        this.lStickY = lStickY;
    }

    public float getrStickX() {
        return rStickX;
    }

    public void setrStickX(float rStickX) {
        this.rStickX = rStickX;
    }

    public float getrStickY() {
        return rStickY;
    }

    public void setrStickY(float rStickY) {
        this.rStickY = rStickY;
    }

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

    private int baseHeightG = 77;
    private int baseWidthG = 81;
    private int basePaddingTopG = 15;
    private int basePaddingSideG = 10;

    public int getPaddingSideHead() {
        return paddingSideHead;
    }

    public void setPaddingSideHead(int paddingSideHead) {
        this.paddingSideHead = paddingSideHead;
    }

    public int getPaddingTopHead() {
        return paddingTopHead;
    }

    public void setPaddingTopHead(int paddingTopHead) {
        this.paddingTopHead = paddingTopHead;
    }

    public int[] getOffsetCenterXHead() {
        return offsetCenterXHead;
    }

    public void setOffsetCenterXHead(int[] offsetCenterXHead) {
        this.offsetCenterXHead = offsetCenterXHead;
    }

    public int[] getOffsetCenterYHead() {
        return offsetCenterYHead;
    }

    public void setOffsetCenterYHead(int[] offsetCenterYHead) {
        this.offsetCenterYHead = offsetCenterYHead;
    }

    private int paddingTopHead = 46;
    private int paddingSideHead = 22;
    private int[] offsetCenterXHead = {0, 3, 6, 0, -1, 0, -6, -4};
    private int[] offsetCenterYHead = {-17, -17, -17, -22, -18, -22, -17, -17};

    private int[] paddingTopBody = {36, 38, 38, 32, 38, 32, 38, 36};
    private int[] paddingSideBody = {18, 8, 8, 18, 19,18,8,8};
    private int[] offsetCenterXBody = {0, -5, -4, -2, -1, 2, 4, 5};
    private int[] offsetCenterYBody = {12, 12, 12, 10, 11, 10, 12, 12};

    private int tempPaddingTopHead = 46;
    private int tempPaddingSideHead = 22;
    private int[] tempPaddingTopBody = {36, 38, 38, 32, 38, 32, 38, 36};
    private int[] tempPaddingSideBody = {18, 8, 8, 18, 19,18,8,8};

    private int tempGooseWidth = 114;
    private int tempGooseHeight = 92;

    private int paddingTopHeadG = 48;
    private int paddingSideHeadG = 46;
    private int[] paddingSideBodyG = new int[]{38, 32, 28, 34, 40, 34, 28, 32};
    private int[] paddingTopBodyG = new int[]{58, 60, 64, 58, 60, 58, 64, 60};

    private int tempPaddingSideHeadG = 48;
    private int tempPaddingTopHeadG = 46;
    private int[] tempPaddingSideBodyG = new int[]{38, 32, 28, 34, 40, 34, 28, 32};
    private int[] tempPaddingTopBodyG = new int[]{58, 60, 64, 58, 60, 58, 64, 60};

    public int[] getPaddingTopBody() {
        return paddingTopBody;
    }

    public void setPaddingTopBody(int[] paddingTopBody) {
        this.paddingTopBody = paddingTopBody;
    }

    public int[] getPaddingSideBody() {
        return paddingSideBody;
    }

    public void setPaddingSideBody(int[] paddingSideBody) {
        this.paddingSideBody = paddingSideBody;
    }

    public int[] getOffsetCenterXBody() {
        return offsetCenterXBody;
    }

    public void setOffsetCenterXBody(int[] offsetCenterXBody) {
        this.offsetCenterXBody = offsetCenterXBody;
    }

    public int[] getOffsetCenterYBody() {
        return offsetCenterYBody;
    }

    public void setOffsetCenterYBody(int[] offsetCenterYBody) {
        this.offsetCenterYBody = offsetCenterYBody;
    }

    private int[] skinColors = new int[8];
    private int skIndex = 1;
    private boolean rainbow;

    private boolean isRemoved = false;
    private boolean nearSelect = false;

    public boolean isNearSelect2() {
        return nearSelect2;
    }

    public void setNearSelect2(boolean nearSelect2) {
        this.nearSelect2 = nearSelect2;
    }

    private boolean nearSelect2 = false;
    private int selectIndex = 0;
    private boolean isTimedOut = false;
    private boolean isGrabbed = false;
    private boolean colorKeyUp = true;

    private boolean isStunned = false;
    private float stunTimer = 2.5f;
    private float tempStun = stunTimer;

    private boolean rumbling;
    private float tempRumbleTimer = 0;
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    private float speed = 150f;

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

    XInputComponents components;
    XInputButtons buttonStates;
    XInputAxes axesStates;

    public Player(String name, int width, int height, String path, int totalFrames, float frameLife, int playerNumber)
    {
        super(name, width, height, path, totalFrames, frameLife);
        this.tag = "Player";
        this.playerNumber = playerNumber;
        device = GameManager.deviceManager.devices[playerNumber];
        delta = GameManager.deviceManager.deltas[playerNumber];
        buttons = GameManager.deviceManager.buttons[playerNumber];
        axes = GameManager.deviceManager.axes[playerNumber];
        components = device.getComponents();
        buttonStates = components.getButtons();
        axesStates = components.getAxes();
        //GameManager.deviceManager.addComponents(device, delta, buttons, axes);
        this.addComponent(new AABBComponent(this, "player"));
        this.addComponent(new AABBComponent(this, "head"));
        this.addComponent(new AABBComponent(this, "body"));

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
        nearSelect2 = false;
        selected = false;
        collidingTop = false;
        collidingBottom = false;
        collidingLeft = false;
        collidingRight = false;

        if(device.poll())
        {
            lStickX = axesStates.lx;
            lStickY = axesStates.ly;
            rStickX = axesStates.rx;
            rStickY = axesStates.ry;
            System.out.println("X: " + lStickX + ", Y: " + lStickY);

            if(this.isInGame() && !selecting && !isTimedOut && !isStunned && !mainLevel.starting)
            {
                cameraCollision();
                moveController(dt);
                look();
                rainbowSkin();
            }
            if(this.isInGame() && !selecting && !isTimedOut && !isStunned && GameManager.gameLevelManager.getGameState() != GameLevelManager.GameState.MAIN_STATE)
            {
                changeSkin(gameManager, main);
            }
            if(buttons.isPressed(XInputButton.START) && this.isInGame())
            {
                GameManager.pausePlayer = this;
                GameManager.isPlaying = false;
            }
            if(rumbling)
            {
                tempRumbleTimer -= dt;
                if(tempRumbleTimer <= 0)
                {
                    rumbling = false;
                    device.setVibration(0,0);
                }
            }
            //System.out.println(offsetPos.x);
           /*this.offsetPos.x = main.getInput().getMouseX() - this.width;
           this.offsetPos.y = main.getInput().getMouseY() + this.height + 180;*/
        }
        else if(isKeyBoard)
        {
            if(this.isInGame() && !selecting && !isTimedOut && !isStunned && !mainLevel.starting)
            {
                cameraCollision();
                moveKeyboard(main, dt);
                lookKeyboard(main, gameManager);
                changeSkin(gameManager, main);
            }
            if(main.getInput().isKey(KeyEvent.VK_ESCAPE) && this.isInGame())
            {
                GameManager.pausePlayer = this;
                GameManager.isPlaying = false;
            }
        }

        dropIn(dt, main);

        if(selecting)
        {
            select(selection, main);
        }
        else if(isKnocked())
            {
                //updateKnockback();
            }

        this.offsetPos.x = (int)(this.position.x - (this.width / 2) + 320);
        this.offsetPos.y = (int)(this.position.y - (this.height / 2) + 180);
        this.updateComponents(main, gameManager, dt);
        this.animate(dt);

        if(GameManager.gameLevelManager.gameState == GameLevelManager.GameState.MAIN_STATE && !mainLevel.starting && GameManager.isPlaying)
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
                timer.setFrame(0);
            }

            if(time > 0)
            {
                timer.setFrame(time);
            }
            /*timer.setPosition(this.position.x, this.position.y - 50);
            timer.visible = true;*/
        }
        else if(GameManager.gameLevelManager.gameState == GameLevelManager.GameState.SELECTION_STATE)
        {
            if(playerNumber == 0)
            {
                if(selectionLevel.getxButton1() != null && GameManager.timePedestals.get(0) != null)
                {
                    selectionLevel.getxButton1().setPosition(new Vector(GameManager.center.position.x - 300, GameManager.center.position.y + 114));
                    selectionLevel.getxButton1().visible = true;
                }
                if(selectionLevel.getyButton1() != null && GameManager.timePedestals.get(0) != null)
                {
                    selectionLevel.getyButton1().setPosition(new Vector(GameManager.center.position.x - 283, GameManager.center.position.y + 114));
                    selectionLevel.getyButton1().visible = true;
                }
            }
            else
                {
                    if(selectionLevel.getxButton2() != null && GameManager.timePedestals.get(1) != null)
                    {
                        selectionLevel.getxButton2().setPosition(new Vector(GameManager.center.position.x - 135, GameManager.center.position.y + 114));
                        selectionLevel.getxButton2().visible = true;
                    }
                    if(selectionLevel.getyButton2() != null && GameManager.timePedestals.get(1) != null)
                    {
                        selectionLevel.getyButton2().setPosition(new Vector(GameManager.center.position.x - 118, GameManager.center.position.y + 114));
                        selectionLevel.getyButton2().visible = true;
                    }
                }
        }
        if(isGoose)
        {
            tempSecond -= dt;
        }

        if(isStunned)
        {
            stars.setFrameLife(0.075f);
            tempStun -= dt;
            stars.position = new Vector(this.position.x, this.position.y - (this.height / 2));

            if(tempStun <= 0)
            {
                isStunned = false;
                tempStun = stunTimer;
                GameManager.objects.remove(stars);
            }
        }
    }

    public void lockTurning(boolean even)
    {
        if(even)
        {
            lockedEven = true;
        }
    }

    public void unlockTurning()
    {
        lockedEven = false;
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
        if((other.getTag().equalsIgnoreCase("Wall") || (other.getTag().equalsIgnoreCase("Pelican") && (AABBComponent) other.findComponentBySubtag("wall") != null)) && this.isInGame() && !isTimedOut)
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
                if(other.position.x <= 0)
                {
                    nearSelect = true;
                }
                else
                {
                    nearSelect2 = true;
                }
                if ((this.device.getDelta().getButtons().isPressed(XInputButton.A) || main.getInput().isKeyDown(keySelect)) && this.isInGame() && !selecting && !selected)
                {
                    if(other.position.x <= 0)
                    {
                        selectIndex = 0;
                    }
                    else
                        {
                            selectIndex = 1;
                        }
                    selectionLevel.getbButton1().visible = false;
                    selectionLevel.getbButton2().visible = false;
                    selecting = true;
                    selection = other;
                    selection.setFrame(1);
                }

                if ((this.device.getDelta().getButtons().isPressed(XInputButton.B) || main.getInput().isKey(keyDeselect)) && this.isInGame() && selecting)
                {
                    selectionLevel.getbButton1().visible = false;
                    selectionLevel.getbButton2().visible = false;
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
            rumble(50000, 3f);
        }
    }

    public void changeSkin(GameManager gameManager, Main main) {
        int oldSkindex = skIndex;
        if (GameManager.gameLevelManager.getGameState() == GameLevelManager.GameState.SELECTION_STATE) {
            if (!colorKeyUp && (isKeyBoard && (main.getInput().isKeyUp(keyColor1) || main.getInput().isKeyUp(keyColor2)))) {
                colorKeyUp = true;
            }
            if (buttons.isPressed(XInputButton.X) || (isKeyBoard && main.getInput().isKey(keyColor1) && colorKeyUp)) {
                if (skIndex == 0) {
                    skIndex = 7;
                } else {
                    skIndex--;
                    colorKeyUp = false;
                }
                if (playerNumber == 0) {
                    if (selectionLevel.getxButton1() != null)
                        GameManager.objects.remove(selectionLevel.getxButton1());
                } else {
                    if (selectionLevel.getxButton2() != null)
                        GameManager.objects.remove(selectionLevel.getxButton2());
                }
            }
        }
        if (buttons.isPressed(XInputButton.Y) || (isKeyBoard && main.getInput().isKey(keyColor2) && colorKeyUp))
        {
            if (skIndex == 7) {
                skIndex = 0;
            } else
                {
                skIndex++;
                colorKeyUp = false;
            }

            if (playerNumber == 0)
            {
                if (selectionLevel.getyButton1() != null)
                    GameManager.objects.remove(selectionLevel.getyButton1());
            } else
                {
                if (selectionLevel.getyButton2() != null)
                    GameManager.objects.remove(selectionLevel.getyButton2());
            }
        }
            this.getObjImage().changeColor(skinColors[oldSkindex], skinColors[skIndex]);

            GameManager.timePedestals.get(playerNumber).getObjImage().changeColor(skinColors[oldSkindex],
                    skinColors[skIndex]);

            if(gameManager.gameLevelManager.getGameState() == GameLevelManager.GameState.SELECTION_STATE)
            {
                selectionLevel.getExplosiveGuns()[playerNumber].getObjImage().changeColor(skinColors[oldSkindex], skinColors[skIndex]);
                selectionLevel.getExplosiveGuns()[playerNumber + 2].getObjImage().changeColor(skinColors[oldSkindex], skinColors[skIndex]);
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
                this.position.x += speed * dt;
            }
            if (rStickX < 0.4f && rStickX > -0.4f)
            {
                this.setFrame(2 + this.getFrameOffset());
            }
        }
        //Right
        if (lStickX < -0.4f)
        {
            if(!collidingLeft)
            {
                this.position.x -= speed * dt;
            }
            if (rStickX < 0.4f && rStickX > -0.4f) {
                this.setFrame(6 + this.getFrameOffset());
            }
        }

        //Down
        if (lStickY > 0.4f)
        {
            if(!collidingTop)
            {
                this.position.y -= speed * dt;
            }
            if (rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(4 + this.getFrameOffset());
            }
        }

        //Up
        if (lStickY < -0.4f)
        {
            if(!collidingBottom)
            {
                this.position.y += speed * dt;
            }
            if (rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(0 + this.getFrameOffset());
            }
        }

        if(lStickY < 0.4f && lStickY > -0.4f)
        {
            //Idle on Y
            collidingTop = false;
        }

        if(!lockedEven)
        {
            //Changes frame of animation on the diagonals if the right thumbstick is not being used
            if (lStickX > 0.4f && lStickY < -0.4f) {
                if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                    this.setFrame(1 + this.getFrameOffset());
                }
            }
            if (lStickX > 0.4f && lStickY > 0.4f) {
                if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                    this.setFrame(3 + this.getFrameOffset());
                }
            }
            if (lStickY > 0.4f && lStickX < -0.4f) {
                if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                    this.setFrame(5 + this.getFrameOffset());
                }
            }
            if (lStickY < -0.4f && lStickX < -0.4f) {
                if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                    this.setFrame(7 + this.getFrameOffset());
                }
            }
        }
    }

    public void look()
    {
        if (rStickX > 0.4f)
        {
            //Left
            this.setFrame(2 + this.getFrameOffset());
        }
        if (rStickX < -0.4f) {
            //Right
            this.setFrame(6 + this.getFrameOffset());
        }
        if (rStickY > 0.4f) {
            //Down
            this.setFrame(4 + this.getFrameOffset());
        }
        if (rStickY < -0.4f) {
            //Up
            this.setFrame(0 + this.getFrameOffset());
        }
        if(!lockedEven)
        {
        if (rStickX > 0.4f && rStickY < -0.4f) {
            //Left and Up
            this.setFrame(1 + this.getFrameOffset());
        }
        if (rStickX > 0.4f && rStickY > 0.4f) {
            //Left and Down
            this.setFrame(3 + this.getFrameOffset());
        }
        if (rStickY > 0.4f && rStickX < -0.4f) {
            //Right and Down
            this.setFrame(5 + this.getFrameOffset());
        }
        if (rStickY < -0.4f && rStickX < -0.4f) {
            //Right and Up
            this.setFrame(7 + this.getFrameOffset());
        }
        }
    }

    public void moveKeyboard(Main main, float dt)
    {
        if (main.getInput().isKey(keyLeft))
        {
            if(!collidingRight)
            {
                this.position.x += -speed * dt;
            }
            this.setFrame(6 + this.getFrameOffset());

        }
        if (main.getInput().isKey(keyRight))
        {
            if(!collidingLeft)
            {
                this.position.x += speed * dt;
            }
            this.setFrame(2 + this.getFrameOffset());
        }

        if (main.getInput().isKey(keyDown))
        {
            if(!collidingTop)
            {
                this.position.y += speed * dt;
            }
            this.setFrame(0 + this.getFrameOffset());
        }
        if (main.getInput().isKey(keyUp))
        {
            if(!collidingBottom)
            {
                this.position.y += -speed * dt;
            }
            this.setFrame(4 + this.getFrameOffset());
        }

        if(!lockedEven)
        {
            //Changes frame of animation on the diagonals if the right thumbstick is not being used
            if (main.getInput().isKey(keyLeft) && main.getInput().isKey(keyUp)) {
                this.setFrame(5 + this.getFrameOffset());
            }
            if (main.getInput().isKey(keyLeft) && main.getInput().isKey(keyDown)) {
                this.setFrame(7 + this.getFrameOffset());
            }
            if (main.getInput().isKey(keyRight) && main.getInput().isKey(keyDown)) {
                this.setFrame(1 + this.getFrameOffset());
            }
            if (main.getInput().isKey(keyRight) && main.getInput().isKey(keyUp)) {
                this.setFrame(3 + this.getFrameOffset());
            }
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
        if(!lockedEven)
        {
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
    }

    public void rumble(int power, float time)
    {
        if(device.poll())
        {
            device.setVibration(power, power);
            rumbling = true;
            tempRumbleTimer = time;
        }
    }
    public void dropIn(float dt, Main main)
    {
        if((this.device.getDelta().getButtons().isPressed(XInputButton.START) || ( isKeyBoard && main.getInput().isKey(keyDropIn))) && !this.isInGame() && !this.droppingIn)
        {
            this.droppingIn = true;
            this.visible = true;
            this.playInRangeAndBack(8, 22);
            this.zIndex = Integer.MAX_VALUE - 1;
        }

        if(this.position.y >= GameManager.center.position.y && droppingIn)
        {
            droppingIn = false;
            this.stop();
            this.setInGame(true);
            this.setFrame(1);
            this.zIndex = 5;
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
        if(selectIndex == 0)
        {
            selectionLevel.getbButton1().visible = true;
        }
        else
            {
                selectionLevel.getbButton2().visible = true;
            }
        //Left
        if(!stickSelecting)
        {
            if (lStickX > 0.4f)
            {
                if (selection.getFrame() == 1) {
                    selection.setFrame(2);
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
                if (selection.getFrame() == 2)
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
                    selection.setFrame(2);
                    return;

                }
                else
                {
                    selection.goToPrevFrame();
                }
            }

            //Right
            if (main.getInput().isKeyDown(keyRight)) {
                if (selection.getFrame() == 2)
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
            int gooseWidth = 0;
            int gooseHeight = 0;

            removeComponent("Weapon");
            switch (selection.getFrame())
            {
                case 1:
                    if(selectIndex == 0)
                    {
                        this.addComponent(new WeaponComponent(this, "rocketLauncher"));
                        newWidth = 102;
                        newHeight = 81;
                        newPath = "/Duck_rocketLauncher.png";
                        newFrames = 16;

                        gooseWidth = 114;
                        gooseHeight = 92;

                        indWidth = 43;
                        indHeight = 29;
                        indPath = "/missileIndicator.png";
                    }
                    else
                        {
                            this.addComponent(new WeaponComponent(this, "cannon"));
                            newWidth = 101;
                            newHeight = 103;
                            newPath = "/Duck_cannon.png";
                            newFrames = 8;

                            gooseWidth = 101;
                            gooseHeight = 127;

                            indWidth = 23;
                            indHeight = 30;
                            indPath = "/stunBombIndicator.png";
                        }
                    break;

                case 2:
                    if(selectIndex == 0)
                    {
                        this.addComponent(new WeaponComponent(this, "grenadeLauncher"));
                        newWidth = 78;
                        newHeight = 69;
                        newPath = "/Duck_grenadeLauncher.png";
                        newFrames = 16;

                        gooseWidth = 91;
                        gooseHeight = 93;

                        indWidth = 36;
                        indHeight = 43;
                        indPath = "/mirvIndicator.png";
                    }
                    else
                        {
                            this.addComponent(new WeaponComponent(this, "sniper"));
                            newWidth = 101;
                            newHeight = 76;
                            newPath = "/Duck_sniper.png";
                            newFrames = 8;

                            gooseWidth = 108;
                            gooseHeight = 92;

                            indWidth = 29;
                            indHeight = 33;
                            indPath = "/scopeIndicator.png";
                        }
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

            int gooseWidthDifference = tempGooseWidth - gooseWidth;
            int gooseHeightDifference = tempGooseHeight - gooseHeight;
            tempGooseWidth -= gooseWidthDifference;
            tempGooseHeight -= gooseHeightDifference;
            offsetGoosePadding((int)(gooseWidthDifference / 1.8), gooseHeightDifference);
            setGooseTempPaddingValues();

            //System.out.println(gooseWidthDifference +", "+ gooseHeightDifference);

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
            WeaponComponent weapon = (WeaponComponent) this.findComponent("Weapon");
            offsetPadding((int)(widthDifference / 1.8), heightDifference);
            setTempPaddingValues();
            offsetSubHitboxes(weapon.getSubTag());
            selectionLevel.getbButton1().visible = false;
            selectionLevel.getbButton2().visible = false;
            weapon.setTempAltCooldown(0);
            indicator.visible = true;
            GameManager.altReloadText.get(playerNumber).visible = false;
        }
    }

    public void changeSpecies()
    {
        rumble(40000, 0.4f);
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

                case "sniper":
                    newWidth = 108;
                    newHeight = 92;
                    newPath = "/Goose_sniper.png";
                    newFrames = 8;
                    break;
            }

            widthDifference = newWidth - this.width;
            heightDifference = newHeight - this.height;
            this.paddingSide += (widthDifference / 2);
            this.paddingTop += (heightDifference / 2);

            if((this.width / 2) - this.paddingSide < (this.baseWidthG / 2) - basePaddingSideG)
            {
                this.paddingSide = basePaddingSideG;
            }
            if((this.height / 2) - this.paddingTop < (this.baseHeightG / 2) - basePaddingTopG)
            {
                this.paddingTop = basePaddingTopG;
            }

            changeSprite(newWidth, newHeight, newPath, newFrames, 0.1f);
            this.getObjImage().changeColor(skinColors[1], skinColors[skIndex]);
            frameHitboxOffsets.clear();
            offsetHitboxes();
            offsetHitboxes();
            //offsetSubHitboxes(widthDifference);
            offsetSubHitboxes(weapon.getSubTag());
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
                    case "sniper":
                        newWidth = 101;
                        newHeight = 76;
                        newPath = "/Duck_sniper.png";
                        newFrames = 8;
                        break;
                }
                widthDifference = this.width - newWidth;
                heightDifference = this.height - newHeight;
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
                changeSprite(newWidth, newHeight, newPath, newFrames, 0.1f);
                this.getObjImage().changeColor(skinColors[1], skinColors[skIndex]);
                frameHitboxOffsets.clear();
                offsetHitboxes();
                offsetHitboxes();
                offsetSubHitboxes(weapon.getSubTag());
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

    public void setTempPaddingValues()
    {
        tempPaddingTopHead = paddingTopHead;
        tempPaddingSideHead = paddingSideHead;
        tempPaddingTopBody = paddingTopBody;
        tempPaddingSideBody = paddingSideBody;
    }
    public void setGooseTempPaddingValues()
    {
        tempPaddingTopHeadG = paddingTopHeadG;
        tempPaddingSideHeadG = paddingSideHeadG;
        tempPaddingTopBodyG = paddingTopBodyG;
        tempPaddingSideBodyG = paddingSideBodyG;
    }
    public void setPaddingValuesToTemps()
    {
        paddingTopHead = tempPaddingTopHead;
        paddingSideHead = tempPaddingSideHead;
        paddingTopBody = tempPaddingTopBody;
        paddingSideBody = tempPaddingSideBody;
    }
    public void setGoosePaddingValuesToTemps()
    {
        paddingTopHeadG = tempPaddingTopHeadG;
        paddingSideHeadG = tempPaddingSideHeadG;
        paddingTopBodyG = tempPaddingTopBodyG;
        paddingSideBodyG = tempPaddingSideBodyG;
    }
    public void offsetPadding(int widthDiff, int heightDiff)
    {
        for(int i = 0; i < paddingSideBody.length; i++)
        {
            paddingSideBody[i] += widthDiff;
            paddingTopBody[i] += heightDiff;
        }

        paddingSideHead += widthDiff;
        paddingTopHead += heightDiff;
    }

    public void offsetGoosePadding(int widthDiff, int heightDiff)
    {
        for(int i = 0; i < paddingSideBodyG.length; i++)
        {
            paddingSideBodyG[i] -= widthDiff;
            paddingTopBodyG[i] -= heightDiff;
        }

        paddingSideHeadG -= widthDiff;
        paddingTopHeadG -= heightDiff;
    }

    public void setPaddingToGoose()
    {
        paddingTopHead = paddingTopHeadG;
        paddingSideHead = paddingSideHeadG;
        for(int i = 0; i < paddingTopBody.length; i++)
        {
            /*paddingTopBody[i] = paddingTopBodyG[i];
            System.out.println(paddingTopBody[i]);
            paddingSideBody[i] = paddingSideBodyG[i];
            System.out.println(paddingSideBody[i]);*/
            //System.out.print(paddingTopBodyG[i] + ", ");
            //paddingSideBody = new int[]{38, 32, 28, 34, 40, 34, 28, 32};
            //paddingTopBody = new int[]{58, 60, 64, 58, 60, 58, 64, 60};
        }
        //System.out.println();
        paddingTopBody = paddingTopBodyG;
        paddingSideBody = paddingSideBodyG;
    }

    public void offsetSubHitboxes(String tag)
    {
        if(!isGoose)
        {
            setPaddingValuesToTemps();
            switch (tag)
            {
                case "rocketLauncher":
                    offsetCenterXHead = new int[]{0, 0, -15, 0, 0, -1, 14, -1};
                    offsetCenterYHead = new int[]{-17, -17, -17, -16, -18, -16, -17, -17};
                    offsetCenterXBody = new int[]{0, -10, -23, -3, 0, 3, 23, 10};
                    offsetCenterYBody = new int[]{12, 12, 12, 16, 9, 16, 12, 12};
                    break;
                case "grenadeLauncher":
                    offsetCenterXHead = new int[]{-1, -1, -4, -4, 0, 4, 3, -1};
                    offsetCenterYHead = new int[]{-21, -21, -21, -21, -21, -21, -21, -21};
                    offsetCenterXBody = new int[]{0, -10, -12, -6, 0, 6, 12, 10};
                    offsetCenterYBody = new int[]{8, 8, 8, 10, 6, 10, 8, 8};
                    break;
                case "cannon":
                    offsetCenterXHead = new int[]{0, 10, 4, 0, 0, 0, -4, -10};
                    offsetCenterYHead = new int[]{-38, -38, -38, -38, -38, -38, -38, -38};
                    offsetCenterXBody = new int[]{0, 2, -4, 0, 0, 0, 4, -2};
                    offsetCenterYBody = new int[]{-9, -9, -9, -7, -11, -7, -9, -9};
                    break;
                case "sniper":
                    offsetCenterXHead = new int[]{0, -1, -14, -1, 0, 0, 14, -1};
                    offsetCenterYHead = new int[]{-17, -17, -17, -16, -17, -17, -17, -17};
                    offsetCenterXBody = new int[]{0, -10, -23, -3, 0, 3, 23, 10};
                    offsetCenterYBody = new int[]{12, 12, 12, 16, 9, 16, 12, 12};
                    break;
            }
        }
        else
            {
                setGoosePaddingValuesToTemps();
                setPaddingToGoose();
                switch (tag)
                {
                    case "rocketLauncher":
                        /*paddingSideHead = 48;
                        paddingTopHead = 46;
                        paddingSideBody = new int[]{38, 32, 28, 34, 40, 34, 28, 32};
                        paddingTopBody = new int[]{58, 60, 64, 58, 60, 58, 64, 60};*/

                        offsetCenterXHead = new int[]{-1, -1, -3, 0, -1, 0, 3, 0};
                        offsetCenterYHead = new int[]{-20, -20, -21, -21, -21, -21, -21, -20};
                        offsetCenterXBody = new int[]{-1, -10, -23, -10, -1, 10, 23, 10};
                        offsetCenterYBody = new int[]{12, 12, 16, 19, 13, 19, 16, 12};
                        break;

                    case "grenadeLauncher":
                        /*paddingSideHead = 36;
                        paddingTopHead = 44;
                        paddingSideBody = new int[]{28, 32, 16, 34, 40, 34, 28, 32};
                        paddingTopBody = new int[]{58, 60, 64, 58, 60, 58, 64, 60};*/

                        offsetCenterXHead = new int[]{0, 1, 8, 0, 0, 0, -9, -1};
                        offsetCenterYHead = new int[]{-23, -23, -23, -23, -23, -23, -23, -23};
                        offsetCenterXBody = new int[]{0, -10, -12, -6, 0, 6, 12, 10};
                        offsetCenterYBody = new int[]{13, 13, 15, 16, 14, 16, 15, 13};
                        break;

                    case "cannon":
                        /*paddingSideHead = 48;
                        paddingTopHead = 46;
                        paddingSideBody = new int[]{38, 32, 28, 34, 40, 34, 28, 32};
                        paddingTopBody = new int[]{58, 60, 64, 58, 60, 58, 64, 60};*/

                        offsetCenterXHead = new int[]{0, 19, 17, 0, 0, 0, -17, -19};
                        offsetCenterYHead = new int[]{-40, -40, -40, -40, -40, -40, -40, -40};
                        offsetCenterXBody = new int[]{0, 6, -4, -3, 0, -3, 4, -6};
                        offsetCenterYBody = new int[]{-6, -4, -2, -1, -4, -1, -2, -4};
                        break;

                    case "sniper":
                        /*paddingSideHead = 48;
                        paddingTopHead = 46;
                        paddingSideBody = new int[]{38, 32, 28, 34, 40, 34, 28, 32};
                        paddingTopBody = new int[]{58, 60, 64, 58, 60, 58, 64, 60};*/

                        offsetCenterXHead = new int[]{0, -1, -1, -1, 0, 0, 1 -1};
                        offsetCenterYHead = new int[]{-16, -16, -16, -24, -16, -24, -16, -16};
                        offsetCenterXBody = new int[]{0, -10, -23, -6, 0, 6, 21, 10};
                        offsetCenterYBody = new int[]{19, 19, 23, 19, 19, 19, 23, 19};
                        break;
                }
            }
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
