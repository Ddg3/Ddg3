package com.zach.ddg3.components;

import com.zach.ddg3.GameManager;
import com.zach.ddg3.Object;
import com.zach.ddg3.Vector;
import com.zach.ddg3.Vulture;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

public class FollowerComponent extends Component
{
    private Object parent;
    private Object target;
    private float speed = 2000.0f;
    private Vulture vultureParent;

    public FollowerComponent(Object parent, Object target, String subTag)
    {
        this.parent = parent;
        this.tag = "follower";
        this.subTag = subTag;
        this.target = target;
        if(subTag == "vulture")
        {
            vultureParent = (Vulture)parent;
        }
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        if(subTag == "vulture" && vultureParent.isFollowing())
        {
            Vector toTarget = parent.findVector(parent.getPosition(), target.getPosition());
            parent.setPosition(parent.getPositionX() + (toTarget.getX() / (speed * dt)), parent.getPositionY() + (toTarget.getY() / (speed * dt)));
        }
        else if(subTag == "vulture" && vultureParent.isReturning())
        {
            Vector toTarget = parent.findVector(parent.getPosition(), vultureParent.getTargetPosition());
            parent.setPosition(parent.getPositionX() + (toTarget.getX() / (speed * dt)), parent.getPositionY() + (toTarget.getY() / (speed * dt)));
        }
    }

    @Override
    public void render(Main main, Renderer renderer)
    {

    }
}
