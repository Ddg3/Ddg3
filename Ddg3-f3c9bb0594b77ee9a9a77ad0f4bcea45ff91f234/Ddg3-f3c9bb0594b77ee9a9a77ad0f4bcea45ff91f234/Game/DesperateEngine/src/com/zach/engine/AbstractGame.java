package com.zach.engine;

/**
 * Created by Zach on 4/14/2018.
 */
public abstract class AbstractGame
{
    public abstract void init(Main main);
    public abstract void update(Main main, float dt);
    public abstract void render(Main main, Renderer renderer);
}
