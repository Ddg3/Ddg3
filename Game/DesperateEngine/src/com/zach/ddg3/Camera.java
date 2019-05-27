package com.zach.ddg3;

import com.zach.engine.Main;
import org.omg.CORBA.INTERNAL;

public class Camera {
    private int topCamera;
    private int bottomCamera;
    private int leftCamera;
    private int rightCamera;

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

    public boolean isStopped() {
        return stopped;
    }

    private boolean stopped = false;
    private boolean topStopped = false;
    private boolean bottomStopped = false;
    private boolean leftStopped = false;
    private boolean rightStopped = false;

    public boolean isMovingAlongVector() {
        return movingAlongVector;
    }

    public void setMovingAlongVector(boolean movingAlongVector) {
        this.movingAlongVector = movingAlongVector;
    }

    private boolean movingAlongVector = false;

    public Vector getPath() {
        return path;
    }

    public void setPath(Vector path) {
        this.path = path;
    }

    private Vector path;
    private Vector toTarget;

    public Camera(String name) {
        this.targetName = name;
    }

    public void update(GameManager gameManager, Main main, float dt)
    {
        if (target == null) {
            //target = gameManager.getObject(targetName);
            target = GameManager.center;
        }

        /*if (target == null) {
            System.out.println("Object: " + targetName + " could not be found");
            return;
        }*/

        if(boundsRange == gameManager.gameLevelManager.currLevel.loadPoint)
        {
            resetCamera();
            GameManager.gameLevelManager.currLevel.loadPoint = Integer.MAX_VALUE;
            GameManager.gameLevelManager.setGameState(GameLevelManager.GameState.MAIN_STATE);
            GameManager.gameLevelManager.currLevel.uninit();

            if(GameManager.firstTime)
            {
                GameManager.firstTime = false;
            }
        }
        //Offsetting the camera by the center of the image (Bc origin is top left by default) and screen center
        posX = (target.position.x + (target.width / 2)) - main.getWidth() / 2;
        posY = (target.position.y + (target.height / 2)) - main.getHeight() / 2;

        topCamera = (int) gameManager.gameLevelManager.currLevel.verticleBounds.get(boundsRange).x;
        bottomCamera = (int) gameManager.gameLevelManager.currLevel.verticleBounds.get(boundsRange).y;

        if(!movingAlongVector)
        {
            if (gameManager.gameLevelManager.currLevel.horizBounds.size() != 0)
            {
                leftCamera = (int) gameManager.gameLevelManager.currLevel.horizBounds.get(0).x;
                rightCamera = (int) gameManager.gameLevelManager.currLevel.horizBounds.get(0).y;
            }

            //System.out.println(-target.position.y + " , " + (topCamera + (target.height / 2)) + " with bounds range at: " + boundsRange + " out of " + gameManager.gameLevelManager.currLevel.verticleBounds.size());
            //System.out.println(posY + " vs. playerpos at " + target.position.y);
            //System.out.println(posY + ", " + topCamera + ", " + bottomCamera);
            if(GameManager.gameLevelManager.getGameState() != GameLevelManager.GameState.MAIN_STATE)
            {
                if (posY >= (topCamera + (target.height / 2)) && boundsRange != 0) {
                    boundsRange--;
                }
                if (posY < (bottomCamera - (target.height / 2)) && boundsRange != gameManager.gameLevelManager.currLevel.verticleBounds.size() - 1) {
                    boundsRange++;
                }
            }

            if (posY < topCamera && posY > bottomCamera) {
                main.getRenderer().setCameraY((int) posY);
                //topStopped = false;
                //bottomStopped = false;
                stopped = false;
            } else if (posY >= topCamera) {
                main.getRenderer().setCameraY(topCamera);
                target.position.y = topCamera;
                //topStopped = true;
                stopped = true;
            } else {
                main.getRenderer().setCameraY(bottomCamera);
                target.position.y = bottomCamera;
                //bottomStopped = true;
                stopped = true;
            }

            if(GameManager.gameLevelManager.getGameState() != GameLevelManager.GameState.SELECTION_STATE)
            {
                if (posX < leftCamera && posX > rightCamera) {
                    main.getRenderer().setCameraX((int) posX);
                    //leftStopped = false;
                    //rightStopped = false;
                    stopped = false;
                } else if (posX >= leftCamera) {
                    main.getRenderer().setCameraX(leftCamera);
                    target.position.x = leftCamera;
                    //leftStopped = true;
                    stopped = true;

                } else if (posX < rightCamera) {
                    main.getRenderer().setCameraX(rightCamera);
                    target.position.x = rightCamera;
                    //rightStopped = true;
                    stopped = true;
                }
            }
            else
                {
                    main.getRenderer().setCameraX(0);
                    GameManager.center.position.x = 0;
                }
        }
        else
            {
                moveAlongVector(path, dt, main);
            }
    }

    public void resetCamera()
    {
        boundsRange = 0;
        leftCamera = 0;
        rightCamera = 0;
        topCamera = 0;
        bottomCamera = 0;
        posX = 0;
        posY = 0;
        GameManager.center.setPosition(0,0);
        movingAlongVector = false;
    }

    public void moveAlongVector(Vector vector, float dt, Main main)
    {
        if(toTarget == null)
        {
            toTarget = target.findVector(new Vector(posX, posY), vector);
        }
        this.posX += (int)toTarget.x / (8000 * dt);
        this.posY += (int)toTarget.y / (8000 * dt);

        main.getRenderer().setCameraY((int) posY);
        main.getRenderer().setCameraX((int) posX);

        target.position.y = posY;
        target.position.x = posX;
    }

    public boolean isTopStopped() {
        return topStopped;
    }

    public void setTopStopped(boolean topStopped) {
        this.topStopped = topStopped;
    }

    public boolean isBottomStopped() {
        return bottomStopped;
    }

    public void setBottomStopped(boolean bottomStopped) {
        this.bottomStopped = bottomStopped;
    }

    public boolean isLeftStopped() {
        return leftStopped;
    }

    public void setLeftStopped(boolean leftStopped) {
        this.leftStopped = leftStopped;
    }

    public boolean isRightStopped() {
        return rightStopped;
    }

    public void setRightStopped(boolean rightStopped) {
        this.rightStopped = rightStopped;
    }
}