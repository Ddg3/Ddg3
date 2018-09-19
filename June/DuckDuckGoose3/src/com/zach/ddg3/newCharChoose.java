package com.zach.ddg3;

import com.ivan.xinput.XInputAxesDelta;
import com.ivan.xinput.XInputButtonsDelta;
import com.ivan.xinput.XInputComponentsDelta;
import com.ivan.xinput.XInputDevice;
import com.ivan.xinput.enums.XInputAxis;
import com.ivan.xinput.enums.XInputButton;
import edu.digipen.GameLevel;
import edu.digipen.GameLevelManager;
import edu.digipen.GameObject;
import edu.digipen.ObjectManager;

/**
 * Created by Zach on 4/9/2018.
 */
public class newCharChoose extends GameLevel
{
    XInputDevice device0;
    XInputDevice device1;
    XInputDevice device2;
    XInputDevice device3;

    XInputComponentsDelta delta1;
    XInputButtonsDelta buttons1;
    XInputAxesDelta axes1;

    XInputComponentsDelta delta2;
    XInputButtonsDelta buttons2;
    XInputAxesDelta axes2;

    float leftStickX1;
    float leftStickX2;

    public static Weapon pWeapon1 = new Weapon();
    public static Bullet bullet1;

    public static Weapon pWeapon2 = new Weapon();
    public static Bullet bullet2;

    public static String p1tag = "Duck_";
    public static String p2tag = "Goose_";

    boolean p1moved;
    boolean p2moved;

    Breed breed1;

    public static Player player1;
    public static Player player2;

    public GameObject duckRise;
    public GameObject duckRise2;

    public static int[] boundaries = new int[4];

    public boolean p1in;
    public boolean p2in;

    public GameObject prototypeStand;
    public GameObject explosiveStand;
    public GameObject hazardBack;
    public GameObject leftDoor;
    public GameObject rightDoor;
    public GameObject fullStart;

    public static GameObject pointer1;
    public static GameObject pointer2;

    public static float p1LeftX;
    public static float p2LeftX;

    public boolean openDoor;

    private boolean started;
    private GameObject startText;
    
    @Override
    public void create()
    {
        DeviceCheck(0);
        DeviceCheck(1);
        DeviceCheck(2);
        DeviceCheck(3);

        delta1 = device0.getDelta();
        buttons1 = delta1.getButtons();
        axes1 = delta1.getAxes();

        delta2 = device1.getDelta();
        buttons2 = delta2.getButtons();
        axes2 = delta2.getAxes();

        //Graphics.setDrawCollisionData(true);
    }

    @Override
    public void initialize()
    {
        started = false;
        p1in = false;
        p2in = false;
        prototypeStand = new GameObject("prototypeStand", 1133, 387, "prototypeStand.png", 1, 1, 1, 0.1f);
        prototypeStand.setPosition(500, 350);
        prototypeStand.setRectangleCollider(560, 200);
        ObjectManager.addGameObject(prototypeStand);
        prototypeStand.setOpacity(0);

        explosiveStand = new GameObject("explosiveStand", 801, 399, "explosiveStand.png", 1, 1, 1, 0.1f);
        explosiveStand.setPosition(-500, 300);
        explosiveStand.setRectangleCollider(400, 200);
        ObjectManager.addGameObject(explosiveStand);
        explosiveStand.setOpacity(0);

        duckRise = new GameObject("duckRise", 101, 256, "duckRise.png", 41, 1, 41, 0.03f);
        duckRise.setPosition(-400, -350);
        duckRise.animationData.goToFrame(0);
        duckRise.setZOrder(2);
        ObjectManager.addGameObject(duckRise);
        duckRise.setOpacity(0);

        duckRise2 = new GameObject("duckRise2", 101, 256, "duckRise.png", 41, 1, 41, 0.03f);
        duckRise2.setPosition(-100, -350);
        duckRise2.animationData.goToFrame(0);
        duckRise2.setZOrder(2);
        ObjectManager.addGameObject(duckRise2);
        duckRise2.setOpacity(0);

        hazardBack = new GameObject("hazardBack", 1920, 1080, "hazardBack.png", 1, 1, 1, 0.1f);
        hazardBack.setZOrder(-1);
        hazardBack.setOpacity(0);

        leftDoor = new GameObject("leftDoor", 1281, 510, "leftDoor.png", 1, 1, 1, 0.1f);
        leftDoor.setZOrder(-1);
        leftDoor.setPosition(-320, -107);
        ObjectManager.addGameObject(leftDoor);
        leftDoor.setOpacity(0);

        rightDoor = new GameObject("rightDoor", 1149, 510, "rightDoor.png", 1, 1, 1, 0.1f);
        rightDoor.setZOrder(-2);
        rightDoor.setPosition(380, -107);
        ObjectManager.addGameObject(rightDoor);
        rightDoor.setOpacity(0);

        startText = new GameObject("startText", 630, 39, "startText.png", 2, 2, 1, 0.1f);
        startText.setPosition(100, -430);
        ObjectManager.addGameObject(startText);

        boundaries[0] = 900;
        boundaries[1] = -900;
        boundaries[2] = 250;
        boundaries[3] = -400;
    }

