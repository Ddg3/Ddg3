package com.zach.ddg3;

import edu.digipen.GameObject;

/**
 * Created by Zach on 3/16/2018.
 */
public class Dasher extends GameObject
{
    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public float rotation;
    public String texture;

    public Dasher(float rot, String name_, int width_, int height_, String textureName_, int totalFrames, int numberOfRows, int numberOfCols, float frameLifetime)
    {
        super(name_, width_, height_, textureName_, totalFrames, numberOfRows, numberOfCols, frameLifetime);
        rotation = rot;
        texture = textureName_;
    }
    @Override
    public void update (float dt)
    {
            float x, y;

            y = 12 * (float) Math.sin(Math.toRadians(rotation));

            x = 12 * (float) Math.cos(Math.toRadians(rotation));
            setPositionY(getPositionY() + y);
            setPositionX(getPositionX() + x);
    }
}
