package com.zach.ddg3;

public class Vector
{
    private double angle;
    private double length;
    float x , y;
    public Vector(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector normalize(Vector v)
    {
        double x = v.x / v.getLength();
        double y = v.y / v.getLength();

        Vector newV = new Vector((float) x, (float)y);
        newV.setLength(1);
        newV.setAngle(v.angle);
        return newV;
    }

    public void addVector(Vector v1)
    {
        /*this.x += v1.x;
        this.y += v1.y;
        this.length += v1.length;*/

        float newX = v1.x + this.x;
        float newY = v1.y + this.y;
        double angle = Math.tan(newY/newX);
        double magnitude = Math.sqrt((newX * newX) + (newY * newY));
        this.x = newX;
        this.y = newY;

        this.setLength(magnitude);
        this.setAngle(angle);
    }

    public void subtractVector(Vector v1)
    {
        float newX = this.x - v1.x;
        float newY = this.y - v1.y;
        double angle = Math.tan(newY/newX);
        double magnitude = Math.sqrt((newX * newX) + (newY * newY));
        this.x = newX;
        this.y = newY;

        this.setLength(magnitude);
        this.setAngle(angle);
    }

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

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}


