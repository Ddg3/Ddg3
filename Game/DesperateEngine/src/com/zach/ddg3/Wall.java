package com.zach.ddg3;

import com.zach.ddg3.components.AABBComponent;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

public class Wall extends Object
{
    private int color = (int)(Math.random() * Integer.MAX_VALUE);

    public Wall(String name, int width, int height, String path, int totalFrames, float frameLife)
    {
        super(name, width, height, path, totalFrames, frameLife);
        this.tag = name;

        this.addComponent(new AABBComponent(this));
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        super.update(main, gameManager, dt);
        this.updateComponents(main, gameManager, dt);
    }

    @Override
    public void render(Main main, Renderer r)
    {
        super.render(main, r);
        r.drawFillRectangle((int)offsetPos.x, (int)offsetPos.y, width, height, color);
        this.renderComponents(main, r);
    }

    @Override
    public void collision(GameObject other)
    {
        super.collision(other);
        color = (int)(Math.random() * Integer.MAX_VALUE);

        System.out.println("yes");
    }

}
