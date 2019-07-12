package com.zach.ddg3;

import com.ivan.xinput.enums.XInputButton;
import com.zach.ddg3.components.AABBComponent;
import com.zach.engine.Main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class selectionLevel extends GameLevel {
    /*public static Player players.get(0);
    public static Player players.get(1);
    public static Player player3;*/
    public static ArrayList<Player> players = new ArrayList<>(1);

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

    public static Object[] getExplosiveGuns() {
        return explosiveGuns;
    }

    public static void setExplosiveGuns(Object[] explosiveGuns) {
        selectionLevel.explosiveGuns = explosiveGuns;
    }

    private static Object[] explosiveGuns = new Object[3];
    private static boolean allReady = false;

    @Override
    public void init(Main main)
    {
        GameManager.camera.resetCamera();
        this.verticleBounds.clear();
        this.horizBounds.clear();
        GameManager.cameraPlayers.clear();

        this.verticleBounds.add(new Vector(350, -360));
        this.verticleBounds.add(new Vector(-720, -750));
        this.verticleBounds.add(new Vector(-750, -1000));
        this.horizBounds.add(new Vector(0,0));
        this.loadPoint = 2;

        if(GameManager.firstTime)
        {
            players.add(new Player("player0", 63, 68, "/duckSheetLong.png", 24, 0.01f, 0));
            players.get(0).setPosition(-240, -220);
            players.get(0).zIndex = 10;
            players.get(0).maxzIndex = 10;
            GameManager.objects.add(players.get(0));
            players.get(0).visible = false;

            players.add(new Player("player1", 63, 68, "/duckSheetLong.png", 24, 0.01f, 1));
            players.get(1).setPosition(-165, 0);
            players.get(1).zIndex = 10;
            players.get(1).maxzIndex = 10;
            players.get(1).setKeyBoard(false);
            /*players.get(1).setKeyDropIn(KeyEvent.VK_SPACE);
            players.get(1).setKeyLeft(KeyEvent.VK_LEFT);
            players.get(1).setKeyRight(KeyEvent.VK_RIGHT);
            players.get(1).setKeyDown(KeyEvent.VK_DOWN);
            players.get(1).setKeyUp(KeyEvent.VK_UP);
            players.get(1).setKeyShoot(KeyEvent.VK_PAGE_DOWN);*/
            GameManager.objects.add(players.get(1));
            players.get(1).visible = false;
        }
        else
            {
                GameManager.camera.boundsRange = 1;
                for(int i = 0; i < GameManager.players.size(); i++)
                {
                        players.add(GameManager.players.get(i));
                        players.get(i).setPosition(((i * 50) - 25),-600);
                        GameManager.objects.add(players.get(i));
                        players.get(i).zIndex = 10;
                        players.get(i).maxzIndex = 10;
                        GameManager.cameraPlayers.add(players.get(i));
                        players.get(i).setTimedOut(false);
                        players.get(i).setRemoved(false);
                        players.get(i).setInGame(true);
                        players.get(i).setTime(60);
                        players.get(i).setDead(false);

                        if(players.get(i).isGoose())
                        {
                            players.get(i).setGoose(false);
                            players.get(i).changeSpecies();
                        }
                }

                for(int i = 0; i < players.size(); i++)
                {
                    GameManager.objects.add(GameManager.timePedestals.get(i));
                }
                for(int i = 0; i < players.size(); i++)
                {
                    GameManager.objects.add(GameManager.indicators.get(i));
                }
            }


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

        explosiveGuns[0] = new Object("explosiveGuns", 257, 52, "/explosiveSelections.png", 5, 0f);
        explosiveGuns[0].setTag("Selection");
        explosiveGuns[0].addComponent(new AABBComponent(explosiveGuns[0], "selection"));
        explosiveGuns[0].setPosition(187, -782);
        explosiveGuns[0].zIndex = 6;
        GameManager.objects.add(explosiveGuns[0]);
        explosiveGuns[0].getObjImage().changeColor(players.get(0).getSkinColors()[1], players.get(0).getSkinColors()[players.get(0).getSkIndex()]);

        explosiveGuns[1] = new Object("explosiveGuns1", 259, 54, "/explosiveSelections2.png", 5, 0f);
        explosiveGuns[1].setTag("Selection");
        explosiveGuns[1].addComponent(new AABBComponent(explosiveGuns[1], "selection"));
        AABBComponent otherC = (AABBComponent) explosiveGuns[1].findComponentBySubtag("selection");
        otherC.setDesignatedPlayer(1);
        explosiveGuns[1].setPosition(187, -782);
        explosiveGuns[1].zIndex = 5;
        explosiveGuns[1].getObjImage().changeColor(players.get(1).getSkinColors()[1], players.get(1).getSkinColors()[players.get(1).getSkIndex()]);
        GameManager.objects.add(explosiveGuns[1]);

        /*explosiveGuns[2] = new Object("explosiveGuns2", 259, 54, "/explosiveSelections2.png", 5, 0f);
        explosiveGuns[2].setTag("Selection");
        explosiveGuns[2].addComponent(new AABBComponent(explosiveGuns[2], "selection"));
        AABBComponent otherC2 = (AABBComponent) explosiveGuns[2].findComponentBySubtag("selection");
        otherC2.setDesignatedPlayer(2);
        explosiveGuns[2].setPosition(187, -782);
        explosiveGuns[2].zIndex = 4;
        GameManager.objects.add(explosiveGuns[2]);*/

        /*for(int i = 0; i < players.size(); i++)
        {
            GameManager.objects.add(GameManager.timePedestals.get(i));
        }*/
    }

    @Override
    public void update(Main main, float dt)
    {
        if(players.get(0).visible)
        {
            readyUp(door, dt);
        }
        if(GameManager.firstTime)
        {
            if (!players.get(1).visible) {
                players.get(1).position.y = GameManager.center.position.y - 220;
            }
        }
        if(players.get(0).isInGame())
        {
            allReady = true;
        }
        for(int i = 0; i < players.size(); i++)
        {
            if(!players.get(i).isNearSelect() && !players.get(i).isSelecting())
            {
                explosiveGuns[i].setFrame(0);
            }
            else if(!players.get(i).isSelecting())
            {
                explosiveGuns[i].setFrame(4);
            }
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
        else
            {
                if(other.getPositionY() <= -780)
                {
                    other.setPosition(other.getPositionX(), other.getPositionY() + 50 * dt);
                }
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
        players.clear();

        for(int i = 0; i < players.size(); i++)
        {
            GameManager.objects.remove(GameManager.timePedestals.get(i));
        }
    }
}