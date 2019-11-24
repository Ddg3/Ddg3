package com.zach.ddg3;

public class Bird extends Object
{
    int side;
    public Bird(int side, String name, int width, int height, String path, int totalFrames, float frameLife)
    {
        super(name, width, height, path, totalFrames, frameLife);
        this.side = side;
    }
}
