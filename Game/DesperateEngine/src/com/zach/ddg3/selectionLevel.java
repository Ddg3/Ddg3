package com.zach.ddg3;

import com.ivan.xinput.enums.XInputButton;
import com.zach.engine.Main;

public class selectionLevel extends GameLevel
{
    public static Player player1;
    public static Player player2;

    private static Wall fountain;
    private static Wall[] statues = new Wall[12];
    private static Wall[] sideRails = new Wall[2];
    private static Object floor;
    private static Object arch;

    private static Object floor2;
    private static Object wall;
    private static Object door;
    private static Object arch2;

    private static Object explosiveGuns;

    @Override
    public void init(Main main)
    {
        this.verticleBounds.clear();
        this.horizBounds.clear();

        this.verticleBounds.add(new Vector(350,-360));
        this.verticleBounds.add(new Vector(-720,-825));

        //this.verticleBounds.add(new Vector(-600, -600));

        floor = new Object("floor", 640, 1080, "/selectionFloor.png", 1, 0.1f);
        GameManager.objects.add(floor);

        fountain = new Wall("fountain", 188, 103, "/fountain.png", 4, 0.1f);
        fountain.setPosition(0, 150);
        fountain.playInRange(0,3);
        GameManager.objects.add(fountain);
        fountain.paddingTop = 15;
        fountain.paddingSide = 10;

        player1 = new Player("player1", 57, 68, "/duckUnarmed.png", 8, 0.1f, 0);
        GameManager.objects.add(player1);
        player1.setPosition(0,0);
        player1.zIndex = 1;
        //player1.visible = false;

        player2 = new Player("player2", 57, 68, "/duckUnarmed.png", 8, 0.1f, 1);
        GameManager.objects.add(player1);
        player2.setPosition(75,0);
        player2.zIndex = 1;
        player2.visible = false;

        arch = new Object("arch", 640, 1080, "/selectionArch.png", 1, 0.1f);
        GameManager.objects.add(arch);
        arch.zIndex = 2;

        floor2 = new Object("floor2", 640, 465, "/selectionGround.png", 1, 0.1f);
        floor2.setPosition(0, -773);
        GameManager.objects.add(floor2);

        arch2 = new Object("floor2", 640, 465, "/selectionArch2.png", 1, 0.1f);
        arch2.setPosition(0, -773);
        GameManager.objects.add(arch2);
        arch2.zIndex = 2;

        wall = new Object("wall", 640, 465, "/selectionWall.png", 1, 0.1f);
        wall.setPosition(0, -773);
        GameManager.objects.add(wall);

        door = new Object("door", 640, 465, "/selectionDoor.png", 1, 0.1f);
        door.setPosition(0, -773);
        GameManager.objects.add(door);

        int yInd = 0;
        for(int i = 0; i < statues.length; i++)
        {
            statues[i] = new Wall("statue" + i, 61, 80, "/statues.png", 2, 0.1f);
            int xInd = 0;
            int frInd = 0;
            if(i % 2 == 0)
            {
                xInd = -1;
                frInd = 0;
                yInd++;
            }
            else
                {
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

        sideRails[1] = new Wall("sideRail1", 56, 936, "/sideRail.png", 2, 0.1f);
        sideRails[1].setPosition(300, 50);
        sideRails[1].setFrame(1);
        GameManager.objects.add(sideRails[1]);
    }

    @Override
    public void update(Main main, float dt)
    {
        GameManager.center.setPosition(player1.position.x, player1.position.y);

        if(player1.device.poll() && player1.device.getDelta().getButtons().isPressed(XInputButton.START))
        {
            //System.out.println("DAB");
            player1.visible = true;
            GameManager.objects.add(player1);
            GameManager.players.add(player1);
        }
        if(player2.device.poll() && player2.device.getDelta().getButtons().isPressed(XInputButton.START))
        {
            player2.visible = true;
            GameManager.objects.add(player2);
            GameManager.players.add(player2);
            player2.position.y = player1.position.y;
            player2.position.x = player1.position.x + 100;
        }


    }

    @Override
    public void uninit()
    {

    }
}
