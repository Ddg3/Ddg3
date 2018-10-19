package com.zach.ddg3;

public class TextObject
{
    public String text;
    public int posX;
    public int posY;
    public int color;
    public int scale;
    public boolean visible = true;
    public TextObject(String text, int posX, int posY, int color, int scale)
    {
        this.text = text;
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.scale = scale;
    }
}
