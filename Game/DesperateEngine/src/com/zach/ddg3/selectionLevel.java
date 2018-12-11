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

    @Override
    public void init(Main main)
    {
        this.verticleBounds.clear();
        this.horizBounds.clear();

        this.verticleBounds.add(new Vector(350,-360));
        this.horizBounds.add(new Vector(0,0));

        floor = new Object("floor", 640, 1080, "/selectionFloor.png", 1, 0.1f);
        GameManager.objects.add(floor);

        fountain = new Object("fountain", 188, 103, "/fountain.png", 4, 0.1f);
        fountain.setPosition(0, 150);
        fountain.playInRange(0,3);
        GameManager.objects.add(fountain);

        player1 = new Player("player1", 57, 68, "/duckUnarmed.png", 8, 0.1f, 0);
        GameManager.objects.add(player1);
        player1.setPosition(0,0);
        //player1.visible = false;

        player2 = new Player("player2", 57, 68, "/duckUnarmed.png", 8, 0.1f, 1);
        GameManager.objects.add(player1);
        player2.setPosition(75,0);
        player2.visible = false;

        arch = new Object("arch", 640, 1080, "/selectionArch.png", 1, 0.1f);
        GameManager.objects.add(arch);

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
        }
        if(player2.device.poll() && player2.device.getDelta().getButtons().isPressed(XInputButton.START))
        {
            player2.visible = true;
            GameManager.objects.add(player2);
        }

    }

    @Override
    public void uninit()
    {

    }
}