    @Override
    public void update(float v)
    {
        p1LeftX += axes1.getDelta(XInputAxis.LEFT_THUMBSTICK_X);
        p2LeftX += axes2.getDelta(XInputAxis.LEFT_THUMBSTICK_X);

        if(pointer1 != null)
        {
            pointerMove(pointer1, device0, 1, player1);
        }
        if(pointer2 != null)
        {
            pointerMove(pointer2, device1, 2, player2);
        }
        if(device0.poll())
        {
            if(buttons1.isPressed(XInputButton.START))
            {
                if(started)
                {
                    duckRise.animationData.play();
                }
                if(!started)
                {
                    start();
                }
            }
        }
        if(device1.poll())
        {
            if(buttons2.isPressed(XInputButton.START))
            {
                if(started)
                {
                    duckRise2.animationData.play();
                }
                if(!started)
                {
                    start();
                }
            }
        }
        if(started)
        {
            if(startText.getOpacity() > 0)
            {
                startText.setOpacity(startText.getOpacity() - 0.01f);
            }
            if(duckRise.getOpacity() < 1)
            {
                duckRise.setOpacity(duckRise.getOpacity() + 0.01f);
            }
            if(duckRise2.getOpacity() < 1)
            {
                duckRise2.setOpacity(duckRise2.getOpacity() + 0.01f);
            }
            if(leftDoor.getOpacity() < 1)
            {
                leftDoor.setOpacity(leftDoor.getOpacity() + 0.01f);
            }
            if(rightDoor.getOpacity() < 1)
            {
                rightDoor.setOpacity(rightDoor.getOpacity() + 0.01f);
            }
            if(hazardBack.getOpacity() < 1)
            {
                hazardBack.setOpacity(hazardBack.getOpacity() + 0.01f);
            }
            if(explosiveStand.getOpacity() < 1)
            {
                explosiveStand.setOpacity(explosiveStand.getOpacity() + 0.01f);
            }
            if(prototypeStand.getOpacity() < 1)
            {
                prototypeStand.setOpacity(prototypeStand.getOpacity() + 0.01f);
            }


        }
        checkStart(duckRise, 1, pWeapon1, device0);
        checkStart(duckRise2, 2, pWeapon2, device1);

        checkReady();

        loadLevel();
    }

