package com.zach.ddg3;

import com.zach.ddg3.components.AABBComponent;
import com.zach.ddg3.components.FollowerComponent;
import com.zach.engine.Main;
import com.zach.engine.Renderer;
import org.omg.CORBA.INTERNAL;

import java.util.ArrayList;

public class Vulture extends Object
{
    private Player target;

    public boolean isReturning() {
        return isReturning;
    }

    public void setReturning(boolean returning) {
        isReturning = returning;
    }

    private boolean isReturning = false;

    public ArrayList<Vector> getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(ArrayList<Vector> targetPosition) {
        this.targetPosition = targetPosition;
    }

    public ArrayList<Vector> targetPosition = new ArrayList<>(1);

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    private boolean isFollowing = false;

    private Vector toTarget = null;

    private int targetIndex;

    public Vulture(Player target, int targetNumber)
    {
        super("vulture" + targetNumber, 144, 90, "/vultureSheet.png", 8, 0.05f);
        this.target = target;

        //this.addComponent(new FollowerComponent(this, target, "vulture"));
        this.addComponent(new AABBComponent(this, "zUpdater"));

        this.zIndex = Integer.MAX_VALUE;
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        follow(dt);
        if(target.getTime() <= 57 && !isReturning && targetIndex == 0)
        {
            isReturning = true;
            toTarget = this.findVector(getPosition(), targetPosition.get(targetIndex));
            playInRangeAndBack(1, 7);
        }
        if(target.isTimedOut() && !isReturning && !target.isRemoved() && !isFollowing)
        {
            toTarget = this.findVector(this.getPosition(), target.getPosition());
            isFollowing = true;
            playInRangeAndBack(1, 7);
        }
        if(this.getPosition().x <= targetPosition.get(targetIndex).getX() + 1 && this.getPosition().x >= targetPosition.get(targetIndex).getX() - 1 &&
                this.getPosition().y <= targetPosition.get(targetIndex).getY() + 1 && this.getPosition().y >= targetPosition.get(targetIndex).getY() - 1 && isReturning)
        {
            isReturning = false;
            if(targetIndex == 1)
            {
                target.setRemoved(true);
                target.setGrabbed(false);
            }
            stop();
            setFrame(0);
            if(targetIndex < targetPosition.size() - 1)
            {
                targetIndex++;
            }
        }

        this.offsetPos.x = (int)(this.position.x - (this.width / 2) + 320);
        this.offsetPos.y = (int)(this.position.y - (this.height / 2) + 180);

        animate(dt);
        updateComponents(main, gameManager, dt);

        if(target.isGrabbed())
        {
            target.setPosition(this.position.x, this.position.y + 50);
        }
    }

    public void follow(float dt)
    {
        if(isFollowing || isReturning)
        {
            setPosition(getPositionX() + (toTarget.getX() / (4000 * dt)), getPositionY() + (toTarget.getY() / (4000 * dt)));
        }
    }
    @Override
    public void collision(Object other, Main main)
    {
        if(other.getTag().equalsIgnoreCase("Player"))
        {
            isFollowing = false;
            isReturning = true;
            target.setGrabbed(true);
            toTarget = this.findVector(getPosition(), targetPosition.get(targetIndex));
        }
    }

    @Override
    public void render(Main main, Renderer r)
    {
        super.render(main, r);
    }
}
