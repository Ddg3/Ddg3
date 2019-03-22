package com.zach.ddg3;

import com.zach.engine.Renderer;
import com.zach.engine.gfx.Font;

public class TextObject
{
    public String text;
    public int posX;
    public int posY;
    public int color;
    public int scale;
    public boolean visible = true;
    public Font font = Renderer.StandardFont;
    public TextObject(String text, int posX, int posY, int color, int scale)
    {
        this.text = text;
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.scale = scale;
    }
}
