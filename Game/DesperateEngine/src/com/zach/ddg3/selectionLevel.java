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
    private static Wall wall;
    private static Wall door;

    private static Object explosiveGuns;

    @Override
    public void init(Main main) {
        this.verticleBounds.clear();
        this.horizBounds.clear();

        this.verticleBounds.add(new Vector(350, -360));
        this.verticleBounds.add(new Vector(-720, -750));

        //this.verticleBounds.add(new Vector(-600, -600));

        floor = new Object("floor", 640, 1080, "/selectionFloor.png", 1, 0.1f);
        GameManager.objects.add(floor);

        fountain = new Wall("fountain", 188, 103, "/fountain.png", 4, 0.1f);
        fountain.setPosition(0, 150);
        fountain.playInRange(0, 3);
        GameManager.objects.add(fountain);
        fountain.paddingTop = 15;
        fountain.paddingSide = 10;

        player1 = new Player("player1", 57, 68, "/duckUnarmed.png", 8, 0.1f, 0);
        player1.setPosition(0, 0);
        player1.zIndex = 1;
        player1.visible = false;

        player2 = new Player("player2", 57, 68, "/duckUnarmed.png", 8, 0.1f, 1);
        player2.setPosition(75, 0);
        player2.zIndex = 1;
        player2.visible = false;

        floor2 = new Object("floor2", 640, 465, "/selectionGround.png", 1, 0.1f);
        floor2.setPosition(0, -700);
        GameManager.objects.add(floor2);
        floor2.zIndex = -1;

        wall = new Wall("wall", 640, 203, "/selectionWall.png", 1, 0.1f);
        wall.setPosition(0, -830);
        GameManager.objects.add(wall);
        wall.paddingTop = 100;

        door = new Wall("door", 111, 88, "/selectionDoor.png", 1, 0.1f);
        door.setPosition(-1, -780);
        GameManager.objects.add(door);
        door.paddingTop = 87;

        arch = new Wall("arch", 155, 239, "/selectionArchWalls.png", 2, 0.1f);
        GameManager.objects.add(arch);
        arch.setPosition(-243, -475);
        arch.zIndex = 2;
        arch.paddingSide = 20;
        arch.paddingTop = 70;
        arch.setOffsetCenterX(22);
        arch.setOffsetCenterY(0);

        archRef = new Wall("archRef", 155, 239, "/selectionArchWalls.png", 2, 0.1f);
        GameManager.objects.add(archRef);
        archRef.setPosition(242, -475);
        archRef.zIndex = 2;
        archRef.setFrame(1);
        archRef.paddingSide = 20;
        archRef.paddingTop = 70;
        archRef.setOffsetCenterX(-22);
        archRef.setOffsetCenterY(0);

        archBridge = new Object("archBridge", 640, 124, "/selectionArchBridge.png", 1, 0.1f);
        GameManager.objects.add(archBridge);
        archBridge.setPosition(0, -532);
        archBridge.zIndex = 2;


        int yInd = 0;
        for (int i = 0; i < statues.length; i++) {
            statues[i] = new Wall("statue" + i, 61, 80, "/statues.png", 2, 0.1f);
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
        }

        sideRails[0] = new Wall("sideRail1", 56, 936, "/sideRail.png", 2, 0.1f);
        sideRails[0].setPosition(-300, 50);
        GameManager.objects.add(sideRails[0]);
        sideRails[0].zIndex = 1;
        sideRails[0].paddingTop = 10;

        sideRails[1] = new Wall("sideRail1", 56, 936, "/sideRail.png", 2, 0.1f);
        sideRails[1].setPosition(300, 50);
        sideRails[1].setFrame(1);
        GameManager.objects.add(sideRails[1]);
        sideRails[1].zIndex = 1;
        sideRails[1].paddingTop = 10;
    }

    @Override
    public void update(Main main, float dt) {
        //System.out.println(GameManager.players.size());

        if ((player1.device.poll() && player1.device.getDelta().getButtons().isPressed(XInputButton.START)) || (main.getInput().isKey(KeyEvent.VK_SPACE) && !player1.visible)) {
            //System.out.println("DAB");
            GameManager.objects.add(player1);
            GameManager.players.add(player1);
            player1.visible = true;
        }
        if (player2.device.poll() && player2.device.getDelta().getButtons().isPressed(XInputButton.START)) {
            player2.visible = true;
            GameManager.objects.add(player2);
            GameManager.players.add(player2);
            player2.position.y = player1.position.y;
            player2.position.x = player1.position.x + 100;
        }


    }

    @Override
    public void uninit() {

    }
}