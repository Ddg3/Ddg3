package com.zach.ddg3;

public class Vector
{
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    float x , y;
    public Vector(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
}
