package com.zach.ddg3;

import com.zach.engine.Main;

import java.util.ArrayList;

/**

 Created by Zach on 6/12/2018.
 */
public abstract class GameLevel
{
    //FOR THE LOVE OF GOD REMEMBER UP IS NEGATIVE IN THIS STUPID REALITY AND DOWN IS POSITIVE
    public static ArrayList<Vector> verticleBounds = new ArrayList<Vector>(1);
    public static ArrayList<Vector> horizBounds = new ArrayList<Vector>(1);
    public static int loadPoint = Integer.MAX_VALUE;
    public Object fadeAway = new Object("fadeAway", 640, 360, "/fadeAway.png", 6, 0.2f);

    public abstract void init(Main main);

    public abstract void update(Main main, float dt);

    public abstract void uninit();

    public void fade()
    {
        fadeAway.zIndex = Integer.MAX_VALUE;
        GameManager.objects.add(fadeAway);
        fadeAway.setPosition(GameManager.center.getPositionX(), GameManager.center.getPositionY());
        fadeAway.playToAndDestroy(0, 5);
    }
}