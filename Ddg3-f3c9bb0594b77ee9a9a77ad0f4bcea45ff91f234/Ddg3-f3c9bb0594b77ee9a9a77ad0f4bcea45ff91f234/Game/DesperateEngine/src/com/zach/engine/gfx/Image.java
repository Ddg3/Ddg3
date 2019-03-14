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

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        width = image.getWidth();
        height = image.getHeight();
        pixel = image.getRGB(0, 0, width ,height, null, 0, width);

        image.flush();
    }

    public Image(int[] pixel ,int width, int height)
    {
        this.width = width;
        this.height = height;
        this.pixel = pixel;
    }
}
