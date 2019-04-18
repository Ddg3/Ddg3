package com.zach.ddg3;

import com.zach.ddg3.components.Component;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

import java.util.ArrayList;

/**
 * Created by Zach on 6/9/2018.
 */
public abstract class GameObject
{
    protected String tag;
    protected Vector position = new Vector(0,0);
    protected int width;

    public int getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    protected int totalFrames;
    protected float frameLife;
    protected int paddingSide = 0;
    protected int paddingTop = 0;

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    protected int padding;
    protected float rotation;

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingSide() {
        return paddingSide;
    }

    public void setPaddingSide(int paddingSide) {
        this.paddingSide = paddingSide;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    protected int scale;

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    protected boolean dead = false;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

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

    protected int height;


    protected ArrayList<Component> components = new ArrayList<Component>();

    public abstract void update(Main main, GameManager gameManager, float dt);
    public abstract void render(Main main, Renderer r);
    public abstract void collision(Object other, Main main);

    public void updateComponents(Main main, GameManager gameManager, float dt)
    {
        for(Component component : components)
        {
            component.update(main, gameManager, dt);
        }
    }

    public void renderComponents(Main main, Renderer renderer)
    {
        for(Component component : components)
        {
            component.render(main, renderer);
        }
    }

    public void addComponent(Component component)
    {
        components.add(component);
    }

    public void removeComponent(String tag)
    {
        for(int i = 0; i < components.size(); i++)
        {
            if(components.get(i).getTag().equalsIgnoreCase(tag))
            {
                components.remove(i);
            }
        }
    }

    public void removeComponentBySubtag(String tag)
    {
        for(int i = 0; i < components.size(); i++)
        {
            if(components.get(i).getSubTag().equalsIgnoreCase(tag))
            {
                components.remove(i);
            }
        }
    }

    public Component findComponent(String tag)
    {
        for(int i = 0; i < components.size(); i++)
        {
            if(components.get(i).getTag().equalsIgnoreCase(tag))
            {
                return components.get(i);
            }
        }

        return null;
    }

    public Component findComponentBySubtag(String subTag)
    {
        for(int i = 0; i < components.size(); i++)
        {
            if(components.get(i).getSubTag().equalsIgnoreCase(subTag))
            {
                return components.get(i);
            }
        }
        return null;
    }
}
