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
        this.tag = "Wall";

        this.addComponent(new AABBComponent(this, "wall"));
        this.addComponent(new AABBComponent(this, "zUpdater"));
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        super.update(main, gameManager, dt);
        this.updateComponents(main, gameManager, dt);

        /*if(gameManager.players.get(0) != null)
        {
            zUpdate(gameManager.players.get(0));
        }*/
    }

    @Override
    public void render(Main main, Renderer r)
    {
        super.render(main, r);
        //r.drawFillRectangle((int)offsetPos.x, (int)offsetPos.y, width, height, color);
        //this.renderComponents(main, r);
    }

    @Override
    public void collision(Object other)
    {
        super.collision(other);
        color = (int)(Math.random() * Integer.MAX_VALUE);
        zUpdate(other);
    }

    public void zUpdate(Object target)
    {
        if (!target.getTag().equalsIgnoreCase("Wall"))
        {
            AABBComponent myC = (AABBComponent) this.findComponentBySubtag("zUpdater");
            AABBComponent otherC = (AABBComponent) target.findComponent("aabb");
            //AND NEEDS TO BE COLLIDING
            if (otherC.getCenterY() + otherC.getHalfHeight() < myC.getCenterY() + myC.getHalfHeight()) {
                this.zIndex = target.zIndex + 1;
            }
            if (otherC.getCenterY() - otherC.getHalfHeight() > myC.getCenterY() - myC.getHalfHeight()) {
                this.zIndex = target.zIndex - 1;
            }
        }
    }
}
