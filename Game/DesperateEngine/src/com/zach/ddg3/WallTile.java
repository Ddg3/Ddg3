package com.zach.ddg3;

import com.zach.ddg3.components.AABBComponent;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

public class WallTile extends Wall
{
    private float zUpdatePointHigh;
    private float zUpdatePointLow;
    public WallTile()
    {
        super("Wall", 70, 100, "/wallTiles.png", 16, 0.0f, true);
        this.tag = "Wall";
        this.addComponent(new AABBComponent(this, "wall"));
        this.addComponent(new AABBComponent(this, "zUpdater"));
        this.paddingTop = 60;
        this.paddingSide = 12;
    }

    public enum directions
    {
        LEFTof,
        RIGHTof,
        DOWNof,
        UPof,
        BETWEEN_HORIZof,
        BETWEEN_VERTof,
        ULCORNERof,
        LLCORNERof,
        URCORNERof,
        LRCORNERof
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        super.update(main, gameManager, dt);
        this.updateComponents(main, gameManager, dt);
        this.animate(dt);
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
        zUpdate(other);
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
            if (otherC.getCenterY() + otherC.getHalfHeight() < zUpdatePointHigh) {
                //this.zIndex = target.zIndex;
                target.zIndex = this.zIndex - 1;
            }
            else if (otherC.getCenterY() - otherC.getHalfHeight() > zUpdatePointLow) {
                //this.zIndex = target.zIndex - 1;
                target.zIndex = target.maxzIndex;
            }
        }
    }

    public void setWallFrom(directions dir)
    {
        int direction = 0;
        float posX = this.getPositionX();
        float posY = this.getPositionY();
        int zInd = this.zIndex;

        switch (dir)
        {
            case LEFTof:
                direction = 1;
                posX -= this.width;
                break;
            case RIGHTof:
                direction = 3;
                posX += this.width;
                break;
            case UPof:
                direction = 4;
                posY -= this.height;
                zInd -= 1;
                break;
            case DOWNof:
                direction = 12;
                posY += this.height / 2;
                zInd += 1;
                break;

        }

        WallTile newWall = new WallTile();
        newWall.position = new Vector(posX, posY);
        newWall.setFrame(direction);
        newWall.zIndex = zInd;
        GameManager.objects.add(newWall);
    }
}
