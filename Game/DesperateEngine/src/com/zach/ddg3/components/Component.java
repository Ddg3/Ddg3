package com.zach.ddg3.components;

import com.zach.ddg3.GameManager;
import com.zach.ddg3.Object;
import com.zach.engine.Main;
import com.zach.engine.Renderman;

public abstract class Component
{
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    protected String tag;

    public abstract void update(Main main, GameManager gameManager, Object parent, float dt);
    public abstract void render(Main main, GameManager gameManager, Object parent, Renderman renderer);

}
