package com.zach.ddg3;

import edu.digipen.GameObject;
import edu.digipen.ObjectManager;

import static com.zach.ddg3.FightLevel.players;

/**
 * Created by Zach on 2/21/2018.
 */
public class Bullet extends GameObject
{
    public boolean isDuck;

    public String bulletTag;
    public boolean reflected;

    public int getAnimSet() {
        return animSet;
    }

    public void setAnimSet(int animSet) {
        this.animSet = animSet;
    }

    public int animSet;
    public String getBulletTag() {
        return bulletTag;
    }

    public void setBulletTag(String bulletTag) {
        this.bulletTag = bulletTag;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int width;
    public int height;
    public int totalFrames;
    public float speed;
    public String texture;

    public int getColliderSize() {
        return colliderSize;
    }

    public void setColliderSize(int colliderSize) {
        this.colliderSize = colliderSize;
    }

    public int colliderSize;
    public float rotation;

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int playerNumber;

    public boolean bouncing;

    public static GameObject laserSegment;


    public Bullet(float rot, float speedTemp, String name_, int width_, int height_, String textureName_, int totalFrames, int numberOfRows, int numberOfCols, float frameLifetime)
    {
        super(name_, width_, height_, textureName_, totalFrames, numberOfRows, numberOfCols, frameLifetime);
        rotation = rot;
        speed = speedTemp;
        texture = textureName_;
    }


    @Override
    public void update (float dt)
    {
        float x, y;

        y = speed * (float) Math.sin(Math.toRadians(rotation));

        x = speed * (float) Math.cos(Math.toRadians(rotation));
        setPositionY(getPositionY() + y);
        setPositionX(getPositionX() + x);

        if(this.getPositionX() >= 960 || this.getPositionX() <= -960 || this.getPositionY() >= 540 || this.getPositionY() <= -540)
        {
            this.kill();
        }
        if(this.getBulletTag() == "grenadeLauncher")
        {
            if(speed >= 0)
            {
                speed -= 0.04;
            }
        }
        if(this.getBulletTag() == "rocketLauncher")
        {
            if(speed >= 0)
            {
                speed += 0.02;
            }
        }
        if(this.getBulletTag() == "laserMatrix")
        {
            bounce();
            if(this.animationData.getCurrentFrame() == 0 && !bouncing)
            {
                laserSegment = new GameObject("laserSegment", 72,24,"laserMatrix_Bullet.png", 2, 1, 2, 0.1f);
                laserSegment.animationData.goToFrame(1);
                laserSegment.setPosition(this.getPosition());
                laserSegment.setRotation(this.getRotation());
                ObjectManager.addGameObject(laserSegment);
            }
        }
        if(this.getBulletTag() == "cannon")
        {
           bounce();
            speed += 0.01f;
        }
        if(speed > 0 && this.getBulletTag() == "grenadeLauncher")
        {
            this.animationData.play();
        }
        if(speed <= 0 && this.getBulletTag() == "grenadeLauncher")
        {
            this.animationData.stop();
        }
        if(this.getBulletTag() == "alienDuckHead")
        {
            if(animationData.getCurrentFrame() == 2 + getAnimSet())
            {
                this.animationData.goToAndPlay(animSet);
            }
        }

        if(this.isDead())
        {
            ObjectManager.removeAllObjectsByName("laserSegment");
        }
    }

    public void bounce()
    {
        if(this.getPositionX() < 890 && this.getPositionX() > -890 && this.getPositionY() < 500 && this.getPositionY() > -500)
        {
            bouncing = false;
        }
        if(this.getPositionX() >= 900)
        {
            rotation -= 90;
            this.setRotation(this.getRotation() - 90);
            bouncing = true;
        }
        if(this.getPositionX() <= -900)
        {
            rotation -= 90;
            this.setRotation(this.getRotation() - 90);
            bouncing = true;
        }
        if(this.getPositionY() >= 510)
        {
            rotation -= 90;
            this.setRotation(this.getRotation() - 90);
            bouncing = true;
        }
        if(this.getPositionY() <= -510)
        {
            rotation -= 90;
            this.setRotation(this.getRotation() - 90);
            bouncing = true;
        }
    }
    @Override
    public void collisionReaction(GameObject collidedWith)
    {
        if("explosion1".equals(collidedWith.getName()) || "explosion2".equals(collidedWith.getName()))
        {
            this.kill();
        }
        if("bullets1".equals(collidedWith.getName()) || "bullets2".equals(collidedWith.getName()))
        {
            if(this.getBulletTag() == "laserMatrix")
            {
                ObjectManager.removeAllObjectsByName("laserSegment");
            }
            this.kill();
        }
        if(players[1].reflected)
        {
            if("player1".equals(collidedWith.getName()))
            {
                rotation -= 180;
                reflected = true;
                players[1].bulletRef = true;
                this.speed += 2;
            }
        }
        if(players[2].reflected)
        {
            if("player2".equals(collidedWith.getName()))
            {
                rotation -= 180;
                reflected = true;
                players[2].bulletRef = true;
                this.speed += 2;
            }
        }
    }
}
