package com.zach.ddg3;

import com.zach.ddg3.components.AABBComponent;
import com.zach.engine.AbstractGame;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

import javax.lang.model.type.ArrayType;
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
    public static ArrayList<Player> cameraPlayers = new ArrayList<Player>(1);
    public static ArrayList<TextObject> textObjects = new ArrayList<TextObject>(1);
    private ArrayList<Object> toKillList = new ArrayList<Object>();
    public static ArrayList<Object> timers = new ArrayList<Object>(1);
    public static ArrayList<Object> indicators = new ArrayList<>(2);
    private int levelWidth;
    private int levelHeight;
    private boolean[] collision;
    private boolean isColliding = false;
    private static TextObject fpsCounter = null;
    private boolean showFps = true;
    private boolean showHitboxes = false;
    private boolean isPlaying = true;
    public static boolean firstTime = true;

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
        indicators.add(0, null);
        indicators.add(1, null);
        main.getRenderer().setAmbientColor(-1);
        gameLevelManager.gameState = GameLevelManager.GameState.SELECTION_STATE;
        deviceManager.init(main);

        //Acts as invisible camera target for menu levels or static camera levels
        center = new Object("center", 640, 360, "/centerTest.png", 1, 1f);
        center.target = true;
        //GameManager.objects.add(center);
        //center.visible = false;
        //center.zIndex = Integer.MAX_VALUE;

        center.setPosition(0,0);
        center.addComponent(new AABBComponent(center, "camera"));

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
                center.update(main, this, dt);
                objects.get(i).update(main, this, dt);
                //center.update(main, this, dt);
            }
            if(objects.get(i).isDead())
            {
                objects.remove(i);
                i--;
            }
        }
        Physics.update(main);

        gameLevelManager.update(main, dt);
        gameLevelManager.currLevel.update(main, dt);

        if(!(gameLevelManager.getGameState() == GameLevelManager.GameState.MAIN_STATE && mainLevel.gameWon))
        {
            cameraFollow();
        }
        camera.update(this, main, dt);
        if(!camera.isStopped())
        {
            //cameraFollow();
        }
        deviceManager.update(main, dt);

        //camera.topCamera = gameLevelManager.currLevel.topCamera;
        //camera.bottomCamera = gameLevelManager.currLevel.bottomCamera;

        Collections.sort(objects);

        fpsCounter.text = "FPS:" + main.getFps();
        fpsCounter.posX = (int)(center.position.x);
        fpsCounter.posY = (int)(center.position.y);

        if(gameLevelManager.getGameState() == GameLevelManager.GameState.MAIN_STATE)
        {
            if(mainLevel.testText != null)
            {
                mainLevel.testText.posX = (int) (center.position.x);
                mainLevel.testText.posY = (int) (center.position.y + 320);
            }
            for(int i = 0; i < mainLevel.getTimePedestals().size(); i++)
            {
                Object pedestal =  mainLevel.getTimePedestals().get(i);
                Object timer = timers.get(i);
                Object ind = indicators.get(i);
                pedestal.offsetPos.x = mainLevel.testText.posX + (i * pedestal.width);
                pedestal.offsetPos.y = mainLevel.testText.posY - (pedestal.height / 2) + 8;
                timer.offsetPos.x = pedestal.offsetPos.x + 55;
                timer.offsetPos.y = pedestal.offsetPos.y + 17;
                ind.offsetPos.x = pedestal.offsetPos.x + 100;
                ind.offsetPos.y = pedestal.offsetPos.y + 14;
            }
        }
        /*System.out.println(fpsCounter.posY);
        System.out.println("CamX: " + gameLevelManager.currLevel.verticleBounds.get(camera.boundsRange).x);
        System.out.println("CamY: "+ gameLevelManager.currLevel.verticleBounds.get(camera.boundsRange).y);*/
        /*fpsCounter.posY = (int)center.position.y;
        fpsCounter.posX = 0;*/

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
        if(main.getInput().isKeyDown(KeyEvent.VK_0))
        {
            if(showHitboxes)
            {
                showHitboxes = false;
                return;
            }
            else
            {
                showHitboxes = true;
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
        for(Object obj: objects)
        {
            //Renders all objects
            obj.render(main, renderer);
            if(showHitboxes)
            obj.renderComponents(main, renderer);
        }
        if(gameLevelManager.getGameState() == GameLevelManager.GameState.MAIN_STATE)
        {
            for (Object ind : indicators) {
                ind.render(main, renderer);
            }
        }
        for(TextObject textObj: textObjects)
        {
            //Renders all text objects
            if(textObj.visible)
                main.getRenderer().drawText(textObj.text, textObj.posX, textObj.posY, textObj.color, textObj.scale, textObj.font);
        }
        if(showFps)
        {
            main.getRenderer().drawText(fpsCounter.text, fpsCounter.posX, fpsCounter.posY, fpsCounter.color, fpsCounter.scale, renderer.StandardFont);
        }
        if(showHitboxes)
        {
            center.render(main, renderer);
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
    public static void removeTextObjectsByName(String name)
    {
        for(int i = 0; i < textObjects.size(); i++)
        {
            if(textObjects.get(i).tag.equalsIgnoreCase(name))
            {
                textObjects.remove(i);
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
    public void cameraFollow()
    {
        if (cameraPlayers.size() == 3)
        {
            if(cameraPlayers.get(0).isInGame() && cameraPlayers.get(1).isInGame() && cameraPlayers.get(2).isInGame())
            {
                float slope1 = (players.get(2).position.y - players.get(1).position.y) / (players.get(2).position.x - players.get(1).position.x);
                float perpSlope1 = -1 / slope1;
                float midX1 = (players.get(2).position.x + players.get(1).position.x) / 2;
                float midY1 = (players.get(2).position.y + players.get(1).position.y) / 2;

                float slope2 = (players.get(2).position.y - players.get(1).position.y) / (players.get(2).position.x - players.get(1).position.x);
                float perpSlope2 = -1 / slope2;
                float midX2 = (players.get(2).position.x + players.get(1).position.x) / 2;
                float midY2 = (players.get(2).position.y + players.get(1).position.y) / 2;

                float circumcenterX = (((perpSlope1 * midX1) + midY1) + ((perpSlope2 * midX2) + midY2)) / (perpSlope1 - perpSlope2);
                float circumcenterY = ((perpSlope1 * circumcenterX) - (perpSlope1 * midX1) + midY1);

                GameManager.center.setPosition(circumcenterX, circumcenterY);
            }
            else
            {
                GameManager.center.setPosition(0, 0);
            }
        }
        else if (cameraPlayers.size() == 2)
        {
            if(cameraPlayers.get(0).isInGame() && !players.get(0).isTimedOut() && cameraPlayers.get(1).isInGame() && !players.get(1).isTimedOut())
            {
                int posX = ((int)players.get(0).position.x + (int)players.get(1).position.x) / 2;
                int posY = ((int)players.get(0).position.y + (int)players.get(1).position.y) / 2;

                GameManager.center.setPosition(posX, posY);
            }
            else
            {
                GameManager.center.setPosition(0, 0);
            }
        }
        else if (cameraPlayers.size() == 1)
        {
            if(cameraPlayers.get(0).isInGame() && !players.get(0).isTimedOut())
            {
                //GameManager.center.position.x = (int)players.get(0).position.x;
                GameManager.center.position.y = (int)players.get(0).position.y;
                }
            else
                {
                    GameManager.center.setPosition(0, 0);
                }
        }
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
