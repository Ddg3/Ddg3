package com.zach.ddg3;

import edu.digipen.GameLevel;
import edu.digipen.GameObject;

/**
 * Created by Zach on 3/29/2018.
 */
public class testLevel extends GameLevel {
    @Override
    public void create() {

    }

    @Override
    public void initialize()
    {
        GameObject stageBack = new GameObject("stageBack", 1920, 1080, "cloud.png", 40, 10, 4, 0.1f);
        stageBack.animationData.play();
    }

    @Override
    public void update(float v) {

    }

    @Override
    public void uninitialize() {

    }
}
