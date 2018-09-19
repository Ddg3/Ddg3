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
 * Created by Zach on 2/16/2018.
 */
public class CharChooseLevel extends GameLevel {

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

    public GameObject pointer1;
    public GameObject pointer2;
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
    }

    @Override
    public void initialize()
    {

        GameObject one = new GameObject("one", 234, 207, "Duck_alienDuckHead.png", 8, 1, 8, 0.1f);
        ObjectManager.addGameObject(one);
        one.setPosition(-800, 0);
        one.animationData.goToFrame(0);

        GameObject two = new GameObject("two", 303, 309, "Duck_cannon.png", 8, 1, 8, 0.1f);
        ObjectManager.addGameObject(two);
        two.setPosition(-600, 0);
        two.animationData.goToFrame(0);

        GameObject three = new GameObject("three", 270, 276, "grenadeLauncher.png", 32, 1, 32, 0.1f);
        ObjectManager.addGameObject(three);
        three.setPosition(-400, 0);
        three.animationData.goToFrame(0);

        GameObject four = new GameObject("four", 171, 204, "Duck_laserMatrix.png", 8, 1, 8, 0.1f);
        ObjectManager.addGameObject(four);
        four.setPosition(-200, 0);
        four.animationData.goToFrame(0);

        GameObject five = new GameObject("five", 306, 243, "Duck_rocketLauncher.png", 16, 1, 16, 0.1f);
        ObjectManager.addGameObject(five);
        five.setPosition(0, 0);
        five.animationData.goToFrame(0);

        GameObject six = new GameObject("six", 270, 207, "Duck_shotgun.png", 8, 1, 8, 0.1f);
        ObjectManager.addGameObject(six);
        six.setPosition(200, 0);
        six.animationData.goToFrame(0);

        pointer1 = new GameObject("pointer", 81, 81, "cannon_Bullet.png");
        pointer1.setPosition(-800, 200);

        pointer2 = new GameObject("pointer", 81, 81, "cannon_Bullet.png");
        pointer2.setPosition(-800, -200);
    }

    @Override
    public void update(float v)
    {
        pointerMove(pointer1, device0, 1, axes1, pWeapon1, bullet1);
        pointerMove(pointer2, device1, 2, axes2, pWeapon2, bullet2);
    }
    public void findDuck(float indicator, Weapon weapon, Bullet bullet, String species, int playerNum)
    {
        if(indicator == -800)
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
        if(indicator == -600)
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
        if(indicator == -400)
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
        if(indicator == -200)
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
        if(indicator == 0)
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
        if(indicator == 200)
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

    @Override
    public void uninitialize()
    {
        ObjectManager.removeAllObjects();
    }
    public void pointerMove(GameObject pointer, XInputDevice deviceX, int stickIndicator, XInputAxesDelta axes, Weapon weapon, Bullet bullet) {

        if (deviceX.poll()) {
            if (stickIndicator == 1)
            {
                leftStickX1 += axes.getDelta(XInputAxis.LEFT_THUMBSTICK_X);

                if (leftStickX1 > 0.3f && !p1moved)
                {
                    if (pointer.getPositionX() == -800)
                    {
                        pointer.setPositionX(200);
                        p1moved = true;
                    }
                    else
                        pointer.setPositionX(pointer.getPositionX() - 200);
                    p1moved = true;
                }
                if (leftStickX1 < -0.3f && !p1moved) {
                    if (pointer.getPositionX() == 200) {
                        pointer.setPositionX(-800);
                        p1moved = true;
                    } else
                        pointer.setPositionX(pointer.getPositionX() + 200);
                    p1moved = true;
                }

                if (leftStickX1 < 0.3f && leftStickX1 > -0.3f)
                {
                    p1moved = false;
                }

                if (buttons1.isPressed(XInputButton.A)) {
                    findDuck(pointer.getPositionX(), pWeapon1, bullet, p1tag, 1);
                    GameLevelManager.goToLevel(new FightLevel());
                }
            }

            if (stickIndicator == 2)
            {
                leftStickX2 += axes.getDelta(XInputAxis.LEFT_THUMBSTICK_X);
                //System.out.println(leftStickX2);

                if (leftStickX2 > 0.3f && !p2moved) {
                    if (pointer.getPositionX() == -800) {
                        pointer.setPositionX(200);
                        p2moved = true;
                    } else
                        pointer.setPositionX(pointer.getPositionX() - 200);
                    p2moved = true;
                }
                if (leftStickX2 < -0.3f && !p2moved) {
                    if (pointer.getPositionX() == 200) {
                        pointer.setPositionX(-800);
                        p2moved = true;
                    } else
                        pointer.setPositionX(pointer.getPositionX() + 200);
                    p2moved = true;
                }

                if (leftStickX2 < 0.3f && leftStickX2 > -0.3f) {
                    p2moved = false;
                }

                if (buttons1.isPressed(XInputButton.A)) {
                    findDuck(pointer.getPositionX(), pWeapon2, bullet, p2tag, 2);
                    GameLevelManager.goToLevel(new FightLevel());
                }
            }
        }
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

