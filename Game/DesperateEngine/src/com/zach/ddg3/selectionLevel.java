package com.zach.ddg3;

import com.ivan.xinput.enums.XInputButton;
import com.zach.engine.Main;

public class selectionLevel extends GameLevel
{
    public static Player player1;
    private static Object fountain;
    private static Object floor;
    private static Object arch;

    @Override
    public void init(Main main)
    {
        this.bottomCamera = -360;
        this.topCamera = 350;

        floor = new Object("floor", 640, 1080, "/selectionFloor.png", 1, 0.1f);
        GameManager.objects.add(floor);

        fountain = new Object("fountain", 188, 103, "/fountain.png", 4, 0.1f);
        fountain.setPosition(0, 150);
        fountain.playInRange(0,3);
        GameManager.objects.add(fountain);

        player1 = new Player("player1", 57, 68, "/duckUnarmed.png", 8, 0.1f, 0);
        GameManager.objects.add(player1);
        player1.setPosition(0,425);

        arch = new Object("arch", 640, 1080, "/selectionArch.png", 1, 0.1f);
        GameManager.objects.add(arch);

    }

    @Override
    public void update(Main main, float dt)
    {
        GameManager.center.setPosition(player1.position.x, player1.position.y);

        if(player1.device.poll() && player1.device.getDelta().getButtons().isPressed(XInputButton.START))
        {
            GameManager.objects.add(player1);
        }
    }

    @Override
    public void uninit()
    {

    }
}
