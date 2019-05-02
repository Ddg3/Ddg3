package com.zach.ddg3;

import com.ivan.xinput.enums.XInputButton;
import com.zach.ddg3.components.AABBComponent;
import com.zach.engine.Main;

import java.awt.event.KeyEvent;

public class selectionLevel extends GameLevel {
    public static Player player1;
    public static Player player2;
    public static Player player3;

    private static Wall fountain;
    private static Wall[] statues = new Wall[12];
    private static Wall[] sideRails = new Wall[2];
    private static Object floor;
    private static Wall arch;
    private static Wall archRef;
    private static Object archBridge;

    private static Object floor2;
    private static Wall doorframe;
    private static Wall[] walls = new Wall[2];
    private static Wall door;

    private static Object explosiveGuns;
    private static Object explosiveGuns2;
    private static boolean allReady = false;

    @Override
    public void init(Main main) {
        this.verticleBounds.clear();
        this.horizBounds.clear();

        this.verticleBounds.add(new Vector(350, -360));
        this.verticleBounds.add(new Vector(-720, -750));
        this.verticleBounds.add(new Vector(-750, -1000));
        this.loadPoint = 2;

        //this.verticleBounds.add(new Vector(-600, -600));

        floor = new Object("floor", 640, 1080, "/selectionFloor.png", 1, 0.1f);
        GameManager.objects.add(floor);

        fountain = new Wall("fountain", 188, 103, "/fountain.png", 4, 0.1f, true);
        fountain.setPosition(0, 150);
        fountain.playInRange(0, 3);
        GameManager.objects.add(fountain);
        fountain.paddingTop = 15;
        fountain.paddingSide = 10;
        fountain.zIndex = 2;

        player1 = new Player("player1", 63, 68, "/duckSheetLong.png", 24, 0.01f, 0);
        player1.setPosition(-240, -220);
        player1.zIndex = 10;
        player1.maxzIndex = 10;
        GameManager.objects.add(player1);
        player1.visible = false;

        player2 = new Player("player2", 63, 68, "/duckSheetLong.png", 24, 0.01f, 1);
        player2.setPosition(-165, 0);
        player2.zIndex = 10;
        player2.maxzIndex = 10;
        player2.setKeyDropIn(KeyEvent.VK_SPACE);
        player2.setKeyLeft(KeyEvent.VK_LEFT);
        player2.setKeyRight(KeyEvent.VK_RIGHT);
        player2.setKeyDown(KeyEvent.VK_DOWN);
        player2.setKeyUp(KeyEvent.VK_UP);
        player2.setKeyShoot(KeyEvent.VK_PAGE_DOWN);
        GameManager.objects.add(player2);
        player2.visible = false;

        floor2 = new Object("floor2", 640, 465, "/selectionGround.png", 1, 0.1f);
        floor2.setPosition(0, -700);
        GameManager.objects.add(floor2);
        floor2.zIndex = -2;

        walls[0] = new Wall("wall1", 265, 203, "/selectionWallHalf.png", 2, 0.1f, false);
        walls[0].setPosition(-188, -830);
        //walls[0].setzUpdatePointOffset(-50);
        GameManager.objects.add(walls[0]);
        walls[0].paddingTop = 100;
        walls[0].zIndex = 4;

        walls[1] = new Wall("wall2", 265, 203, "/selectionWallHalf.png", 2, 0.1f, false);
        walls[1].setPosition(186, -830);
        //walls[1].setzUpdatePointOffset(-50);
        walls[1].setFrame(1);
        GameManager.objects.add(walls[1]);
        walls[1].paddingTop = 100;
        walls[1].zIndex = 4;

        doorframe = new Wall("doorframe", 122, 203, "/selectionWallDoorFrame.png", 1, 0.1f, true);
        doorframe.removeComponentBySubtag("wall");
        doorframe.setPosition(-1, -830);
        GameManager.objects.add(doorframe);
        doorframe.zIndex = 4;
        doorframe.setzUpdatePointOffset(-25);

        door = new Wall("door", 111, 88, "/selectionDoor.png", 1, 0.1f, false);
        door.setPosition(-1, -780);
        GameManager.objects.add(door);
        door.paddingTop = 87;

        arch = new Wall("arch", 155, 239, "/selectionArchWalls.png", 2, 0.1f, false);
        GameManager.objects.add(arch);
        arch.setPosition(-243, -475);
        arch.zIndex = 1;
        arch.paddingSide = 20;
        arch.paddingTop = 70;
        arch.setOffsetCenterX(22);
        arch.setOffsetCenterY(0);

        archRef = new Wall("archRef", 155, 239, "/selectionArchWalls.png", 2, 0.1f, false);
        GameManager.objects.add(archRef);
        archRef.setPosition(242, -475);
        archRef.zIndex = 1;
        archRef.setFrame(1);
        archRef.paddingSide = 20;
        archRef.paddingTop = 70;
        archRef.setOffsetCenterX(-22);
        archRef.setOffsetCenterY(0);

        archBridge = new Object("archBridge", 640, 124, "/selectionArchBridge.png", 1, 0.1f);
        GameManager.objects.add(archBridge);
        archBridge.setPosition(0, -532);
        archBridge.zIndex = 12;


        int yInd = 0;
        for (int i = 0; i < statues.length; i++) {
            statues[i] = new Wall("statue" + i, 61, 80, "/statues.png", 2, 0.1f, true);
            int xInd = 0;
            int frInd = 0;
            if (i % 2 == 0) {
                xInd = -1;
                frInd = 0;
                yInd++;
            } else {
                xInd = 1;
                frInd = 1;
            }
            statues[i].setPosition(225 * xInd, 625 + (yInd * -150));
            statues[i].setFrame(frInd);
            statues[i].paddingSide = 2;
            statues[i].paddingTop = 45;
            statues[i].setOffsetCenterY(20);
            GameManager.objects.add(statues[i]);
            statues[i].zIndex = 6;
        }

        sideRails[0] = new Wall("sideRail1", 56, 936, "/sideRail.png", 2, 0.1f, true);
        sideRails[0].setPosition(-300, 50);
        GameManager.objects.add(sideRails[0]);
        sideRails[0].zIndex = 2;
        sideRails[0].paddingTop = 10;

        sideRails[1] = new Wall("sideRail1", 56, 936, "/sideRail.png", 2, 0.1f, true);
        sideRails[1].setPosition(300, 50);
        sideRails[1].setFrame(1);
        GameManager.objects.add(sideRails[1]);
        sideRails[1].zIndex = 2;
        sideRails[1].paddingTop = 10;

        explosiveGuns = new Object("explosiveGuns", 257, 52, "/explosiveSelections.png", 5, 0f);
        explosiveGuns.setTag("Selection");
        explosiveGuns.addComponent(new AABBComponent(explosiveGuns, "selection"));
        explosiveGuns.setPosition(187, -782);
        explosiveGuns.zIndex = 6;
        GameManager.objects.add(explosiveGuns);

        explosiveGuns2 = new Object("explosiveGuns2", 257, 52, "/explosiveSelections.png", 5, 0f);
        explosiveGuns2.setTag("Selection");
        explosiveGuns2.addComponent(new AABBComponent(explosiveGuns2, "selection"));
        AABBComponent otherC = (AABBComponent) explosiveGuns2.findComponentBySubtag("selection");
        otherC.setDesignatedPlayer(1);
        explosiveGuns2.setPosition(187, -782);
        explosiveGuns2.zIndex = 6;
        GameManager.objects.add(explosiveGuns2);
    }

