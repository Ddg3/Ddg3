package com.zach.ddg3;

import com.zach.engine.AbstractGame;
import com.zach.engine.Main;
import com.zach.engine.Renderman;
import com.zach.engine.gfx.Image;

import java.util.ArrayList;

/**
 * Created by Zach on 4/14/2018.
 */
public class GameManager extends AbstractGame
{
    public static ArrayList<Object> objects = new ArrayList<Object>(1);
    public static ArrayList<TextObject> textObjects = new ArrayList<TextObject>(1);
    private ArrayList<Object> toKillList = new ArrayList<Object>();
    private int[] collision;
    private int levelWidth;
    private int levelHeight;
    private boolean isColliding = false;

    public static Object center;
    public static Camera camera;
    public static GameLevelManager gameLevelManager;
    public static DeviceManager deviceManager;

    public GameManager()
    {
        gameLevelManager = new GameLevelManager(this);
        deviceManager = new DeviceManager();
        camera = new Camera("center" );
    }

    @Override
    public void init(Main main)
    {
        //Runs first
        main.getRenderman().setAmbientColor(-1);
        gameLevelManager.gameState = GameLevelManager.GameState.TITLE_STATE;
        deviceManager.init(main);

        //Acts as invisible camera target for menu levels or static camera levels
        center = new Object("center", 640, 360, "/centerTest.png", 1, 1f, true);
        GameManager.objects.add(center);
        center.visible = false;
        center.setPosition(0,0);
    }

    @Override
    public void update(Main main, float dt)
    {
        //Runs ALWAYS in case u couldn't tell
        for(int i = 0; i < objects.size(); i++)
        {
            objects.get(i).update(main, this, dt);
            if(objects.get(i).isDead())
            {
                objects.remove(i);
                i--;
            }
        }

        gameLevelManager.update(main, dt);
        gameLevelManager.currLevel.update(main, dt);
        camera.update(this, main, dt);
        deviceManager.update(main, dt);
    }

    @Override
    public void render(Main main, Renderman renderman)
    {
        /*for(int y = 0; y < levelHeight; y++)
        {
            for(int x = 0; x < levelWidth; x++)
            {
                if(collision[x + y * levelWidth] == 1)
                {
                    renderman.drawFillRectangle(x * 16, y * 16, 16, 16, 0xff0f0f0f);
                }
                else
                    {
                        renderman.drawFillRectangle(x * 16, y * 16, 16, 16, 0xfff9f9f9);
                    }
            }
        }*/
        for(Object obj: objects)
        {
            //Renders all objects
            obj.render(main, renderman);
        }
        for(TextObject textObj: textObjects)
        {
            //Renders all text objects
            if(textObj.visible)
            main.getRenderman().drawText(textObj.text, textObj.posX, textObj.posY, textObj.color, textObj.scale);
        }
    }

    public void loadLevel(String path)
    {
        //Useless function. Keeping bc why not
        Image levelImage = new Image(path);

        levelWidth = levelImage.getWidth();
        levelHeight = levelImage.getHeight();
        collision = new int[levelWidth * levelHeight];
        for(int y = 0; y < levelImage.getHeight(); y++)
        {
            for(int x = 0; x < levelImage.getWidth(); x++)
            {
                if(levelImage.getPixel()[x + y * levelImage.getWidth()] == 0xff000000)
                {
                    collision[x + y * levelImage.getWidth()] = 1;
                }
                else
                {
                    collision[x + y * levelImage.getWidth()] = 0;
                }
            }
        }
    }

    public static void removeObjectsByName(String name)
    {
        for(int i = 0; i < objects.size(); i++)
        {
            if(objects.get(i).getTag() == name)
            {
                objects.remove(i);
            }
        }
    }

    public Object getObject(String name)
    {
        for(int i = 0; i < objects.size(); i++)
        {
            if(objects.get(i).getTag().equals(name))
            {
                return objects.get(i);
            }
        }
        return null;
    }

    public int getCollision(int x, int y)
    {
        return collision[x + y * levelWidth];
    }

    public static void main(String args[])
    {
        Main main = new Main(new GameManager());
        main.initialize();
        main.setWidth(640);
        main.setHeight(360);
        main.setScale(3f);
        main.initialize();
    }
}
