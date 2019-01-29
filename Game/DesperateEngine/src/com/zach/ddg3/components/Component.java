package com.zach.ddg3.components;

import com.zach.ddg3.GameManager;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

public abstract class Component
{
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    protected String tag;

    public String getSubTag() {
        return subTag;
    }

    public void setSubTag(String subTag) {
        this.subTag = subTag;
    }

    protected String subTag;

    public abstract void update(Main main, GameManager gameManager, float dt);
    public abstract void render(Main main, Renderer renderer);

}
