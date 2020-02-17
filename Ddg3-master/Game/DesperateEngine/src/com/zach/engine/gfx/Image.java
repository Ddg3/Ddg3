package com.zach.engine.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Zach on 4/14/2018.
 */
public class Image
{
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

    public int[] getPixel() {
        return pixel;
    }

    public void setPixel(int[] pixel) {
        this.pixel = pixel;
    }

    private int width;
    private int height;
    private int[] pixel;

    public boolean isColorChanged() {
        return colorChanged;
    }

    public void setColorChanged(boolean colorChanged) {
        this.colorChanged = colorChanged;
    }

    private boolean colorChanged;

    public boolean isAlpha() {
        return alpha;
    }

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }

    private boolean alpha = false;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    public Image(String path)
    {
        this.path = path;
        BufferedImage image = null;

        try
        {
            image = ImageIO.read(Image.class.getResourceAsStream(this.path));
            width = image.getWidth();
            height = image.getHeight();
            pixel = image.getRGB(0, 0, width ,height, null, 0, width);

            image.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Image(int[] pixel ,int width, int height)
    {
        this.width = width;
        this.height = height;
        this.pixel = pixel;
    }

    public void changeColor(int oldColor, int newColor)
    {
        int tempX = 0;
        int tempY = 0;
        int tempWidth = this.getWidth();
        int tempHeight = this.getHeight();

        for(int y = tempY; y < tempHeight; y++)
        {
            for(int x = tempX; x < tempWidth; x++)
            {
                if(this.getPixel()[x + y * this.getWidth()] == oldColor)
                {
                    this.getPixel()[x + y * this.getWidth()] = newColor;
                }
            }
        }
    }
}
