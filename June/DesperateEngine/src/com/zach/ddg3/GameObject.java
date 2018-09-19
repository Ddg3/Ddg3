package com.zach.ddg3;

import com.zach.engine.Main;
import com.zach.engine.Renderman;

/**
 * Created by Zach on 6/9/2018.
 */
public abstract class GameObject
{
    protected String tag;
    protected Vector position = new Vector(0,0);
    protected int width;
    protected int totalFrames;
    protected float frameLife;

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    protected float rotation;

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    protected int scale;

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    protected boolean dead = false;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected int height;

    public abstract void update(Main main, GameManager gameManager, float dt);
    public abstract void render(Main main, Renderman r);
}
