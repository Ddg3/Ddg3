package com.zach.ddg3;

import com.zach.engine.Main;
import com.zach.engine.Renderer;
import com.zach.engine.gfx.ImageTile;

/**
 * Created by Zach on 6/9/2018.
 */
public class Object extends GameObject implements Comparable<Object>
{
    public int zIndex;
    private ImageTile objImage;
    public int anim = 0;
    public float opacity;
    public Vector offsetPos = new Vector(0,0);
    public boolean visible = true;
    public boolean target = false;
    private boolean isPlaying = false;
    private float frameLife;
    private float tempLife;
    private boolean isPlayingInRange = false;
    private boolean isPlayingInRangeAndBack = false;
    private boolean reversing = false;
    private int minRange = 0;
    private int maxRange = 0;
    private int endPoint = 0;
    private boolean inGame = true;

    public int getOffsetCenterX() {
        return offsetCenterX;
    }

    public void setOffsetCenterX(int offsetCenterX) {
        this.offsetCenterX = offsetCenterX;
    }

    public int getOffsetCenterY() {
        return offsetCenterY;
    }

    public void setOffsetCenterY(int offsetCenterY) {
        this.offsetCenterY = offsetCenterY;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    private int offsetCenterX = 0;
    private int offsetCenterY = 0;

    public static boolean isActiveOnPause = false;
    public static boolean isActiveOnPlay = true;

    public int getzIndex() {
        return zIndex;
    }

    public Object(String name, int width, int height, String path, int totalFrames, float frameLife)
    {
        objImage = new ImageTile(path, width, height);
        this.tag = name;
        this.width = width;
        this.height = height;
        this.position.setX(0);
        this.position.setY(0);
        this.totalFrames = totalFrames;
        this.frameLife = frameLife;
        this.scale = 1;
        this.opacity = 1f;
        this.rotation = 0;
        this.frameLife = frameLife;
        tempLife = frameLife;
        if(!target)
        {
            //The camera does not position the origin of non-targeted images in the center
            //To center the origin you offset the image by half height and half width plus half screen width and half screen height
            //For now the screen dimensions are hard coded as i refuse to pass in main in the constructor
            //If settings for screen dimensions are added this must be solved
            offsetPos.setX(position.x - width / 2 + 320);
            offsetPos.setY(position.y - height / 2 + 180);
        }
        else
        {
            //The camera centers ONE image and nothing else, so this image does not need to be offset
            offsetPos.setX(position.x);
            offsetPos.setY(position.y);
        }
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        if(!target)
        {
            offsetPos.setX(position.x - width / 2 + 320);
            offsetPos.setY(position.y - height / 2 + 180);
        }
        else
        {
            offsetPos.setX(position.x);
            offsetPos.setY(position.y);
        }

        animate(dt);
        updateComponents(main, gameManager, dt);
    }

    public void animate(float dt)
    {
        if(anim == totalFrames)
        {
            anim = 0;
            tempLife = frameLife;
        }
        if(anim < 0)
        {
            anim = totalFrames - 1;
            tempLife = frameLife;
        }
        if(isPlaying)
        {
            tempLife -= dt;
            if(tempLife <= 0 && anim < totalFrames)
            {
                anim++;
                tempLife = frameLife;
            }
        }
        if(isPlayingInRange)
        {
            if(endPoint == 0)
            {
                tempLife -= dt;
                if (tempLife <= 0 && anim < maxRange) {
                    anim++;
                    tempLife = frameLife;
                }
                if (anim == maxRange) {
                    anim = minRange;
                }
            }
            else
            {
                tempLife -= dt;
                if (tempLife <= 0 && anim < endPoint) {
                    anim++;
                    tempLife = frameLife;
                }
                if (anim == endPoint) {
                    stop();
                }
            }
        }
        if(isPlayingInRangeAndBack)
        {
            tempLife -= dt;
            if (anim == maxRange)
            {
                reversing = true;
            }
            if (anim == minRange)
            {
                reversing = false;
            }
            if (tempLife <= 0 && anim < maxRange && !reversing)
            {
                anim++;
                tempLife = frameLife;
            }
            if (tempLife <= 0 && anim > minRange && reversing)
            {
                anim--;
                tempLife = frameLife;
            }
        }
    }

    @Override
    public void render(Main main, Renderer r)
    {
        //r.drawFillRectangle((int)positionX, (int)positionY, width, height, 0xff00ff00);
        if(this.visible)
            r.drawImageTile(objImage, (int)offsetPos.x, (int)offsetPos.y, anim, 0, scale);

        //Shows the origin of the image (Its top left)
        //r.setPixel((int)offsetPos.x, (int)offsetPos.y, 0xff00FF00);
    }

    @Override
    public void collision(Object other)
    {

    }

    public void changeSprite(int width, int height, String path, int totalFrames, float frameLife)
    {
        objImage.setPath(path);
        objImage.setWidth(width);
        objImage.setHeight(height);
        this.width = width;
        this.height = height;
        this.totalFrames = totalFrames;
        this.frameLife = frameLife;
        this.frameLife = frameLife;
        tempLife = frameLife;
    }

    public void setPosition(float x, float y)
    {
        position.setX(x);
        position.setY(y);
    }
    //Animation functions
    public float getPositionX()
    {
        float offsetPos = position.getX();
        return offsetPos;
    }
    public float getPositionY()
    {
        float offsetPos = position.getY();
        return offsetPos;
    }
    public int getFrame()
    {
        return anim;
    }

    public int goToNextFrame()
    {
        return anim++;
    }

    public int goToPrevFrame()
    {
        return anim--;
    }

    public void play()
    {
        isPlaying = true;
        return;
    }

    public void stop()
    {
        isPlaying = false;
        isPlayingInRange = false;
        isPlayingInRangeAndBack = false;
        return;
    }

    public void setFrame(int newFrame)
    {
        anim = newFrame;
        return;
    }

    public void goToAndPlay(int newFrame)
    {
        setFrame(newFrame);
        isPlaying = true;
    }
    public void playInRange(int min, int max)
    {
        setFrame(min);
        minRange = min;
        maxRange = max;
        isPlayingInRange = true;
    }
    public void playTo(int start, int end)
    {
        setFrame(start);
        endPoint = end;
        isPlayingInRange = true;
    }

    public void playInRangeAndBack(int min, int max)
    {
        setFrame(min);
        minRange = min;
        maxRange = max;
        isPlayingInRangeAndBack = true;
    }

    public int compareTo(Object o) {
        return this.zIndex - o.getzIndex();
    }
}
