package com.zach.ddg3;

import com.ivan.xinput.XInputDevice;
import com.ivan.xinput.enums.XInputButton;
import com.zach.engine.Main;

import java.awt.event.KeyEvent;


public class titleLevel extends GameLevel
{
    private static Object title;
    private static TextObject enterText;
    private static Object owlGuard;
    private static Object owlGuard2;
    private static Object swan;
    private static Object nest;
    private static Object center;
    private static Player player1;
    private static float timer = 0.0f;
    private static float posX = 0;
    private static float blinkTimer = 1.0f;
    private static float loadTimer = 0.2f;

    public static Object getFadeAway() {
        return fadeAway;
    }

    public static void setFadeAway(Object fadeAway) {
        titleLevel.fadeAway = fadeAway;
    }

    private static Object fadeAway = null;

    @Override
    public void init(Main main)
    {
        fadeAway = new Object("fadeAway", 640, 360, "/fadeAway.png", 6, 0.1f);
        GameManager.objects.add(fadeAway);
        fadeAway.setPosition(0,0);
        //fadeAway.setFrame(0);
        fadeAway.zIndex = Integer.MAX_VALUE;
        //this.bottomCamera = 0;
        //this.topCamera = 0;

        this.verticleBounds.add(new Vector(0,0));
        this.horizBounds.add(new Vector(0,0));

        nest = new Object("nest", 640, 480, "/swanNest.png", 1, 0.1f);
        GameManager.objects.add(nest);
        nest.setPosition(0, -7);

        swan = new Object("swan", 92, 95, "/swanRaise.png", 12, 0.05f);
        GameManager.objects.add(swan);
        swan.setPosition(-10, -135);

        owlGuard2 = new Object("owlGuard2", 68, 74, "/owlGuardAnim.png", 5, 0.1f);
        //GameManager.objects.add(owlGuard2);
        owlGuard2.setPosition(100, -100);
        owlGuard2.playTo(0, 4);

        owlGuard = new Object("owlGuard", 68, 74, "/owlGuardAnim.png", 5, 0.1f);
        //GameManager.objects.add(owlGuard);
        owlGuard.setPosition(-100, -100);
        owlGuard.playTo(0, 4);

        title = new Object("title", 379, 140, "/title2.png", 1, 1f);
        GameManager.objects.add(title);
        title.setPosition(0, 0);

        enterText = new TextObject("Enter the Arena" , 280,325,0xffffffff, 1);
        GameManager.textObjects.add(enterText);

        /*player1 = new Player("player1", 57, 68, "/duckUnarmed.png", 8, 0.1f, false, 0);
        GameManager.objects.add(player1);
        player1.setPosition(0,0);*/
    }

    @Override
    public void update(Main main, float dt)
    {
        //GameManager.center.setPosition(player1.position.x, player1.position.y);

        /*swan.position.x += 1f;
        posX += 1f;
        timer += 1;
        System.out.println(posX + " at time: " + timer);
        System.out.println(swan.offsetPos.x + " = " + (swan.position.x - swan.width / 2 + 320));*/

        if(main.getInput().isKeyDown(KeyEvent.VK_SPACE))
        {
            swan.playTo(0, 10);
        }

        for(int i = 0; i < GameManager.deviceManager.devices.length; i++) {
            if (GameManager.deviceManager.devices[i].poll()) {
                if (GameManager.deviceManager.buttons[i].isPressed(XInputButton.START)) {
                    if (swan.anim == 0) {
                        swan.playTo(0, 10);
                    }
                }
            }
        }
        if(swan.anim > 0 && swan.anim < 3){
            for (int j = 0; j < GameManager.deviceManager.devices.length; j++) {
                GameManager.deviceManager.devices[j].setVibration(30000, 30000);
            }
        }
        if(swan.anim == 10)
        {
            for (int j = 0; j < GameManager.deviceManager.devices.length; j++) {
                GameManager.deviceManager.devices[j].setVibration(0, 0);
            }
            if(fadeAway.getFrame() == 0)
            {
                fadeAway.playToAndDestroy(0, 6);
                enterText.visible = false;
                blinkTimer = 100;
            }
            loadTimer -= dt;

            if(fadeAway.getFrame() == 5 && loadTimer <= 0)
            {
                GameManager.camera.boundsRange = 0;
                uninit();
                GameManager.gameLevelManager.setGameState(GameLevelManager.GameState.SELECTION_STATE);
            }
        }
        if(blinkTimer <= 0)
        {
            if(enterText.visible)
            {
                enterText.visible = false;
                blinkTimer = 1.0f;
                return;
            }

            else
                {
                    enterText.visible = true;
                    blinkTimer = 2.0f;
                    return;
                }
        }
        blinkTimer -= dt;
    }
    @Override
    public void uninit()
    {
        GameManager.objects.clear();
        GameManager.textObjects.clear();
        horizBounds.clear();
        verticleBounds.clear();
        GameManager.gameLevelManager.currLevel.loadPoint = Integer.MAX_VALUE;
    }
}
