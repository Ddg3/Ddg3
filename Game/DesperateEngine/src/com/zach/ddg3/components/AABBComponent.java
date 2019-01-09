package com.zach.ddg3.components;

import com.zach.ddg3.GameManager;
import com.zach.ddg3.Object;
import com.zach.engine.Main;
import com.zach.engine.Renderman;

public class AABBComponent extends Component
{
    private int centerX, centerY;
    private int halfWidth, halfHeight;

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

    @Override
    public void update(Main main, GameManager gameManager, Object parent, float dt)
    {
        centerX = (int)(parent.getPositionX() + (parent.getWidth() / 2));
        centerY = (int)(parent.getPositionY() + (parent.getHeight() / 2) + (parent.getPaddingTop() / 2));

        halfWidth = parent.getWidth() - parent.getPaddingBottom();
        halfHeight = parent.getHeight() - parent.getPaddingTop();
    }

    @Override
    public void render(Main main, GameManager gameManager, Object parent, Renderman renderer)
    {

    }
}
