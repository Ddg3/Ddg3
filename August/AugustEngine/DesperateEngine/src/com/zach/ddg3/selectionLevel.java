package com.zach.ddg3;

import com.zach.engine.Main;

public class selectionLevel extends GameLevel
{
    public static Player player1;
    //private static Object fountain;
    //private static Object floor;
    //private static Object arch;

    @Override
    public void init(Main main)
    {
        //floor = new Object("floor", 640, 1080, "/selectionFloor.png", 1, 0.1f, false);
        //GameManager.objects.add(floor);

        //fountain = new Object("fountain", 188, 103, "/fountain.png", 4, 0.1f, false);
        //fountain.setPosition(-100, 0);
        //GameManager.objects.add(fountain);

        player1 = new Player("player1", 57, 68, "/duckUnarmed.png", 8, 0.1f, false, 0);
        GameManager.objects.add(player1);
        player1.setPosition(0,0);

        //arch = new Object("arch", 640, 1080, "/selectionArch.png", 1, 0.1f, false);
        //GameManager.objects.add(arch);

    }

    @Override
    public void update(Main main, float dt)
    {
        //GameManager.center.setPosition(player1.position.x, player1.position.y);
    }

    @Override
    public void uninit() {

    }
}
