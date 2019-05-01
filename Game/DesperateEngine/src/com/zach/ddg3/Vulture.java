package com.zach.ddg3;

import com.zach.ddg3.components.AABBComponent;
import com.zach.ddg3.components.FollowerComponent;
import com.zach.engine.Main;
import com.zach.engine.Renderer;
import org.omg.CORBA.INTERNAL;

public class Vulture extends Object
{
    private Player target;

    public boolean isReturning() {
        return isReturning;
    }

    public void setReturning(boolean returning) {
        isReturning = returning;
    }

    private boolean isReturning;

    public Vector getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Vector targetPosition) {
        this.targetPosition = targetPosition;
    }

    private Vector targetPosition;

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    private boolean isFollowing = false;

    public Vulture(Player target, int targetNumber)
    {
        super("vulture" + targetNumber, 53, 90, "/vulture.png", 1, 1);
        this.target = target;

        this.addComponent(new FollowerComponent(this, target, "vulture"));
        this.addComponent(new AABBComponent(this, "camera"));

        this.zIndex = Integer.MAX_VALUE;
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        if(target.getTime() <= 57 && !isReturning && !target.isRemoved())
        {
            isFollowing = true;
        }
        if(this.getPosition() == this.targetPosition && isReturning)
        {
            isReturning = false;
            target.setRemoved(true);
        }

        this.offsetPos.x = (int)(this.position.x - (this.width / 2) + 320);
        this.offsetPos.y = (int)(this.position.y - (this.height / 2) + 180);

        animate(dt);
        updateComponents(main, gameManager, dt);
    }

    @Override
    public void collision(Object other, Main main)
    {
        if(other.getTag().equalsIgnoreCase("Player"))
        {
            isFollowing = false;
            isReturning = true;
        }
    }

    @Override
    public void render(Main main, Renderer r)
    {
        super.render(main, r);
    }
}
