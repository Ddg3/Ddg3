package com.zach.ddg3;

import com.zach.engine.AbstractGame;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Zach on 4/14/2018.
 */
public class GameManager extends AbstractGame
{
    public static ArrayList<Object> objects = new ArrayList<Object>(1);
    public static ArrayList<Player> players = new ArrayList<Player>(1);
    public static ArrayList<TextObject> textObjects = new ArrayList<TextObject>(1);
    private ArrayList<Object> toKillList = new ArrayList<Object>();
    private int levelWidth;
    private int levelHeight;
    private boolean[] collision;
    private boolean isColliding = false;
    private static TextObject fpsCounter = null;
    private boolean showFps = true;
    private boolean isPlaying = true;

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
        main.getRenderer().setAmbientColor(-1);
        gameLevelManager.gameState = GameLevelManager.GameState.TITLE_STATE;
        deviceManager.init(main);

        //Acts as invisible camera target for menu levels or static camera levels
        center = new Object("center", 640, 360, "/centerTest.png", 1, 1f);
        center.target = true;
        GameManager.objects.add(center);
        center.visible = false;
        center.setPosition(0,0);

        fpsCounter = new TextObject("FPS:" + main.getFps() , 0,0,0xffffffff, 1);
        GameManager.textObjects.add(fpsCounter);
    }

    @Override
    public void update(Main main, float dt)
    {
        //Runs ALWAYS in case u couldn't tell
        for(int i = 0; i < objects.size(); i++)
        {
            if((objects.get(i).isActiveOnPlay && isPlaying) || (objects.get(i).isActiveOnPause && !isPlaying))
            {
                objects.get(i).update(main, this, dt);
            }
            if(objects.get(i).isDead())
            {
                objects.remove(i);
                i--;
            }
        }

        Physics.update();

        gameLevelManager.update(main, dt);
        gameLevelManager.currLevel.update(main, dt);
        camera.update(this, main, dt);
        deviceManager.update(main, dt);

        //camera.topCamera = gameLevelManager.currLevel.topCamera;
        //camera.bottomCamera = gameLevelManager.currLevel.bottomCamera;

        Collections.sort(objects);

        fpsCounter.text = "FPS:" + main.getFps();
        fpsCounter.posX = 0;
        if(gameLevelManager.getGameState() != GameLevelManager.GameState.TITLE_STATE && (camera.getPosY() < gameLevelManager.currLevel.verticleBounds.get(0).x && camera.getPosY() > gameLevelManager.currLevel.verticleBounds.get(0).y))
        {
            fpsCounter.posY = (int) camera.getPosY();
        }
        if(gameLevelManager.getGameState() == GameLevelManager.GameState.TITLE_STATE)
        {
            fpsCounter.posY = (int) camera.getPosY();
        }

        if(main.getInput().isKeyDown(KeyEvent.VK_F1))
        {
            if(showFps)
            {
                showFps = false;
                return;
            }
            else
                {
                    showFps = true;
                }
        }
        if(main.getInput().isKeyDown(KeyEvent.VK_ESCAPE))
        {
            if(isPlaying)
            {
                isPlaying = false;
                return;
            }
            else
            {
                isPlaying = true;
            }
        }
    }

    @Override
    public void render(Main main, Renderer renderer)
    {
        /*for(int y = 0; y < levelHeight; y++)
        {
            for(int x = 0; x < levelWidth; x++)
            {
                if(collision[x + y * levelWidth] == 1)
                {
                    renderer.drawFillRectangle(x * 16, y * 16, 16, 16, 0xff0f0f0f);
                }
                else
                    {
                        renderer.drawFillRectangle(x * 16, y * 16, 16, 16, 0xfff9f9f9);
                    }
            }
        }*/
        for(Object obj: objects)
        {
            //Renders all objects
            obj.render(main, renderer);
        }
        for(TextObject textObj: textObjects)
        {
            //Renders all text objects
            if(textObj.visible)
                main.getRenderer().drawText(textObj.text, textObj.posX, textObj.posY, textObj.color, textObj.scale);
        }
        if(showFps)
        {
            main.getRenderer().drawText(fpsCounter.text, fpsCounter.posX, fpsCounter.posY, fpsCounter.color, fpsCounter.scale);
        }
    }

    public boolean getCollision(int x, int y)
    {
        return false;
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
