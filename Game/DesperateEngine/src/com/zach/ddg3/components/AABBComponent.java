package com.zach.ddg3.components;

import com.zach.ddg3.GameManager;
import com.zach.ddg3.Object;
import com.zach.ddg3.Physics;
import com.zach.ddg3.Player;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

import java.awt.event.KeyEvent;

public class AABBComponent extends Component
{
    private int color = (int)(Math.random() * Integer.MAX_VALUE);
    private Object parent;
    private int centerX, centerY;
    private int halfWidth, halfHeight;
    private int lastCenterX;
    private int lastCenterY;
    private int designatedPlayer = 0;

    public int getDesignatedPlayer() {
        return designatedPlayer;
    }

    public void setDesignatedPlayer(int designatedPlayer) {
        this.designatedPlayer = designatedPlayer;
    }
    public int getLastCenterX() {
        return lastCenterX;
    }

    public void setLastCenterX(int lastCenterX) {
        this.lastCenterX = lastCenterX;
    }

    public int getLastCenterY() {
        return lastCenterY;
    }

    public void setLastCenterY(int lastCenterY) {
        this.lastCenterY = lastCenterY;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getHalfWidth() {
        return halfWidth;
    }

    public void setHalfWidth(int halfWidth) {
        this.halfWidth = halfWidth;
    }

    public int getHalfHeight() {
        return halfHeight;
    }

    public void setHalfHeight(int halfHeight) {
        this.halfHeight = halfHeight;
    }

    public AABBComponent(Object parent, String subTag)
    {
        this.parent = parent;
        this.tag = "aabb";
        this.subTag = subTag;
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        lastCenterX = centerX;
        lastCenterY = centerY;

        if(this.subTag == "wall" || this.subTag == "selection" || this.subTag == "bullet" || this.subTag == "camera")
        {

            centerX = (int) (parent.getPositionX() + parent.getOffsetCenterX());
            centerY = (int) (parent.getPositionY() + parent.getOffsetCenterY());

            halfWidth = (parent.getWidth() / 2) - parent.getPaddingSide();
            halfHeight = (parent.getHeight() / 2) - (parent.getPaddingTop() / 2);
        }

        if(this.subTag == "player")
        {
            Player player = (Player) parent;

            if(player.isInGame())
            {
                centerX = (int) (parent.getPositionX() + parent.getOffsetCenterX() + player.getFrameHitboxOffsets().get(player.getFrame()).getX());
                centerY = (int) (parent.getPositionY() + parent.getOffsetCenterY() + player.getFrameHitboxOffsets().get(player.getFrame()).getY());
            }
            else
                {
                    centerX = (int) (parent.getPositionX() + parent.getOffsetCenterX());
                    centerY = (int) (parent.getPositionY() + parent.getOffsetCenterY());
                }

            halfWidth = (parent.getWidth() / 2) - parent.getPaddingSide();
            halfHeight = (parent.getHeight() / 2) - (parent.getPaddingTop() / 2);
        }
        if(this.subTag == "zUpdater")
        {
            centerX = (int) (parent.getPositionX());
            centerY = (int) (parent.getPositionY());

            halfWidth = (parent.getWidth() / 2);
            halfHeight = (parent.getHeight() / 2);
        }
        Physics.addAABBComponent(this);
    }

    @Override
    public void render(Main main, Renderer renderer)
    {
        if(this.getSubTag() == "wall" || this.subTag == "selection" || this.getSubTag() == "bullet" || this.getSubTag() == "player")
        {
            renderer.drawFillRectangle(centerX - halfWidth + 320, centerY - halfHeight + 180, halfWidth * 2, halfHeight * 2, color);
            renderer.setPixel(centerX + 320, centerY + 180, 0xffff0000);
        }
    }
}
