package com.zach.ddg3;

import com.zach.engine.Main;

import java.util.ArrayList;

public class mainLevel extends GameLevel
{
    public static ArrayList<Player> players = new ArrayList<Player>(1);

    private static Wall frontWall;
    private static Wall backWall;
    private static Wall[] diagonalWalls = new Wall[4];
    private static Wall[] sideWalls = new Wall[2];
    private static Wall[] spears = new Wall[8];
    private static Object ground;

    @Override
    public void init(Main main)
    {
        players.add(new Player("player1", 63, 68, "/duckSheetLong.png", 24, 0.01f, 0));
        players.get(0).zIndex = 1;
        GameManager.objects.add(players.get(0));

        GameManager.camera.boundsRange = 0;
        this.verticleBounds.clear();
        this.horizBounds.clear();

        this.verticleBounds.add(new Vector(350, -360));
        this.horizBounds.add(new Vector(350, -360));

        /*for(int i = 0; i < GameManager.players.size(); i++)
        {
            if(GameManager.players.get(i) != null)
            {
                players.add(GameManager.players.get(i));
                players.get(i).setPosition(((i * 50) - 25),0);
                GameManager.objects.add(players.get(i));
            }
        }*/

        frontWall = new Wall("frontWall", 398, 116, "/frontWall.png", 1, 0.1f, false);
        frontWall.position.y = -206;
        frontWall.zIndex = 1;
        GameManager.objects.add(frontWall);

        backWall = new Wall("backWall", 639, 184, "/BackWalls.png", 1, 0.1f, false);
        backWall.position.y = 200;
        backWall.zIndex = 3;
        GameManager.objects.add(backWall);

        ground = new Object("ground", 640, 296, "/ground.png", 1, 0.1f);
        ground.zIndex = 0;
        GameManager.objects.add(ground);

        diagonalWalls[0] = new Wall("diagonalWall0", 57, 146, "/halfDoor1.png", 2, 0.1f, false);
        diagonalWalls[0].position.y = -171;
        diagonalWalls[0].position.x = -291;
        diagonalWalls[0].zIndex = 1;
        GameManager.objects.add(diagonalWalls[0]);

        diagonalWalls[1] = new Wall("diagonalWall1", 63, 135, "/halfDoor2.png", 2, 0.1f, false);
        diagonalWalls[1].position.y = -197;
        diagonalWalls[1].position.x = -231;
        diagonalWalls[1].zIndex = 1;
        GameManager.objects.add(diagonalWalls[1]);

        diagonalWalls[2] = new Wall("diagonalWall2", 57, 146, "/halfDoor1.png", 2, 0.1f, false);
        diagonalWalls[2].position.y = -171;
        diagonalWalls[2].position.x = 290;
        diagonalWalls[2].zIndex = 1;
        diagonalWalls[2].setFrame(1);
        GameManager.objects.add(diagonalWalls[2]);

        diagonalWalls[3] = new Wall("diagonalWall3", 63, 135, "/halfDoor2.png", 2, 0.1f, false);
        diagonalWalls[3].position.y = -197;
        diagonalWalls[3].position.x = 230;
        diagonalWalls[3].zIndex = 1;
        diagonalWalls[3].setFrame(1);
        GameManager.objects.add(diagonalWalls[3]);

        sideWalls[0] = new Wall("sideWall0", 16, 388, "/sideWalls.png", 2, 0.1f, false);
        sideWalls[0].position.x = -400;
        sideWalls[0].zIndex = 1;
        GameManager.objects.add(sideWalls[0]);
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
