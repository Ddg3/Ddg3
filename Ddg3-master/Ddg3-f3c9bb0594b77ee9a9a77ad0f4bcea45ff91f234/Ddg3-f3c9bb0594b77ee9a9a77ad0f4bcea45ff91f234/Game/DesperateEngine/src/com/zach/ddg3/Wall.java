package com.zach.ddg3;

import com.zach.ddg3.components.AABBComponent;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

public class Wall extends Object
{
    private int color = (int)(Math.random() * Integer.MAX_VALUE);
    private boolean isZUpdater;
    private float zUpdatePointHigh;

    public float getzUpdatePointOffset() {
        return zUpdatePointOffset;
    }

    public void setzUpdatePointOffset(float zUpdatePointOffset) {
        this.zUpdatePointOffset = zUpdatePointOffset;
    }

    private float zUpdatePointOffset;

    public float getzUpdatePointHigh() {
        return zUpdatePointHigh;
    }

    public void setzUpdatePointHigh(float zUpdatePointHigh) {
        this.zUpdatePointHigh = zUpdatePointHigh;
    }

    public float getzUpdatePointLow() {
        return zUpdatePointLow;
    }

    public void setzUpdatePointLow(float zUpdatePointLow) {
        this.zUpdatePointLow = zUpdatePointLow;
    }

    private float zUpdatePointLow;

    public Wall(String name, int width, int height, String path, int totalFrames, float frameLife, boolean isZUpdater)
    {
        super(name, width, height, path, totalFrames, frameLife);
        this.tag = "Wall";
        this.isZUpdater = isZUpdater;

        this.addComponent(new AABBComponent(this, "wall"));

        if(this.isZUpdater)
        {
            this.addComponent(new AABBComponent(this, "zUpdater"));
        }
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
    public void collision(Object other, Main main)
    {
        super.collision(other, main);
        color = (int)(Math.random() * Integer.MAX_VALUE);

        if(this.isZUpdater)
        {
            zUpdate(other);
        }
    }

    public void zUpdate(Object target)
    {
        if (target.getTag().equalsIgnoreCase("Player") || target.getTag().equalsIgnoreCase("Bullet"))
        {
            AABBComponent myC = (AABBComponent) this.findComponentBySubtag("zUpdater");
            this.zUpdatePointHigh = myC.getCenterY() + myC.getHalfHeight();
            this.zUpdatePointLow = myC.getCenterY() - myC.getHalfHeight();
            AABBComponent otherC = (AABBComponent) target.findComponent("aabb");
            //AND NEEDS TO BE COLLIDING
            if (otherC.getCenterY() + otherC.getHalfHeight() < zUpdatePointHigh + zUpdatePointOffset) {
                this.zIndex = target.zIndex + 1;
            }
            else if (otherC.getCenterY() - otherC.getHalfHeight() > zUpdatePointLow) {
                this.zIndex = target.zIndex - 1;
            }
        }
    }
}
