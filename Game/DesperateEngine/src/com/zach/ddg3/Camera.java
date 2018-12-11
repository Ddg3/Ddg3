package com.zach.ddg3;

import com.zach.engine.Main;

public class Camera {
    private int topCamera;
    private int bottomCamera;

    public void setBoundsRange(int boundsRange) {
        this.boundsRange = boundsRange;
    }

    public int boundsRange = 0;

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    private float posX, posY;

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    private String targetName;
    private Object target;

    public Camera(String name) {
        this.targetName = name;
    }

    public void update(GameManager gameManager, Main main, float dt) {
        if (target == null) {
            target = gameManager.getObject(targetName);
        }

        if (target == null) {
            System.out.println("Object: " + targetName + " could not be found");
            return;
        }

        //Offsetting the camera by the center of the image (Bc origin is top left by default) and screen center
        posX = (target.position.x + (target.width / 2)) - main.getWidth() / 2;
        posY = (target.position.y + (target.height / 2)) - main.getHeight() / 2;

        topCamera = (int) gameManager.gameLevelManager.currLevel.verticleBounds.get(0).x;
        bottomCamera = (int) gameManager.gameLevelManager.currLevel.verticleBounds.get(0).y;

        main.getRenderman().setCameraX(0);

        if (target.getPositionY() > topCamera && boundsRange != gameManager.gameLevelManager.currLevel.verticleBounds.size())
        {
            boundsRange++;
        }
        if (target.getPositionY() < bottomCamera && boundsRange != 0)
        {
            boundsRange--;
        }

        if (posY < topCamera && posY > bottomCamera)
        {
            main.getRenderman().setCameraY((int) posY);

        }
        else if (posY >= topCamera)
        {
            main.getRenderman().setCameraY(topCamera);
        } else
            {
            main.getRenderman().setCameraY(bottomCamera);
        }
    }
}