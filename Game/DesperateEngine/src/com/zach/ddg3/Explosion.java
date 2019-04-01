package com.zach.ddg3;

import com.zach.ddg3.components.AABBComponent;
import com.zach.ddg3.components.WeaponComponent;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

public class Explosion extends Object
{
    WeaponComponent weapon;
    Object owner;
    public Explosion(String name, int width, int height, String path, int totalFrames, float frameLife, WeaponComponent weapon)
    {
        super(name, width, height, path, totalFrames, frameLife);

        this.playToAndDestroy(0, totalFrames);
        this.tag = "Explosion";
        this.addComponent(new AABBComponent(this, "bullet"));

        this.weapon = weapon;
        this.owner = weapon.getParent();
        this.zIndex = owner.zIndex + 1;
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        this.animate(dt);
        this.offsetPos.x = (int)(this.position.x - (this.width / 2) + 320);
        this.offsetPos.y = (int)(this.position.y - (this.height / 2) + 180);
        this.updateComponents(main, gameManager, dt);
    }

    @Override
    public void collision(Object other, Main main)
    {
        if(other.getTag().equalsIgnoreCase("Player"))
        {
            Player player = (Player)other;
            Player ownerP = (Player)owner;
            if(player.getPlayerNumber() != weapon.getPlayerNumber())
            {
                if(player.isGoose())
                    player.depleteTime(weapon.getDamage());
                else if(ownerP.isGoose())
                {
                    player.setGoose(true);
                    player.changeSpecies();
                    ownerP.setGoose(false);
                    ownerP.changeSpecies();
                }
            }
            removeComponentBySubtag("bullet");
        }
    }

    @Override
    public void render(Main main, Renderer r)
    {
        super.render(main, r);
    }
}
