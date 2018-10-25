package com.zach.engine.gfx;

/**
 * Created by Zach on 5/6/2018.
 */
public class ImageTile extends Image
{
    public int getframeWidth() {
        return frameWidth;
    }

    public void setframeWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getframeHeight() {
        return frameHeight;
    }

    public void setframeHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    private int frameWidth;
    private int frameHeight;

    public ImageTile(String path, int frameWidth, int frameHeight)
    {
        super(path);
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }
    public Image getTileImage(int frameX, int frameY)
    {
        int[] pixel = new int[frameWidth * frameHeight];

        for(int y = 0; y < frameHeight; y++)
        {
            for(int x = 0; x < frameWidth; x++)
            {
                pixel[x + y * frameWidth] = this.getPixel()[(x + frameX * frameWidth) + (y + frameY * frameHeight) * this.getWidth()];
            }
        }
        return new Image(pixel, frameWidth, frameHeight);
    }
}
