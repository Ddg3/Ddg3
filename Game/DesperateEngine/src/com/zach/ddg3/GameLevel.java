package com.zach.ddg3;

import com.zach.engine.Main;

import java.util.ArrayList;

/**

 Created by Zach on 6/12/2018.
 */
public abstract class GameLevel
{
    public static int bottomCamera;
    public static int topCamera;
    //public static ArrayList eventPoints = new ArrayList(1);
    public static ArrayList<Vector> verticleBounds = new ArrayList<Vector>(1);
    public static ArrayList<Vector> horizBounds = new ArrayList<Vector>(1);

    public abstract void init(Main main);

    public abstract void update(Main main, float dt);

    public abstract void uninit();
}