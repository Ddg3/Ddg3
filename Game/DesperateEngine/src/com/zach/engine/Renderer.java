package com.zach.engine;

import com.zach.engine.gfx.*;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Zach on 4/14/2018.
 */
public class Renderer
{
    private int pWidth;
    private int pHeight;
    private int[] pixels;
    private int[] zBuffer;
    private int zOrder = 0;
    private boolean processing = false;
    private int[] lightMap;
    private int[] lightBlock;

    public int getCameraX() {
        return cameraX;
    }

    public void setCameraX(int cameraX) {
        this.cameraX = cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public void setCameraY(int cameraY) {
        this.cameraY = cameraY;
    }

    private int cameraX, cameraY;

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    protected float alpha;

    public int getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(int ambientColor) {
        this.ambientColor = ambientColor;
    }

    private int ambientColor = 0xff6b6b6b;
    public static Font StandardFont = Font.STANDARD;
    public static Font BlockNumberFont = Font.BLOCKNUMBER;
    private ArrayList<ImageRequest> imageRequest = new ArrayList<ImageRequest>();

    public int getzOrder() {
        return zOrder;
    }

    public void setzOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    public Renderer(Main main)
    {
        pWidth = main.getWidth();
        pHeight = main.getHeight();
        pixels = ((DataBufferInt)main.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zBuffer = new int[pixels.length];
        lightMap = new int[pixels.length];
        lightBlock = new int[pixels.length];
    }

    public void clear()
    {
        for(int i = 0; i < pixels.length; i++)
        {
            pixels[i] = 0;
            zBuffer[i] = 0;
            lightMap[i] = ambientColor;
            lightBlock[i] = 0;
        }
    }

    public void process()
    {
        processing = true;

        Collections.sort(imageRequest, new Comparator<ImageRequest>()
        {
            @Override
            public int compare(ImageRequest o1, ImageRequest o2)
            {
                if(o1.zDepth < o2.zDepth)
                {
                    return -1;
                }
                if(o1.zDepth > o2.zDepth)
                {
                    return 1;
                }
                return 0;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });

        for(int  i = 0 ; i < imageRequest.size(); i++)
        {
            ImageRequest ir = imageRequest.get(i);
            setzOrder(ir.zDepth);
            drawImage(ir.image, ir.posX, ir.posY, 1);
        }
        for(int i = 0; i < pixels.length; i++)
        {
            float r = ((lightMap[i] >> 16) & 0xff) / 255f;
            float g = ((lightMap[i] >> 8) & 0xff) / 255f;
            float b = (lightMap[i] & 0xff) / 255f;

            pixels[i] = (int)(((pixels[i] >> 16) & 0xff) * r) << 16 | (int)(((pixels[i] >> 8) & 0xff) * g) << 8 | (int)(((pixels[i] & 0xff) * b));
        }
        imageRequest.clear();
        processing = false;
    }

    public void setPixel(int x, int y, int value)
    {
        alpha = ((value >> 24) & 0xff);

        x -= cameraX;
        y -= cameraY;

        if((x < 0 || x >= pWidth || y < 0 || y >= pHeight) || alpha == 0)
        {
            return;
        }

        int index = x + y * pWidth;

        if(zBuffer[index] > zOrder)
        {
            return;
        }

        zBuffer[index] = zOrder;
        if(alpha == 255)
        {
            pixels[index] = value;
        }
        else
            {
                int pixelColor = pixels[index];

                int newRed = ((pixelColor >> 16) & 0xff) - (int)((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
                int newGreen = ((pixelColor >> 8) & 0xff) - (int)((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
                int newBlue = (pixelColor & 0xff) - (int)(((pixelColor & 0xff) - (value & 0xff)) * (alpha / 255f));

                pixels[index] = (newRed << 16 | newGreen << 8 | newBlue);
            }
    }

    public void setLightMap(int x, int y, int value)
    {
        x -= cameraX;
        y -= cameraY;
        if(x < 0 || x >= pWidth || y < 0 || y >= pHeight)
        {
            return;
        }

        int baseColor = lightMap[x + y * pWidth];

        int maxRed = Math.max((baseColor >> 16) & 0xff, (value >> 16) & 0xff);
        int maxGreen = Math.max((baseColor >> 9) & 0xff, (value >> 8) & 0xff);
        int maxBlue = Math.max(baseColor & 0xff, value & 0xff);

        lightMap[x + y * pWidth] = (maxRed << 16 | maxGreen << 8 | maxBlue);
    }
    public void drawText(String text, int offsetX, int offsetY, int color, int scale, Font font)
    {
        int offset = 0;

        if(scale == 1)
        {
            for (int i = 0; i < text.length(); i++)
            {
                int unicode = text.codePointAt(i);

                for (int y = 0; y < font.getFontImage().getHeight(); y++) {
                    for (int x = 0; x < font.getWidths()[unicode]; x++) {
                        if (font.getFontImage().getPixel()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getWidth()] == 0xffffffff)
                        {
                            setPixel(x + offsetX + offset, y + offsetY, color);
                        }
                    }
                }
                offset += font.getWidths()[unicode];
            }
        }
    }
    public void drawImage(Image image, int offsetX, int offsetY, int scale)
    {
        if(image.isAlpha() && !processing)
        {
            imageRequest.add(new ImageRequest(image, zOrder, offsetX, offsetY));
            return;
        }
        //Do not render

        int tempX = 0;
        int tempY = 0;
        int tempWidth = image.getWidth() * scale;
        int tempHeight = image.getHeight() * scale;

        //Clipping for optimization

        for(int y = tempY; y < tempHeight; y++)
        {
            for(int x = tempX; x < tempWidth; x++)
            {
                setPixel(x + offsetX,y + offsetY, image.getPixel()[x + y * image.getWidth()]);
            }
        }
    }

    public void drawImageTile(ImageTile image, int offsetX, int offsetY, int frameX, int frameY, int scale)
    {
        if(image.isAlpha() && !processing)
        {
            imageRequest.add(new ImageRequest(image.getTileImage(frameX, frameY), zOrder, offsetX, offsetY));
            return;
        }

        int tempX = 0;
        int tempY = 0;
        int tempWidth = image.getframeWidth() * scale;
        int tempHeight = image.getframeHeight() * scale;

        //Clipping for optimization

        for(int y = tempY; y < tempHeight; y++)
        {
            for(int x = tempX; x < tempWidth; x++)
            {
                setPixel(x + offsetX,y + offsetY, image.getPixel()[(x + frameX * image.getframeWidth()) + (y + frameY * image.getframeHeight()) * image.getWidth()]);
            }
        }
    }

    public void drawRectangle(int posX, int posY, int width, int height, int color)
    {
        for(int y = 0; y <= height; y++)
        {
            setPixel(posY, y + posY, color);
            setPixel(posX + width, y  + posY, color);
        }
        for(int x = 0; x <= width; x++)
        {
            setPixel(x + posX, posX, color);
            setPixel(x + posX, posY + height, color);
        }
    }

    public void drawFillRectangle(int posX, int posY, int width, int height, int color)
    {

        int tempX = 0;
        int tempY = 0;
        int tempWidth = width;
        int tempHeight = height;

        //Clipping for optimization
        /*if(tempX  + posX < 0)
        {
            tempX -= posX;
        }
        if(tempY  + posY < 0)
        {
            tempY -= posY;
        }
        if(tempWidth + posX > pWidth)
        {
            tempWidth = tempWidth - (tempWidth + posX - pWidth);
        }
        if(tempHeight + posY > pHeight)
        {
            tempHeight = tempHeight - (tempHeight + posY - pHeight);
        }*/
        for(int y = tempY; y <= tempHeight; y++)
        {
            for(int x = tempX; x <= tempWidth; x++)
            {
                setPixel(posX + x, y + posY, color);
            }
        }
    }

    /*public Font getStandardFont() {
        return StandardFont;
    }

    public void setStandardFont(Font standardFont) {
        StandardFont = standardFont;
    }

    public Font getBlockNumberFont() {
        return BlockNumberFont;
    }

    public void setBlockNumberFont(Font blockNumberFont) {
        BlockNumberFont = blockNumberFont;
    }*/

}