    public void start()
    {
        startText.animationData.goToNextFrame();
        //title.kill();

        started = true;
    }
    public void loadLevel()
    {
        if(!leftDoor.isInViewport() && !rightDoor.isInViewport())
        {
            GameLevelManager.goToLevel(new FightLevel());
        }
    }
    public void checkReady()
    {
        if(player1 != null && player2 != null && player1.ready && player2.ready)
        {
            openDoor = true;
        }
        if(openDoor)
        {
            leftDoor.setPositionX(leftDoor.getPositionX() - 8);
            rightDoor.setPositionX(rightDoor.getPositionX() + 8);
        }
    }
    public void checkStart(GameObject duckRiser, int playerNum, Weapon weapon, XInputDevice device)
    {
        if(duckRiser.animationData.getCurrentFrame() == 38)
        {
            if(playerNum == 1 && !p1in)
            {
                p1in = true;
                player1 = new Player("player" + playerNum, 171, 207, "DefaultDuck.png", 32, 1, 32, 0.1f, device, weapon, breed1);
                player1.setPosition(duckRiser.getPosition());
                ObjectManager.addGameObject(player1);
                player1.playerNum = playerNum;
                player1.inLevel = 1;
            }
            if(playerNum == 2 && !p2in)
            {
                p2in = true;
                player2 = new Player("player" + playerNum, 171, 207, "DefaultDuck.png", 32, 1, 32, 0.1f, device, weapon, breed1);
                player2.setPosition(duckRiser.getPosition());
                ObjectManager.addGameObject(player2);
                player2.playerNum = playerNum;
                player2.inLevel = 1;
            }
        }
        if(duckRiser.animationData.getCurrentFrame() == 40)
        {
            duckRiser.animationData.stop();
        }
    }
    public static void findDuck(float indicator, Weapon weapon, Bullet bullet, String species, int playerNum)
    {
        if(indicator == 250)
        {
            weapon.setWeaponTag("alienDuckHead");
            weapon.setGweaponWidth( 279);
            weapon.setGweaponHeight( 279);

            weapon.setCooldown(0.75f);

            bullet = new Bullet(0, 10, "", 69, 57, "", 12, 1, 12, 0.1f);
            bullet.setWidth(69);
            bullet.setHeight(57);
            bullet.setSpeed(11);
            bullet.setTotalFrames(12);
            bullet.setColliderSize(60);
            bullet.setBulletTag("alienDuckHead");
            bullet.setPlayerNumber(playerNum);
            weapon.setBullet(bullet);
        }
        if(indicator == -300)
        {
            weapon.setWeaponTag("cannon");

            weapon.setGweaponWidth(303);
            weapon.setGweaponHeight(381);

            weapon.setCooldown(3.0f);

            bullet = new Bullet(0, 8, "", 69, 57, "", 1, 1, 1, 0.1f);
            bullet.setWidth(81);
            bullet.setHeight(81);
            bullet.setSpeed(8);
            bullet.setTotalFrames(1);
            bullet.setColliderSize(40);
            bullet.setBulletTag("cannon");
            bullet.setPlayerNumber(playerNum);
            weapon.setBullet(bullet);
        }
        if(indicator == -500)
        {
            weapon.setWeaponTag("grenadeLauncher");
            weapon.setGweaponWidth(270);
            weapon.setGweaponHeight(276);

            weapon.setCooldown(2.0f);

            bullet = new Bullet(0, 10, "", 42, 48, "", 4, 1, 4, 0.1f);
            bullet.setWidth(42);
            bullet.setHeight(48);
            bullet.setSpeed(8);
            bullet.setTotalFrames(4);
            bullet.setColliderSize(20);
            bullet.setBulletTag("grenadeLauncher");
            bullet.setPlayerNumber(playerNum);
            weapon.setBullet(bullet);
        }
        if(indicator == 450)
        {
            weapon.setWeaponTag("laserMatrix");

            weapon.setGweaponWidth(243);
            weapon.setGweaponHeight(276);

            weapon.setCooldown(2.0f);
            bullet = new Bullet(0, 20, "", 72, 24, "", 2, 1, 2, 0.1f);
            bullet.setWidth(72);
            bullet.setHeight(24);
            bullet.setSpeed(20);
            bullet.setTotalFrames(2);
            bullet.setColliderSize(48);
            bullet.setBulletTag("laserMatrix");
            bullet.setPlayerNumber(playerNum);
            weapon.setBullet(bullet);
        }
        if(indicator == -700)
        {
            weapon.setWeaponTag("rocketLauncher");

            weapon.setGweaponWidth(339);
            weapon.setGweaponHeight(276);

            weapon.setCooldown(2.0f);
            bullet = new Bullet(0, 12, "", 99, 30, "", 1, 1, 1, 0.1f);
            bullet.setWidth(99);
            bullet.setHeight(30);
            bullet.setSpeed(13);
            bullet.setTotalFrames(1);
            bullet.setColliderSize(20);
            bullet.setBulletTag("rocketLauncher");
            bullet.setPlayerNumber(playerNum);
            weapon.setBullet(bullet);
        }
        if(indicator == 650)
        {
            weapon.setWeaponTag("shotgun");
            weapon.setGweaponWidth(315);
            weapon.setGweaponHeight(276);

            weapon.setCooldown(1.25f);

            bullet = new Bullet(0, 11, "", 60, 30, "", 2, 1, 2, 0.1f);
            bullet.setWidth(60);
            bullet.setHeight(30);
            bullet.setSpeed(11);
            bullet.setTotalFrames(2);
            bullet.setColliderSize(45);
            bullet.setBulletTag("shotgun");
            bullet.setPlayerNumber(playerNum);
            weapon.setBullet(bullet);
        }
    }
    public static void previewDuck(Player player, int pNumber, XInputDevice device, Breed breed, float leftX)
    {
        float tempX = player.getPositionX();
        float tempY = player.getPositionY();
        int[] tempPointerBounds = player.pointerBounds;
        player.kill();

        if(pNumber == 1)
        {
            findDuck(pointer1.getPositionX(), pWeapon1, pWeapon1.getBullet(), "Duck", 1);
            player = new Player("player1", newCharChoose.pWeapon1.getGweaponWidth(),
                    newCharChoose.pWeapon1.getGweaponHeight(), newCharChoose.pWeapon1.getWeaponTag() + ".png", 32, 1, 32, 0.1f, device, newCharChoose.pWeapon1, breed);
            ObjectManager.addGameObject(player);
            player.setPosition(tempX, tempY);
            player.pointing = false;
            player.playerNum = pNumber;
            player.inLevel = 1;
            player.pointerBounds = tempPointerBounds;
            player.leftStickX = leftX;
            pointer1.kill();
        }
        if(pNumber == 2)
        {
            findDuck(pointer2.getPositionX(), pWeapon2, pWeapon2.getBullet(), "Duck", 2);
            player = new Player("player2", newCharChoose.pWeapon2.getGweaponWidth(),
                    newCharChoose.pWeapon2.getGweaponHeight(), newCharChoose.pWeapon2.getWeaponTag() + ".png", 32, 1, 32, 0.1f, device, newCharChoose.pWeapon2, breed);
            ObjectManager.addGameObject(player);
            player.setPosition(tempX, tempY);
            player.pointing = false;
            player.playerNum = pNumber;
            player.inLevel = 1;
            player.pointerBounds = tempPointerBounds;
            player.leftStickX = leftX;
            pointer2.kill();
        }
        player.pointing = false;
        player.atExploStand = false;
        player.atProtoStand = false;
    }
    public void pointerMove(GameObject pointer, XInputDevice deviceX, int stickIndicator, Player player)
    {
        if (deviceX.poll())
        {
            if (stickIndicator == 1)
            {
                if (p1LeftX > 0.5f && !p1moved) {
                    if (pointer.getPositionX() <= player.pointerBounds[0]) {
                        pointer.setPositionX(player.pointerBounds[2]);
                        p1moved = true;
                    } else
                        pointer.setPositionX(pointer.getPositionX() - (player.pointerBounds[1] - player.pointerBounds[0]));
                    p1moved = true;
                }
                if (p1LeftX < -0.5f && !p1moved) {
                    if (pointer.getPositionX() >= player.pointerBounds[2]) {
                        pointer.setPositionX(player.pointerBounds[0]);
                        p1moved = true;
                    } else
                        pointer.setPositionX(pointer.getPositionX() + (player.pointerBounds[1] - player.pointerBounds[0]));
                    p1moved = true;
                }

                if (p1LeftX < 0.5f && p1LeftX > -0.5f)
                {
                    p1moved = false;
                }
            }
            if (stickIndicator == 2)
            {
                if (p2LeftX > 0.5f && !p2moved) {
                    if (pointer.getPositionX() <= player.pointerBounds[0]) {
                        pointer.setPositionX(player.pointerBounds[2]);
                        p2moved = true;
                    } else
                        pointer.setPositionX(pointer.getPositionX() - (player.pointerBounds[1] - player.pointerBounds[0]));
                    p2moved = true;
                }
                if (p2LeftX < -0.5f && !p2moved) {
                    if (pointer.getPositionX() >= player.pointerBounds[2]) {
                        pointer.setPositionX(player.pointerBounds[0]);
                        p2moved = true;
                    } else
                        pointer.setPositionX(pointer.getPositionX() + (player.pointerBounds[1] - player.pointerBounds[0]));
                    p2moved = true;
                }

                if (p2LeftX < 0.5f && p2LeftX > -0.5f) {
                    p2moved = false;
                }
            }
        }
    }
    @Override
    public void uninitialize()
    {
        ObjectManager.removeAllObjects();
    }
    public void DeviceCheck(int devicenum)
    {
        if (devicenum == 0)
        {

            try
            {
                device0 = XInputDevice.getDeviceFor(devicenum);
            } catch (com.ivan.xinput.exceptions.XInputNotLoadedException e)
            {
                System.out.println("Exception thrown" + devicenum);
            }
        }

        if (devicenum == 1)
        {

            try
            {
                device1 = XInputDevice.getDeviceFor(devicenum);
            } catch (com.ivan.xinput.exceptions.XInputNotLoadedException e)
            {
                System.out.println("Exception thrown" + devicenum);
            }
        }

        if (devicenum == 2)
        {

            try
            {
                device2 = XInputDevice.getDeviceFor(devicenum);
            } catch (com.ivan.xinput.exceptions.XInputNotLoadedException e)
            {
                System.out.println("Exception thrown" + devicenum);
            }
        }

        if (devicenum == 3)
        {

            try
            {
                device3 = XInputDevice.getDeviceFor(devicenum);
            } catch (com.ivan.xinput.exceptions.XInputNotLoadedException e)
            {
                System.out.println("Exception thrown" + devicenum);
            }
        }
    }
}
