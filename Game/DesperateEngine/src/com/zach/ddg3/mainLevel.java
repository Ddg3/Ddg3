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
    private static Object[] doorBacks = new Object[2];
    private static Wall[] sideWalls = new Wall[2];
    private static Wall[] spears = new Wall[8];
    private static Object ground;
    private static Object ground2;
    private static Object stands;
    private static Wall backGate;
    private static Object backGateTrigger;
    private static ArrayList<Object> pointers = new ArrayList<Object>(1);
    private static ArrayList<TextObject> timers = new ArrayList<TextObject>(1);
    private static Vulture vulture1;
    private static Object kingSwan;
    private static Object nest;

    private static ArrayList<Object> debris = new ArrayList<>(1);
    private static Object[] topStands = new Object[2];
    private static Object[] bottomStands = new Object[2];
    private static ArrayList<Object> birds = new ArrayList<>(1);

    private static int debrisCount;
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


    private static Object swanShadow = new Object("shadow", 396, 292, "/swanShadow.png", 1, 0.01f);
    private static Vector[] dropPoints = new Vector[3];
    private static WeaponComponent swanWeapon;

    private static float hazardTimer = 30f;
    private static float tempHazardTimer = 2f;
    private static int hazardInd = 0;
    private static ArrayList<Integer> hazardList = new ArrayList<>(1);
    private static int reticleInd = 0;
    private float reticleBuffer = 0.5f;
    private float tempReticleBuffer = 0;
    private static float dropTimer = 2f;
    private static float tempDropTimer = dropTimer;
    private static boolean dropping = false;
    private static boolean flyingBack = false;
    private static boolean movingL = false;
    private static boolean movingR = false;
    private static float spearBuffer = 2f;
    private static float tempSpearBuffer = spearBuffer;
    private static boolean risedL = false;
    private static boolean risedR = false;

    private static Object[] pelicans = new Object[2];
    private static WeaponComponent[] pelicanWeapons = new WeaponComponent[2];
    private static int shotIndex = 0;
    private static boolean moveP = false;
    private static boolean leaveP = false;
    private static boolean compAdd = false;

    private static boolean readyShoot = false;
    private static float pelicanCooldown = 3.5f;
    private static int tempShotInd = 1;
    private static float pelicanTempCooldown = pelicanCooldown;

    private static Object[] ostrichGens = new Object[2];
    private static float ostrichTimer = 5f;
    private static float ostrichTimerOffset = 2f;
    private static float tempOstrichTimerOffset = ostrichTimerOffset;
    private static float tempOstrichTimer = ostrichTimer;
    private static boolean ostrichFinished = false;
    private static boolean ostrichHazard = false;
    private static boolean ostrichL = false;
    private static int ostrichInt = 0;

    public static boolean starting = false;
    private static float startOffsetTimer = 1.5f;
    private static float tempStartOffsetTimer = 0f;
    private static Object counter;
    private static int startInd = 0;
    private static int ostrichYSpeed;
    private static Object ostrichTrigger = new Object("ostrichGenL", 53, 83, "/pelican.png", 3, 0.01f);

    private boolean keyPressed = false;
    private float birdTimer = 3f;
    private float tempBirdTimer = birdTimer;

    @Override
    public void init(Main main)
    {
        ostrichGens[0] = new Object("ostrichGenL", 53, 83, "/pelican.png", 3, 0.01f);
        ostrichGens[1] = new Object("ostrichGenR", 53, 83, "/pelican.png", 3, 0.01f);
        ostrichGens[0].position = new Vector(-296, -176);
        ostrichGens[1].position = new Vector(296, -176);
        ostrichGens[0].addComponent(new WeaponComponent(ostrichGens[0], "ostrichLauncher"));
        ostrichGens[1].addComponent(new WeaponComponent(ostrichGens[1], "ostrichLauncher"));
        GameManager.objects.add(ostrichGens[0]);
        GameManager.objects.add(ostrichGens[1]);

        pelicans[0] = new Object("pelican0", 53, 83, "/pelican.png", 3, 0.01f);
        pelicans[1] = new Object("pelican1", 53, 83, "/pelican.png", 3, 0.01f);
        GameManager.objects.add(pelicans[0]);
        pelicans[0].setFrame(2);
        pelicans[0].zIndex = Integer.MAX_VALUE;
        GameManager.objects.add(pelicans[1]);
        pelicans[1].setFrame(0);
        pelicans[1].zIndex = Integer.MAX_VALUE;
        pelicans[0].position = new Vector(-386, -186);
        pelicans[1].position = new Vector(386, -186);
        pelicanWeapons[0] = new WeaponComponent(pelicans[0], "rocketLauncher");
        pelicanWeapons[1] = new WeaponComponent(pelicans[1], "rocketLauncher");
        pelicans[0].setKnockable(false);
        pelicans[1].setKnockable(false);
        pelicans[0].zIndex = 2;
        pelicans[1].zIndex = 2;
        pelicans[0].tag = "Pelican";
        pelicans[1].tag = "Pelican";
        pelicans[0].addComponent(new AABBComponent(pelicans[0], "pelican"));
        pelicans[1].addComponent(new AABBComponent(pelicans[0], "pelican"));

        GameManager.objects.add(swanShadow);
        swanShadow.zIndex = Integer.MAX_VALUE - 2;
        swanShadow.position.y = -700;

        GameManager.textObjects.add(GameManager.altReloadText.get(0));
        GameManager.textObjects.add(GameManager.altReloadText.get(1));

        for(int i = 0; i < GameManager.pauseUI.size(); i++)
        {
            GameManager.objects.add(GameManager.pauseUI.get(i));
        }

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

        backGateTrigger = new Object("backGateTrigger", 639, 58, "/backFence.png", 1, 0.1f);
        backGateTrigger.addComponent(new AABBComponent(backGateTrigger, "trigger"));
        backGateTrigger.position.y = 130;
        backGateTrigger.zIndex = 7;
        backGateTrigger.paddingTop = 30;
        backGateTrigger.setOffsetCenterY(35);
        backGateTrigger.tag = "Trigger";
        GameManager.objects.add(backGateTrigger);

        ground = new Object("ground", 640, 360, "/ground.png", 1, 0.1f);
        ground.zIndex = 0;
        GameManager.objects.add(ground);

        ground2 = new Object("ground2", 640, 360, "/ground2.png", 1, 0.1f);
        ground2.zIndex = 0;
        ground2.position.y = -500;
        GameManager.objects.add(ground2);

        stands = new Object("stands", 690, 280, "/stands.png", 1, 0.1f);
        stands.zIndex = 3;
        stands.position.y = -236;
        GameManager.objects.add(stands);

        diagonalWalls[0] = new Wall("diagonalWall0", 57, 146, "/halfDoor1.png", 2, 0.1f, false);
        diagonalWalls[0].position.y = -171;
        diagonalWalls[0].position.x = -291;
        diagonalWalls[0].zIndex = 2;
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
        diagonalWalls[2].zIndex = 2;
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

        doorBacks[0] = new Object("doorBack0", 51, 84, "/doorBackground.png", 2, 0.01f);
        doorBacks[0].position.x = -268;
        doorBacks[0].position.y = -161;
        doorBacks[0].zIndex = 0;
        GameManager.objects.add(doorBacks[0]);

        doorBacks[1] = new Object("doorBack1", 51, 84, "/doorBackground.png", 2, 0.01f);
        doorBacks[1].position.x = 267;
        doorBacks[1].position.y = -161;
        doorBacks[1].zIndex = 0;
        doorBacks[1].setFrame(1);
        GameManager.objects.add(doorBacks[1]);

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
            spears[i] = new Wall("spear", 9, 58, "/spearPole.png", 46, 0.01f, false);
            spears[i].setPosition(-284 + (i * 7), -146 - (i * 3));
            spears[i].zIndex = 1;
            spears[i].paddingTop = 40;
            spears[i].setOffsetCenterY(-30);
            spears[i + 1] = new Wall("spear", 9, 58, "/spearPole.png", 46, 0.01f, false);
            spears[i + 1].setPosition(283 - (i * 7), -146 - (i * 3));
            spears[i + 1].zIndex = 1;
            spears[i + 1].paddingTop = 40;
            spears[i + 1].setOffsetCenterY(-30);
            //spears[i + 1].setFrame(1);
        }
        for(int i = 0; i < spears.length; i++)
        {
            GameManager.objects.add(spears[i]);
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

        GameManager.objects.add(ostrichTrigger);
        ostrichTrigger.visible = false;
        ostrichTrigger.addComponent(new AABBComponent(ostrichTrigger, "trigger"));
        ostrichTrigger.setTag("Trigger");
        ostrichTrigger.paddingTop = -300;
        ostrichTrigger.position.x = sideWalls[1].position.x;

        nest = new Object("nest", 110, 109, "/nest.png", 1,  1f);
        nest.setPosition(0, -270);
        nest.zIndex = 199;
        GameManager.objects.add(nest);

        topStands[0] = new Object("topStand0", 15, 451, "/topStand.png", 2, 0.01f);
        topStands[0].setPosition(-332, -20);
        topStands[0].zIndex = 10;
        GameManager.objects.add(topStands[0]);

        topStands[1] = new Object("topStand1", 15, 451, "/topStand.png", 2, 0.01f);
        topStands[1].setPosition(332, -25);
        topStands[1].zIndex = 10;
        topStands[1].setFrame(1);
        GameManager.objects.add(topStands[1]);

        bottomStands[0] = new Object("bottomStand0", 29, 393, "/bottomStand.png", 2, 0.01f);
        bottomStands[0].setPosition(-325, -25);
        bottomStands[0].zIndex = 9;
        GameManager.objects.add(bottomStands[0]);

        bottomStands[1] = new Object("bottomStand1", 29, 393, "/bottomStand.png", 2, 0.01f);
        bottomStands[1].setPosition(325, -25);
        bottomStands[1].zIndex = 9;
        bottomStands[1].setFrame(1);
        GameManager.objects.add(bottomStands[1]);

        random = new Random();

        //debrisCount = random.nextInt(10);
        debrisCount = 20;

        for(int i = 0; i < debrisCount; i++)
        {
            int randomX = random.nextInt((int) (sideWalls[1].position.x * 2)) + (int) sideWalls[0].position.x;
            int randomY = random.nextInt(100) - 80;
            int randomFrame = random.nextInt(3) + 3;
            debris.add(i, new Object("debris" + i, 28, 26, "/skeletons.png", 6, 0));
            debris.get(i).setPosition(randomX, randomY);
            debris.get(i).setFrame(randomFrame);
            debris.get(i).zIndex = 1;
        }

        for(int i = 0; i < debris.size(); i++)
        {
            GameManager.objects.add(debris.get(i));
        }

        start();
    }

    @Override
    public void update(Main main, float dt)
    {
        if(starting)
        {
            /*if(counter.getFrame() == 3 && counter != null)
            {
                GameManager.removeObjectsByName("counter");
                counter = null;
            }*/
                //kingSwan.isActiveOnPause = true;
                tempStartOffsetTimer -= dt;
                if(tempStartOffsetTimer <= 0)
                {
                    if (startInd == 0)
                    {
                        GameManager.camera.setPath(kingSwan.position);
                        GameManager.camera.setMovingAlongVector(true);
                        //GameManager.removeObjectsByName("counter");
                        startInd++;
                    }
                    if (GameManager.camera.getPosY() <= kingSwan.position.y + 1 && GameManager.camera.getPosY() >= kingSwan.position.y - 1)
                    {
                        if(startInd == 1)
                        {
                            tempStartOffsetTimer = startOffsetTimer;
                            kingSwan.speak("/    B E G I N!", 0xff000000);
                            GameManager.camera.setMovingAlongVector(false);
                            startInd++;

                            for(int i = 0; i < players.size(); i++)
                            {
                                players.get(i).setFrame(2 + (i * 4));
                            }
                        }
                        if (tempStartOffsetTimer <= 0 && startInd == 2)
                        {
                            GameManager.camera.setPath(new Vector(0, 0));
                            GameManager.camera.setMovingAlongVector(true);
                            startInd++;
                        }
                    }

                    if (GameManager.camera.getPosY() <= 1 && GameManager.camera.getPosY() >= -1 && startInd == 3)
                    {
                        //starting = false;
                        counter.playTo(0, 4);
                        GameManager.camera.setMovingAlongVector(false);
                        tempStartOffsetTimer = startOffsetTimer;
                        startInd++;
                    }
                }
                if(startInd == 4 && counter.getFrame() >= 3)
                {
                    GameManager.removeObjectsByName("counter");
                    counter = null;
                    starting = false;
                }
        }
        else
            {
            kingSwan.animate(dt);
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


            if (GameManager.cameraPlayers.size() <= 1 && winner == null)
            {
                for (int i = 0; i < GameManager.cameraPlayers.size(); i++)
                {
                    if (GameManager.cameraPlayers.get(i) != null)
                    {
                        winner = GameManager.cameraPlayers.get(i);
                        break;
                    }
                }
            }
            if (GameManager.cameraPlayers.size() <= 1 && winner != null)
            {
                swanSpeak(winner, main);
            }
            if (players.size() > 0)
            {
                swanFollow();
            }

            tempHazardTimer -= dt;
            tempReticleBuffer -= dt;

            if (dropping)
            {
                tempDropTimer -= dt;

                if (tempDropTimer <= 0)
                {
                    kingSwan.position.x = dropPoints[reticleInd].x;
                    dropBomb(dropPoints[reticleInd]);
                    dropTimer /= 1.5f;
                    tempDropTimer = dropTimer;

                    if (reticleInd == 2)
                    {
                        kingSwan.position.x = 0;
                        dropping = false;
                    } else {
                        reticleInd++;
                    }
                }
            }

            if (tempHazardTimer <= 0) {
                hazard(hazardInd, dt);
                tempHazardTimer = hazardTimer;
            }

            if (flying) {
                if (kingSwan.position.y > -400) {
                    kingSwan.position.y -= 50f * dt;
                } else {
                    kingSwan.position.x = swanShadow.position.x;
                    swanShadow.position.y += 110f * dt;

                    for (int i = 0; i < dropPoints.length; i++) {
                        if (swanShadow.position.y <= dropPoints[i].y + 2 && swanShadow.position.y >= dropPoints[i].y - 2) {
                            if (tempReticleBuffer <= 0)
                            {
                                Object reticle = new Object("reticle", 113, 57, "/reticle.png", 2, 0.01f);
                                reticle.position.y = swanShadow.position.y;
                                reticle.position.x = swanShadow.position.x;
                                GameManager.objects.add(reticle);
                                reticle.zIndex = 0;
                                reticle.addComponent(new AABBComponent(reticle, "trigger"));
                                reticle.tag = "Trigger";
                                reticle.offsetPos = new Vector(reticle.position.x + 6400, reticle.position.y + 3600);
                                tempReticleBuffer = reticleBuffer;

                                if (!dropping)
                                {
                                    dropping = true;
                                }
                            }
                        }
                    }

                    if (swanShadow.position.y > 1200)
                    {
                        flying = false;
                        kingSwan.position.x = 0;
                        flyingBack = true;
                        dropTimer = 2f;
                        dropping = false;
                        reticleInd = 0;
                    }
                }
            } else if (flyingBack) {
                if (kingSwan.position.y < -301) {
                    kingSwan.position.y += 50f * dt;
                } else {
                    flyingBack = false;
                }
            }

        /*if(moving)
        {
            tempSpearBuffer -= dt;
            if(tempSpearBuffer <= 0 && !rised)
            {
                moving = false;
                moveSpears(false, true);
                tempSpearBuffer = spearBuffer;
            }
        }*/

            if (!readyShoot) {
                if (moveP) {
                    for (int i = 0; i < 2; i++) {
                        pelicanMove(true, new Vector(-184, -150), new Vector(184, -150), dt);
                    }
                }
            }
            if (readyShoot) {
                if (!compAdd) {
                    pelicans[0].addComponent(new AABBComponent(pelicans[0], "wall"));
                    pelicans[1].addComponent(new AABBComponent(pelicans[1], "wall"));
                    compAdd = true;
                }
                if (shotIndex < 6) {
                    pelicanTempCooldown -= dt;
                    if (pelicanTempCooldown <= 0) {
                        if (tempShotInd >= 3) {
                            tempShotInd = 1;
                        }

                        pelicanShoot(tempShotInd, dt);
                        shotIndex++;
                        tempShotInd++;
                        pelicanTempCooldown = pelicanCooldown;
                    }
                } else {
                    readyShoot = false;
                    leaveP = true;
                }
            }
            if (leaveP) {
                pelicans[0].removeComponentBySubtag("wall");
                pelicans[1].removeComponentBySubtag("wall");
                compAdd = false;
                moveSpears(false, true, movingL, risedL);
                moveSpears(false, false, movingR, risedR);
                pelicanMove(false, new Vector(-446, -150), new Vector(446, -150), dt);
                shotIndex = 0;
                pelicanTempCooldown = pelicanCooldown;
            }

            if (ostrichHazard) {
                tempOstrichTimerOffset -= dt;
                if (tempOstrichTimerOffset <= 0) {
                    ostrichRun(ostrichL, (WeaponComponent) ostrichGens[ostrichInt].findComponentBySubtag("ostrichLauncher"), dt);
                }
            }
        }

        if(tempBirdTimer <= 0)
        {
            int side = random.nextInt(2);
            boolean left = true;
            if(side == 1)
                left = false;

            int frame = random.nextInt(3);
            addBird(left, frame);

            tempBirdTimer = birdTimer;
        }

        updateBirds();
        tempBirdTimer -= dt;
    }

    public void start()
    {
        counter = new Object("counter", 128, 168, "/startNumbers.png", 5, 1f);
        starting = true;
        GameManager.objects.add(counter);
        counter.setFrame(4);
        //counter.isActiveOnPause = true;
        counter.zIndex = Integer.MAX_VALUE - 1;
        //GameManager.isPlaying = false;
    }

    public void addBird(boolean left, int frame)
    {
        Object bird = new Object("bird", 85, 91, "/birds.png", 6, 0.01f);
        int sideOffset = 0;
        bird.position.x = -500;
        if(!left)
        {
            sideOffset = 3;
            bird.position.x = 500;
        }

        bird.setFrame(frame + sideOffset);
        bird.position.y = random.nextInt(230) - 80;
        //bird.position.x = random.nextInt((int) (sideWalls[1].position.x * 2)) + (int) sideWalls[0].position.x;
        GameManager.objects.add(bird);
        birds.add(bird);
    }

    public void updateBirds()
    {
        for(int i = 0; i < birds.size(); i++)
        {
            if(birds.get(i).getFrame() < 3)
            {
                if(birds.get(i).position.x < topStands[0].position.x)
                {
                    birds.get(i).position.x += 5f;
                }
                else
                    {
                        birds.remove(i);
                    }
            }
            else
                {
                    if(birds.get(i).position.x > topStands[1].position.x)
                    {
                        birds.get(i).position.x -= 5f;
                    }
                    else
                    {
                        birds.remove(i);
                    }
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
        if(hazardInd == 2)
        {
            hazardInd = 0;
        }
        else
        {
            hazardInd += 1;
        }
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
                    dropPoints[i].x = randomX + (random.nextInt(20) - 10);
                    dropPoints[i].y = /*random.nextInt(150 * 2) - 150;*/ (100 * i) - 100 + (random.nextInt(40) - 20);
                }
            }
            break;
            case 1:
                moveSpears(true, true, movingL, risedL);
                moveSpears(true, false, movingR, risedR);
                moveP = true;
                break;
            case 2:
                random = new Random();
                ostrichInt = random.nextInt(1);
                boolean moving;
                boolean rised;
                if(ostrichInt == 0)
                {
                    ostrichL = true;
                    moving = movingL;
                    rised = risedL;
                }
                else
                    {
                        ostrichL = false;
                        moving = movingR;
                        rised = risedR;
                    }
                moveSpears(true, ostrichL, moving, rised);
                ostrichHazard = true;
                break;
        }
    }

    public void pelicanMove(boolean enter, Vector targetL, Vector targetR, float dt)
    {
        if(enter)
        {
            if (pelicans[0].position.x < targetL.x)
            {
                pelicans[0].position.x += 30 * dt;
                if (pelicans[0].position.y < targetL.y)
                {
                    pelicans[0].position.y += 30 * dt;
                }
            }
            else
            {
                readyShoot = true;
                moveP = false;
            }

            if (pelicans[1].position.x > targetR.x)
            {
                pelicans[1].position.x -= 30 * dt;

                if (pelicans[1].position.y < targetR.y)
                {
                    pelicans[1].position.y += 30 * dt;
                }
            }
            else
            {
                readyShoot = true;
                moveP = false;
            }
        }
        else
            {
                if (pelicans[0].position.x > targetL.x)
                {
                    pelicans[0].position.x -= 30 * dt;

                    if (pelicans[0].position.y > targetL.y)
                    {
                        pelicans[0].position.y -= 30 * dt;
                    }
                }
                else
                {
                    leaveP = false;
                    pelicanTempCooldown = pelicanCooldown;
                }

                if (pelicans[1].position.x < targetR.x)
                {
                    pelicans[1].position.x += 30 * dt;

                    if (pelicans[1].position.y > targetR.y)
                    {
                        pelicans[1].position.y -= 30 * dt;
                    }
                }
                else
                    {
                        leaveP = false;
                        pelicanTempCooldown = pelicanCooldown;
                    }
            }
    }
    public void pelicanShoot(int dir, float dt)
    {
        for(int i = 0; i < pelicans.length; i++)
        {
            if(i == 0)
            {
                pelicans[i].setFrame(dir);
                switch(dir)
                {
                    case 1:
                        pelicans[i + 1].setFrame(0);
                        break;
                    case 2:
                        pelicans[i + 1].setFrame(1);
                        break;
                }
            }
            int direction = pelicans[i].getFrame();

            switch(direction)
            {
                case 0:
                    direction = 7;
                    break;
                case 1:
                    direction = 0;
                    break;
                case 2:
                    direction = 1;
                    break;
            }

            pelicanWeapons[i].shoot("fishBomb",25, 31, "/fishBomb.png", 3, 0.01f, pelicans[i].getFrame(), direction);
        }
    }
    public void ostrichRun(boolean left, WeaponComponent weapon, float dt)
    {
        int direction;
        boolean moving;
        boolean rising;
        if(left)
        {
            direction = 1;
            moving = movingL;
            rising = risedL;
        }
        else
            {
                direction = 7;
                moving = movingR;
                rising = risedR;
            }
        if(ostrichYSpeed == 0)
        {
            ostrichYSpeed = random.nextInt(30) + 15;
        }
        weapon.shoot(90f, ostrichYSpeed);
        tempOstrichTimer -= dt;
        if(tempOstrichTimer <= 0)
        {
            tempOstrichTimer = ostrichTimer;
            ostrichHazard = false;
            moveSpears(false, left, moving, rising);
            tempOstrichTimerOffset = ostrichTimerOffset;
            ostrichYSpeed = 0;
        }
    }

    public void moveSpears(boolean lower, boolean left, boolean moving, boolean rised)
    {
        int[] spearSet = new int[spears.length / 2];

        if(left)
        {
            spearSet[0] = 0;
            spearSet[1] = 2;
            spearSet[2] = 4;
            spearSet[3] = 6;
        }
        else
            {
                spearSet[0] = 1;
                spearSet[1] = 3;
                spearSet[2] = 5;
                spearSet[3] = 7;
            }
        if(!moving)
        {
            for (int i = 0; i < spearSet.length; i++)
            {
                if(lower)
                    spears[spearSet[i]].playTo(0, 45);
                else
                    {
                        spears[spearSet[i]].playToInReverse(45, 1);
                        rised = true;
                    }
            }
            moving = true;
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
