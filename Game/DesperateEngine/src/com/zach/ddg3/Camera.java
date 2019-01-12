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

        topCamera = (int) gameManager.gameLevelManager.currLevel.verticleBounds.get(boundsRange).x;
        bottomCamera = (int) gameManager.gameLevelManager.currLevel.verticleBounds.get(boundsRange).y;

        main.getRenderer().setCameraX(0);

        //System.out.println(-target.position.y + " , " + (topCamera + (target.height / 2)) + " with bounds range at: " + boundsRange + " out of " + gameManager.gameLevelManager.currLevel.verticleBounds.size());
        //System.out.println(posY + " vs. playerpos at " + target.position.y);
        //System.out.println(posY + ", " + topCamera + ", " + bottomCamera);
        if (posY >= (topCamera + (target.height / 2)) && boundsRange != 0)
        {
            boundsRange--;
        }
        if (posY < (bottomCamera - (target.height / 2)) && boundsRange != gameManager.gameLevelManager.currLevel.verticleBounds.size() - 1  )
        {
            boundsRange++;
        }

        if (posY < topCamera && posY > bottomCamera)
        {
            main.getRenderer().setCameraY((int) posY);
        }
        else if (posY >= topCamera)
        {
            main.getRenderer().setCameraY(topCamera);
        } else
            {
            main.getRenderer().setCameraY(bottomCamera);
        }
    }
}