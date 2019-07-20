package com.zach.ddg3;

import com.ivan.xinput.XInputButtons;
import com.ivan.xinput.XInputDevice;
import com.ivan.xinput.enums.XInputButton;
import com.zach.ddg3.components.AABBComponent;
import com.zach.ddg3.components.WeaponComponent;
import com.zach.engine.Main;
//import org.omg.CORBA.INTERNAL;

//import javax.xml.soap.Text;
import java.util.*;
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
    private static ArrayList<Object> pointers = new ArrayList<Object>(1);
    private static ArrayList<TextObject> timers = new ArrayList<TextObject>(1);
    private static Vulture vulture1;
    private static Object kingSwan;
    private Player gooseWatched;
    private Player winner;
    private static boolean flying = false;
    private boolean running = false;
    private boolean spitting = false;
    private static Random random;


    public static boolean gameWon = false;
    private static int speakInd = 0;
    private static boolean buttonPressed = false;

    private static int wallInd = 0;
    public static WallTile wall;
    public HoleTile hole;
    public static WallTile wall2;
    private Vulture vulture2;


    private static Object swanShadow = new Object("shadow", 242, 198, "/swanShadow.png", 1, 1);
    private static Vector[] dropPoints = new Vector[3];
    private static WeaponComponent swanWeapon;

    private float hazardTimer = 2f;
    private static float tempHazardTimer = 3f;
    private int hazardInd = 0;
    private ArrayList<Integer> hazardList = new ArrayList<>(1);
    private static int reticleInd = 0;
    private float reticleBuffer = 0.05f;
    private float tempReticleBuffer = 0;
    private static float dropTimer = 2f;
    private static float tempDropTimer = dropTimer;
    private static boolean dropping = false;
    private static boolean flyingBack = false;

    private boolean keyPressed = false;

    @Override
    public void init(Main main)
    {
        GameManager.objects.add(swanShadow);
        swanShadow.zIndex = Integer.MAX_VALUE - 2;
        swanShadow.position.y = -700;
        for(int i = 0; i < 3; i++)
        {
            hazardList.add(i);
        }

        for (int i = 0; i < dropPoints.length; i++)
        {
            dropPoints[i] = new Vector(0,0);
        }

        Collections.shuffle(hazardList);
        /*players.add(new Player("player1", 63, 68, "/duckSheetLong.png", 24, 0.01f, 0));
        players.get(0).zIndex = 5;
        players.get(0).addComponent(new WeaponComponent(players.get(0), "rocketLauncher"));
        players.get(0).changeSprite(102, 81, "/Duck_rocketLauncher.png", 16, 0.1f);
        players.get(0).setGoose(true);
        players.get(0).changeSpecies();
        GameManager.objects.add(players.get(0));
        GameManager.players.add(players.get(0));

        players.add(new Player("player2", 63, 68, "/duckSheetLong.png", 24, 0.01f, 0));
        players.get(1).zIndex = 5;
        players.get(1).addComponent(new WeaponComponent(players.get(1), "rocketLauncher"));
        players.get(1).changeSprite(102, 81, "/Duck_rocketLauncher.png", 16, 0.1f);
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

        for(int i = 0; i < players.size(); i++)
        {
            GameManager.objects.add(GameManager.timePedestals.get(i));
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
        kingSwan.setPosition(0,-301);
        kingSwan.zIndex = 200;
        kingSwan.setFrame(1);
        GameManager.objects.add(kingSwan);

        for(int i = 0; i < players.size(); i++)
        {
            if (players.get(i).isGoose())
            {
                GameManager.timePedestals.get(i).setFrame(1);
            }
        }

        setWalls(2);

        kingSwan.tag = "Swan";
        swanWeapon = new WeaponComponent(kingSwan, "rocketLauncher");
        kingSwan.addComponent(swanWeapon);
    }

    @Override
    public void update(Main main, float dt)
    {
        //kingSwan.animate(dt);
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

        tempHazardTimer -= dt;
        tempReticleBuffer -= dt;

        if(dropping)
        {
            tempDropTimer -= dt;

            if(tempDropTimer <= 0)
            {
                dropBomb(dropPoints[reticleInd]);
                dropTimer /= 1.5f;
                tempDropTimer = dropTimer;

                if(reticleInd == 2)
                {
                    dropping = false;
                }
                else
                    {
                        reticleInd++;
                    }
            }
        }

        if(tempHazardTimer <= 0)
        {
            hazard(0, dt);
            if(hazardInd == hazardList.size() - 1)
            {
                hazardInd = 0;
            }
            else
                {
                    hazardInd++;
                }
            tempHazardTimer = hazardTimer * 10000;
        }

        if(flying)
        {
            if (kingSwan.position.y > -400)
            {
                kingSwan.position.y -= 50f * dt;
            }
            else
                {
                    kingSwan.position.x = swanShadow.position.x;
                    swanShadow.position.y += 170f * dt;

                    for (int i = 0; i < dropPoints.length; i++)
                    {
                        if (swanShadow.position.y <= dropPoints[i].y + 2 && swanShadow.position.y >= dropPoints[i].y - 2)
                        {
                            if(tempReticleBuffer <= 0)
                            {
                                Object reticle = new Object("reticle", 113, 57, "/reticle.png", 2, 0.01f);
                                reticle.position.y = swanShadow.position.y;
                                reticle.position.x = swanShadow.position.x;
                                GameManager.objects.add(reticle);
                                reticle.zIndex = 0;
                                reticle.addComponent(new AABBComponent(reticle, "trigger"));
                                reticle.tag = "Trigger";
                                reticle.offsetPos = new Vector(reticle.position.x + 640, reticle.position.y + 360);
                                tempReticleBuffer = reticleBuffer;

                                if(!dropping)
                                {
                                    dropping = true;
                                }
                            }
                        }
                    }

                    if(swanShadow.position.y > 1200)
                    {
                        flying = false;
                        kingSwan.position.x = 0;
                        flyingBack = true;
                    }
                }
        }
        else if(flyingBack)
        {
            if (kingSwan.position.y < -301)
            {
                kingSwan.position.y += 50f * dt;
            }
            else
                {
                    flyingBack = false;
                }
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

    public void hazard(int hazardNum, float dt)
    {
        switch (hazardNum)
        {
        case 0:
            if (!flying)
            {
                swanShadow.position.y = -500;
                swanShadow.position.x = 0;
                kingSwan.changeSprite(121, 96, "/swanFly.png", 10, 0.01f);
                kingSwan.playInRangeAndBack(0, 9);
                flying = true;

                random = new Random();

                int randomX = random.nextInt((int) (sideWalls[1].position.x * 2)) + (int) sideWalls[0].position.x;
                swanShadow.position.x = randomX;

                for (int i = 0; i < dropPoints.length; i++)
                {
                    dropPoints[i].x = randomX;
                    dropPoints[i].y = (i * 120) - 120;
                }
            }
            break;
            case 1:

                break;
            case 2:

                break;
        }
    }

    public void dropBomb(Vector position)
    {
        swanWeapon.shoot("egg", 78, 90, "/eggBomb.png", 1, 1, 0, position, 425f);
        swanWeapon.isTriggered = true;
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
        if((device.getDelta().getButtons().isPressed(XInputButton.A) || main.getInput().isKeyDown(player.getKeySelect())) && speakInd == 1)
        {
            kingSwan.speak("Victory is yours, /my duckling!", 0xff000000);
            speakInd = 2;
            if(device.getDelta().getButtons().isPressed(XInputButton.A))
            {
                buttonPressed = true;
            }
            if(main.getInput().isKeyDown(player.getKeySelect()))
            {
                keyPressed = true;
            }
        }
        if(!device.getDelta().getButtons().isPressed(XInputButton.A) && buttonPressed)
        {
            buttonPressed = false;
        }
        if(main.getInput().isKeyUp(player.getKeySelect()) && keyPressed)
        {
            keyPressed = false;
        }
        if(((device.getDelta().getButtons().isPressed(XInputButton.A) && !buttonPressed) || (main.getInput().isKeyDown(player.getKeySelect()) && !keyPressed)) && speakInd == 2 && GameManager.gameLevelManager.getGameState() == GameLevelManager.GameState.MAIN_STATE)
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
        //timePedestals.clear();
        gooseWatched = null;
        winner = null;
        gameWon = false;
        speakInd = 0;

        for(int i = 0; i < players.size(); i++)
        {
            GameManager.objects.remove(GameManager.timePedestals.get(i));
        }
    }

    /*public static ArrayList<Object> getTimePedestals() {
        return timePedestals;
    }

    public static void setTimePedestals(ArrayList<Object> timePedestals) {
        mainLevel.timePedestals = timePedestals;
    }*/
}
