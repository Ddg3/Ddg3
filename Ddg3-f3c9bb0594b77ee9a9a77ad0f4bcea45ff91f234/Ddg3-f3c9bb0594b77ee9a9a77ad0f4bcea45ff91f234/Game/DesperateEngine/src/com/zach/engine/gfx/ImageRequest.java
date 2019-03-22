package com.zach.engine.gfx;

/**
 * Created by Zach on 5/16/2018.
 */
public class ImageRequest
{
    public Image image;
    public int zDepth;
    public int posX, posY;

    public ImageRequest(Image image, int zDepth, int posX, int posY)
    {
        this.image = image;
        this.zDepth = zDepth;
        this.posX = posX;
        this.posY = posY;

    }
}
