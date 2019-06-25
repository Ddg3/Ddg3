package com.zach.ddg3;

import com.ivan.xinput.XInputButtons;
import com.ivan.xinput.XInputDevice;
import com.ivan.xinput.enums.XInputButton;
import com.zach.ddg3.components.WeaponComponent;
import com.zach.engine.Main;
//import org.omg.CORBA.INTERNAL;

//import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class mainLevel extends GameLevel
{
    public static ArrayList<Player> players = new ArrayList<Player>(1);

    private static Wall frontWall;
    private static Wall backWall;
    private static Wall[] diagonalWalls = new Wall[4];
    private static Wall[] sideWalls = new Wall[2];
    private static Wall[] spears = new Wall[8];
    private static Object ground;
    private static Object stands;
    private static Wall backGate;
    private static ArrayList<Object> timePedestals = new ArrayList<Object>(1);
    private static ArrayList<Object> pointers = new ArrayList<Object>(1);
    private static ArrayList<TextObject> timers = new ArrayList<TextObject>(1);
    public static TextObject testText;
    private static Vulture vulture1;
    private static Object kingSwan;
    private Player gooseWatched;
    private Player winner;

    public static boolean gameWon = false;
    private static int speakInd = 0;
    private static boolean buttonPressed = false;

    private static int wallInd = 0;
    public WallTile wall;
    public HoleTile hole;
    public WallTile wall2;
    private Vulture vulture2;

    @Override
    public void init(Main main)
    {
        /*players.add(new Player("player1", 63, 68, "/duckSheetLong.png", 24, 0.01f, 0));
        players.get(0).zIndex = 5;
        players.get(0).addComponent(new WeaponComponent(players.get(0), "rocketLauncher"));
        players.get(0).changeSprite(102, 81, "/Duck_rocketLauncher.png", 16, 0.1f);
        players.get(0).setGoose(true);
        players.get(0).changeSpecies();
        GameManager.objects.add(players.get(0));
        GameManager.players.add(players.get(0));*/

        /*players.add(new Player("player2", 63, 68, "/duckSheetLong.png", 24, 0.01f, 0));
        players.get(1).zIndex = 5;
        players.get(1).addComponent(new WeaponComponent(players.get(1), "rocketLauncher"));
        /*players.get(1).changeSprite(102, 81, "/Duck_rocketLauncher.png", 16, 0.1f);
        players.get(1).setGoose(true);
        players.get(1).changeSpecies();
        GameManager.objects.add(players.get(1));
        GameManager.players.add(players.get(1));*/

        int randGoose = ThreadLocalRandom.current().nextInt(0, 2);

        speakInd = 0;
        gameWon = false;
        GameManager.camera.resetCamera();
        this.verticleBounds.clear();
        this.horizBounds.clear();
        GameManager.cameraPlayers.clear();

        this.verticleBounds.add(new Vector(-15, -350));
        this.horizBounds.add(new Vector(16, -17));

        for(int i = 0; i < GameManager.players.size(); i++)
        {
            if(GameManager.players.get(i) != null)
            {
                players.add(GameManager.players.get(i));
                players.get(i).setPosition(((i * 400) -200),0);
                GameManager.objects.add(players.get(i));
                GameManager.objects.add(GameManager.timers.get(i));
                GameManager.cameraPlayers.add(players.get(i));
                players.get(i).setGrabbed(false);

                if(i == randGoose)
                {
                    players.get(i).setGoose(true);
                    players.get(i).changeSpecies();
                }
            }
        }

        frontWall = new Wall("frontWall", 398, 116, "/frontWall.png", 1, 0.1f, false);
        frontWall.position.y = -206;
        frontWall.zIndex = 1;
        frontWall.paddingTop = 106;
        GameManager.objects.add(frontWall);

        /*backWall = new Wall("backWall", 639, 184, "/BackWalls.png", 1, 0.1f, false);
        backWall.position.y = 120;
        backWall.zIndex = 3;
        backWall.paddingTop = 100;
        backWall.setOffsetCenterY(80);
        GameManager.objects.add(backWall);*/

        backGate = new Wall("backGate", 639, 58, "/backFence.png", 1, 0.1f, false);
        backGate.position.y = 130;
        backGate.zIndex = 7;
        backGate.paddingTop = 30;
        backGate.setOffsetCenterY(35);
        GameManager.objects.add(backGate);

        ground = new Object("ground", 640, 360, "/ground.png", 1, 0.1f);
        ground.zIndex = 0;
        GameManager.objects.add(ground);

        stands = new Object("stands", 690, 280, "/stands.png", 1, 0.1f);
        stands.zIndex = 3;
        stands.position.y = -236;
        GameManager.objects.add(stands);

        diagonalWalls[0] = new Wall("diagonalWall0", 57, 146, "/halfDoor1.png", 2, 0.1f, false);
        diagonalWalls[0].position.y = -171;
        diagonalWalls[0].position.x = -291;
        diagonalWalls[0].zIndex = 1;
        diagonalWalls[0].paddingTop = 100;
        diagonalWalls[0].setOffsetCenterX(-30);
        GameManager.objects.add(diagonalWalls[0]);

        diagonalWalls[1] = new Wall("diagonalWall1", 63, 135, "/halfDoor2.png", 2, 0.1f, false);
        diagonalWalls[1].position.y = -197;
        diagonalWalls[1].position.x = -231;
        diagonalWalls[1].zIndex = 1;
        diagonalWalls[1].paddingTop = 120;
        GameManager.objects.add(diagonalWalls[1]);

        diagonalWalls[2] = new Wall("diagonalWall2", 57, 146, "/halfDoor1.png", 2, 0.1f, false);
        diagonalWalls[2].position.y = -171;
        diagonalWalls[2].position.x = 290;
        diagonalWalls[2].zIndex = 1;
        diagonalWalls[2].paddingTop = 100;
        diagonalWalls[2].setOffsetCenterX(30);
        diagonalWalls[2].setFrame(1);
        GameManager.objects.add(diagonalWalls[2]);

        diagonalWalls[3] = new Wall("diagonalWall3", 63, 135, "/halfDoor2.png", 2, 0.1f, false);
        diagonalWalls[3].position.y = -197;
        diagonalWalls[3].position.x = 230;
        diagonalWalls[3].zIndex = 1;
        diagonalWalls[3].paddingTop = 120;
        diagonalWalls[3].setFrame(1);
        GameManager.objects.add(diagonalWalls[3]);

        sideWalls[0] = new Wall("sideWall0", 16, 392, "/sideWalls.png", 2, 0.1f, false);
        sideWalls[0].position.x = -327;
        sideWalls[0].position.y = -25;
        sideWalls[0].zIndex = 3;
        GameManager.objects.add(sideWalls[0]);

        sideWalls[1] = new Wall("sideWall1", 16, 392, "/sideWalls.png", 2, 0.1f, false);
        sideWalls[1].position.x = 327;
        sideWalls[1].position.y = -25;
        sideWalls[1].setFrame(1);
        sideWalls[1].zIndex = 3;
        GameManager.objects.add(sideWalls[1]);

        for(int i = 0; i < spears.length; i += 2)
        {
            spears[i] = new Wall("spear" + i, 9, 58, "/spearPole.png", 2, 0.1f, false);
            spears[i].setPosition(-284 + (i * 7), -146 - (i * 3));
            spears[i].zIndex = 1;
            spears[i].paddingTop = 40;
            spears[i].setOffsetCenterY(-30);
            GameManager.objects.add(spears[i]);
            spears[i + 1] = new Wall("spear" + i + 1, 9, 58, "/spearPole.png", 2, 0.1f, false);
            spears[i + 1].setPosition(283 - (i * 7), -146 - (i * 3));
            spears[i + 1].zIndex = 1;
            spears[i + 1].paddingTop = 40;
            spears[i + 1].setOffsetCenterY(-30);
            spears[i + 1].setFrame(1);
            GameManager.objects.add(spears[i + 1]);
        }


        for (int i = 0; i < players.size(); i++)
        {
            /*pointers.add(i, new Object("pointer" + i, 4, 2, "/pointer.png", 1, 1));
            GameManager.objects.add(pointers.get(i));
            pointers.get(i).zIndex = Integer.MAX_VALUE;

            timers.add(i, new TextObject("" + players.get(i).getTime(), (int)pointers.get(i).getPositionX(), (int)pointers.get(i).getPositionY(), 0xffffffff, 1));
            GameManager.textObjects.add(timers.get(i));*/

            timePedestals.add(i, new Object("playerFrame" + i, 160, 58, "/playerFrameNew.png", 2, 0));
            GameManager.objects.add(timePedestals.get(i));
            timePedestals.get(i).zIndex = Integer.MAX_VALUE - 1;
            timePedestals.get(i).getObjImage().changeColor(players.get(i).getSkinColors()[1], players.get(i).getSkinColors()[players.get(i).getSkIndex()]);
            if(players.get(i).isGoose())
            {
                timePedestals.get(i).setFrame(1);
            }
        }

        testText = new TextObject("" , (int)(GameManager.center.position.x),(int)(GameManager.center.position.y + 320),0xffffffff, 1);
        GameManager.textObjects.add(testText);

        /*vulture1 = new Vulture(players.get(0), 0);
        GameManager.objects.add(vulture1);
        vulture1.setPosition(-200, -450);
        vulture1.targetPosition.add(new Vector(-110, -290));
        vulture1.targetPosition.add(new Vector(250, -450));
        vulture1.getObjImage().changeColor(players.get(0).getSkinColors()[1], players.get(0).getSkinColors()[players.get(0).getSkIndex()]);

        vulture2 = new Vulture(players.get(1), 0);
        GameManager.objects.add(vulture2);
        vulture2.setPosition(-200, -450);
        vulture2.targetPosition.add(new Vector(-20, -190));
        vulture2.targetPosition.add(new Vector(250, -450));
        vulture2.getObjImage().changeColor(players.get(1).getSkinColors()[1], players.get(1).getSkinColors()[players.get(1).getSkIndex()]);*/

        kingSwan = new Object("kingSwan", 92, 95, "/swanSpeak.png", 4, 0.1f);
        kingSwan.setPosition(-10,-301);
        kingSwan.zIndex = 200;
        kingSwan.setFrame(1);
        GameManager.objects.add(kingSwan);

        setWalls(2);
    }

    @Override
    public void update(Main main, float dt)
    {
        /*if(testText != null)
        {
            testText.posY = (int) (GameManager.center.position.y + 320);
        }*/
        //testText.posX = (int)(GameManager.center.position.x + 320);
        //testText.posY = (int)(GameManager.center.position.y + 160);
        /*for (int i = 0; i < players.size(); i++)
        {
            pointers.get(i).setPosition(players.get(i).position.x, players.get(i).position.y - (players.get(i).getBaseHeight() / 2));

            timers.get(i).posX = (int)pointers.get(i).offsetPos.x - 5;
            timers.get(i).posY = (int)pointers.get(i).offsetPos.y - 10;
            timers.get(i).text = "" + players.get(i).getTime();
        }*/

        /*if(players.size() != 0)
        {
            for (int i = 0; i < players.size(); i++)
            {
                GameManager.objects.add(timePedestals.get(i));
                timePedestals.get(i).zIndex = Integer.MAX_VALUE;
            }
        }*/
        //System.out.println(Math.abs(frontWall.getPositionY() - (frontWall.getHeight() / 2)) - (players.get(0).getPositionY() + 320));
        /*System.out.println(players.get(0).position.x + ", " + players.get(0).position.y);
        System.out.println(GameManager.camera.boundsRange);
        System.out.println(GameManager.camera.getPosX() + ", " + GameManager.camera.getPosY());*/

        for(int i = 0; i < timePedestals.size(); i++)
        {
            pedestalFollow(timePedestals.get(i), i);
        }

        if(GameManager.cameraPlayers.size() <= 1 && winner == null)
        {
            for(int i = 0; i < GameManager.cameraPlayers.size(); i++)
            {
                if(GameManager.cameraPlayers.get(i) != null)
                {
                    winner = GameManager.cameraPlayers.get(i);
                    break;
                }
            }
        }
        if(GameManager.cameraPlayers.size() <= 1 && winner != null)
        {
            swanSpeak(winner, main);
        }
        if(players.size() > 0)
        {
            swanFollow();
        }
    }

    public void setWalls(int i)
    {
        switch(i)
        {
            /*case 0:
                //3-tile line in the middle
                setNewWall(WallTile.directions.BETWEEN_HORIZof, 0, -50);
                wall.setWallFrom(WallTile.directions.LEFTof);
                wall.setWallFrom(WallTile.directions.RIGHTof);
                break;
            case 1:
                //Two 2-tile lines on top and one tile on bottom; extending vertically
                setNewWall(WallTile.directions.UPof, -120, -120);
                wall.setWallFrom(WallTile.directions.DOWNof);

                setNewWall(WallTile.directions.UPof, 120, -120);
                wall.setWallFrom(WallTile.directions.DOWNof);

                setNewWall(WallTile.directions.SINGLE, 0, 110);
                break;
            case 2:
                //Four tile diamond
                setNewWall(WallTile.directions.SINGLE, 0, -120);
                setNewWall(WallTile.directions.SINGLE, -160, 0);
                setNewWall(WallTile.directions.SINGLE, 160, 0);
                setNewWall(WallTile.directions.SINGLE, 0, 110);
                break;*/
            case 0:
                //setNewHole();
                break;
            case 1:

                break;
            case 2:

                break;
        }
    }

    public void setNewWall(WallTile.directions dir, float posX, float posY)
    {
        wall = new WallTile();
        wall.position = new Vector(posX, posY);
        wall.zIndex = 3;
        wall.setFrame(dir.getFrame());
        GameManager.objects.add(wall);
    }
    public void setNewHole(HoleTile.directions dir, float posX, float posY)
    {
        hole = new HoleTile();
        hole.position = new Vector(posX, posY);
        hole.zIndex = 3;
        hole.setFrame(dir.getFrame());
        GameManager.objects.add(hole);
    }
    public static void pedestalFollow(Object pedestal, int index)
    {
        //GameManager.timers.get(index).offsetPos.x = pedestal.offsetPos.x;
        //GameManager.timers.get(index).offsetPos.y = pedestal.offsetPos.y;
        GameManager.timers.get(index).visible = true;
        GameManager.timers.get(index).zIndex = Integer.MAX_VALUE;
    }

    public void swanSpeak(Player player, Main main)
    {
        GameManager.camera.setPath(kingSwan.position);

        XInputDevice device = player.device;

        if(GameManager.camera.getPosY() <= kingSwan.position.y + 1 && GameManager.camera.getPosY() >= kingSwan.position.y - 1)
        {
            GameManager.camera.setMovingAlongVector(false);
        }
        if(speakInd == 0)
        {
            kingSwan.speak("Cease!", 0xff000000);
            GameManager.camera.setMovingAlongVector(true);
            gameWon = true;
            speakInd = 1;
        }
        if(((!player.isKeyBoard() && device.getDelta().getButtons().isPressed(XInputButton.A)) || main.getInput().isKeyDown(player.getKeySelect())) && speakInd == 1)
        {
            kingSwan.speak("Victory is yours, /my duckling!", 0xff000000);
            speakInd = 2;
            buttonPressed = true;
        }
        if((!player.isKeyBoard() && (!device.getDelta().getButtons().isPressed(XInputButton.A))) || main.getInput().isKeyUp(player.getKeySelect()) && buttonPressed)
        {
            buttonPressed = false;
        }
        if(((!player.isKeyBoard() && device.getDelta().getButtons().isPressed(XInputButton.A)) || main.getInput().isKeyDown(player.getKeySelect())) && speakInd == 2 && !buttonPressed && GameManager.gameLevelManager.getGameState() == GameLevelManager.GameState.MAIN_STATE)
        {
            if(GameManager.firstTime)
            {
                GameManager.firstTime = false;
            }
            speakInd = 0;
            GameManager.camera.resetCamera();
            uninit();
            GameManager.gameLevelManager.setGameState(GameLevelManager.GameState.SELECTION_STATE);
            GameManager.gameLevelManager.currLevel.loadPoint = Integer.MAX_VALUE;
        }
    }
    public void swanFollow()
    {
        for(int i = 0; i < players.size(); i++)
        {
            if(players.get(i).isGoose())
            {
                gooseWatched = players.get(i);
                break;
            }
        }
        if(gooseWatched.position.x >= kingSwan.position.x - 25 && gooseWatched.position.x <= kingSwan.position.x + 25)
        {
            kingSwan.setFrame(1);
        }
        else if(gooseWatched.position.x <= kingSwan.position.x - 25)
        {
            kingSwan.setFrame(0);
        }
        else if(gooseWatched.position.x >= kingSwan.position.x + 25)
        {
            kingSwan.setFrame(2);
        }

    }
    @Override
    public void uninit()
    {
        GameManager.center.setPosition(0,0);
        GameManager.camera.setPosX(0);
        GameManager.camera.setPosY(0);
        GameManager.objects.clear();
        GameManager.textObjects.clear();
        players.clear();
        timePedestals.clear();
        gooseWatched = null;
        winner = null;
        gameWon = false;
        speakInd = 0;
    }

    public static ArrayList<Object> getTimePedestals() {
        return timePedestals;
    }

    public static void setTimePedestals(ArrayList<Object> timePedestals) {
        mainLevel.timePedestals = timePedestals;
    }
}
