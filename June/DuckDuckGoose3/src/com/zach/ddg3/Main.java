package com.zach.ddg3;

import edu.digipen.Game;
import edu.digipen.Graphics;


public class Main {

    public static void main(String[] args)
    {
        Game.initialize(1920, 1080, 60, new newCharChoose());
        Graphics.setBackgroundColor(1, 1, 1);
        while(!Game.getQuit())
        {
            Game.update();
        }
        Game.destroy();
    }
}