    @Override
    public void update(Main main, float dt)
    {
        if(!player1.isNearSelect() && !player1.isSelecting())
        {
            explosiveGuns.setFrame(0);
        }
        else if(!player1.isSelecting())
            {
                explosiveGuns.setFrame(4);
            }
        if(!player2.isNearSelect())
        {
            explosiveGuns2.setFrame(0);
        }
        else if(!player2.isSelecting())
            {
              explosiveGuns.setFrame(4);
            }
        if(player1.visible)
        {
            readyUp(door, dt);
        }
        if(!player2.visible)
        {
            player2.position.y = GameManager.center.position.y - 220;
        }
        if(player1.isInGame())
        {
            allReady = true;
        }
    }

    public void readyUp(Object other, float dt)
    {
        for(int i = 0; i < GameManager.players.size(); i++)
        {
            if(!GameManager.players.get(i).isReady())
            {
               allReady = false;
               return;
            }
        }

        if(allReady)
        {
            //doorframe.zIndex = 2;
            other.zIndex = -1;
            other.setPosition(other.getPositionX(), other.getPositionY() - 50 * dt);
        }
    }

    @Override
    public void uninit()
    {
        GameManager.camera.setPosX(0);
        GameManager.camera.setPosY(0);
        GameManager.objects.clear();
        GameManager.textObjects.clear();
        GameManager.gameLevelManager.currLevel = null;
    }
}