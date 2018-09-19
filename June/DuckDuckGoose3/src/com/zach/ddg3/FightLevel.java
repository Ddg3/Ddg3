package com.zach.ddg3;

import com.ivan.xinput.XInputAxesDelta;
import com.ivan.xinput.XInputButtonsDelta;
import com.ivan.xinput.XInputComponentsDelta;
import com.ivan.xinput.XInputDevice;
import com.ivan.xinput.enums.XInputButton;
import edu.digipen.*;


/**
 * Created by Zach on 2/16/2018.
 */
public class FightLevel extends GameLevel
{
    XInputDevice device0;
    XInputDevice device1;
    XInputDevice device2;
    XInputDevice device3;
    XInputButton buttons[] = new XInputButton[6];

    Breed breed1;
    Breed breed2;

    public static Player[] players = new Player[4];
    public static Weapon[] weapons = new Weapon[4];
    Player player1;
    Player player2;

    public static GameObject p1bar;
    public static GameObject p2bar;
    public static GameObject p1timer;
    public static GameObject p2timer;

    public GameObject stageBack;

    public static int[] boundaries = new int[4];
    public static int[] holeBounds = new int[4];

    XInputComponentsDelta delta1;
    XInputButtonsDelta buttons1;
    XInputAxesDelta axes1;

    XInputComponentsDelta delta2;
    XInputButtonsDelta buttons2;
    XInputAxesDelta axes2;

    @Override
    public void create()
    {
       DeviceCheck(0);
        DeviceCheck(1);
        DeviceCheck(2);
        DeviceCheck(3);

        //Graphics.setDrawCollisionData(true);
    }

    @Override
    public void initialize()
    {
        //System.out.println(testtt.getWeaponTag());
      player1 = new Player("player1", newCharChoose.pWeapon1.getGweaponWidth(),
            newCharChoose.pWeapon1.getGweaponHeight(), newCharChoose.pWeapon1.getWeaponTag() + ".png", 32, 1, 32, 0.1f, device0, newCharChoose.pWeapon1,  breed1);
        ObjectManager.addGameObject(player1);
        player1.setPosition(-700,0);
        player1.setZOrder(2);
        player1.setSpecies(newCharChoose.p1tag);


        player2 = new Player("player2", newCharChoose.pWeapon2.getGweaponWidth(),
                newCharChoose.pWeapon2.getGweaponHeight(), newCharChoose.pWeapon2.getWeaponTag() + ".png", 32, 1, 32, 0.1f, device1, newCharChoose.pWeapon2, breed2);
        ObjectManager.addGameObject(player2);
        player2.setPosition(700,0);
        player2.setZOrder(2);
        player2.setSpecies(newCharChoose.p2tag);

        players[1] = player1;
        players[2] = player2;

        players[1].setWeapon(newCharChoose.pWeapon1);
        players[2].setWeapon(newCharChoose.pWeapon2);

        FightLevel.players[1].weapon.setAnimationSet(0);
        player1.animationData.goToFrame(FightLevel.players[1].weapon.getAnimationSet());

        FightLevel.players[2].weapon.setAnimationSet(16);
        player2.animationData.goToFrame(FightLevel.players[2].weapon.getAnimationSet());

        p1bar = new GameObject("p1bar", 213, 1080, "scoreBar_black.png", 1, 1, 1, 0.1f);
        p1bar.setPosition(-950, 0);
        ObjectManager.addGameObject(p1bar);

        p2bar = new GameObject("p2bar", 213, 1080, "scoreBar_black.png", 1, 1, 1, 0.1f);
        p2bar.setPosition(950, 0);
        ObjectManager.addGameObject(p2bar);

        p1timer = new GameObject("p1timer", 78, 51, "numberCountdown.png", 61, 61, 1, 0.1f);
        p1timer.setPosition(-900, 560);
        ObjectManager.addGameObject(p1timer);

        p2timer = new GameObject("p2timer", 78, 51, "numberCountdown.png", 61, 61, 1, 0.1f);
        p2timer.setPosition(900, 560);
        ObjectManager.addGameObject(p2timer);

        stageBack = new GameObject("stageBack", 1920, 1080, "cloudNoHole.png", 40, 10, 4, 0.1f);
        stageBack.setZOrder(-1);
        stageBack.animationData.play();

        p1bar.setZOrder(5);
        p2bar.setZOrder(5);
        p1timer.setZOrder(5);
        p2timer.setZOrder(5);

        boundaries[0] = 800;
        boundaries[1] = -800;
        boundaries[2] = 400;
        boundaries[3] = -300;

        holeBounds[0] = 200;
        holeBounds[1] = -200;
        holeBounds[2] = 200;
        holeBounds[3] = -200;
    }

    @Override
    public void update(float v)
    {
        if(players[1].getSpecies( ) == "Goose_")
        {
            p1bar.setPositionY(p1bar.getPositionY() - (v * (Game.getWindowHeight() / 60)));
            p1timer.setPositionY(p1timer.getPositionY() - (v * (Game.getWindowHeight() / 60)));
        }
        if(players[2].getSpecies( ) == "Goose_")
        {
            p2bar.setPositionY(p2bar.getPositionY() - (v * (Game.getWindowHeight() / 60)));
            p2timer.setPositionY(p2timer.getPositionY() - (v * (Game.getWindowHeight() / 60)));
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
