package com.zach.ddg3;

import com.zach.ddg3.components.AABBComponent;
import com.zach.engine.Main;

import java.util.ArrayList;

public class Physics
{
    private static ArrayList<AABBComponent> aabbList = new ArrayList<AABBComponent>();

    public static void addAABBComponent(AABBComponent aabb)
    {
        aabbList.add(aabb);
    }

    public static void update(Main main)
    {
        for(int i = 0; i < aabbList.size(); i++)
        {
            for(int j = i + 1; j < aabbList.size(); j++)
            {
                AABBComponent c0 = aabbList.get(i);
                AABBComponent c1 = aabbList.get(j);
                if(Math.abs(c0.getCenterX() - c1.getCenterX()) < c0.getHalfWidth() + c1.getHalfWidth())
                {
                    if(Math.abs(c0.getCenterY() - c1.getCenterY()) < c0.getHalfHeight() + c1.getHalfHeight() && (c0.getNoCollideTag() != c1.getSubTag() && c1.getNoCollideTag() != c0.getSubTag()))
                    {
                        //Colliding on both axes
                        if(c0.getSubTag().equalsIgnoreCase("zUpdater"))
                        {
                            c0.getParent().collision(c1.getParent(), main);
                        }
                        else if(c1.getSubTag().equalsIgnoreCase("zUpdater"))
                        {
                            c1.getParent().collision(c0.getParent(), main);
                        }
                        else
                        {
                            c0.getParent().collision(c1.getParent(), main);
                            c1.getParent().collision(c0.getParent(), main);
                        }
                    }
                }
            }
        }
        aabbList.clear();
    }
}
