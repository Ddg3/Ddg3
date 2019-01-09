package com.zach.ddg3;

import com.ivan.xinput.enums.XInputButton;
import com.zach.engine.Main;

public class selectionLevel extends GameLevel
{
    public static Player player1;
    public static Player player2;

    private static Object fountain;
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

        fountain = new Object("fountain", 188, 103, "/fountain.png", 4, 0.1f);
        fountain.setPosition(0, 150);
        fountain.playInRange(0,3);
        GameManager.objects.add(fountain);

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
