package com.zach.ddg3;

import com.zach.engine.Main;

import java.util.ArrayList;

public class mainLevel extends GameLevel
{
    public static ArrayList<Player> players = new ArrayList<Player>(1);

    @Override
    public void init(Main main)
    {
        GameManager.camera.boundsRange = 0;
        this.verticleBounds.clear();
        this.horizBounds.clear();

        this.verticleBounds.add(new Vector(350, -360));

        for(int i = 0; i < GameManager.players.size(); i++)
        {
            if(GameManager.players.get(i) != null)
            {
                players.add(GameManager.players.get(i));
                players.get(i).setPosition(0,0);
                GameManager.objects.add(players.get(i));
            }
        }
        System.out.println("HMMMMM");
    }

    @Override
    public void update(Main main, float dt)
    {
        /*System.out.println(players.get(0).position.x + ", " + players.get(0).position.y);
        System.out.println(GameManager.camera.boundsRange);
        System.out.println(GameManager.camera.getPosX() + ", " + GameManager.camera.getPosY());*/
    }

    @Override
    public void uninit()
    {

    }
}
