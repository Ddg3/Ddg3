package com.zach.ddg3;

import com.zach.ddg3.components.AABBComponent;
import com.zach.ddg3.components.WeaponComponent;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

public class Bullet extends Object
{
    public Object getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public WeaponComponent getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponComponent weapon) {
        this.weapon = weapon;
    }

    private int direction;
    private Object owner;
    private WeaponComponent weapon;

    private float speed;
    private float tempSlow = 0f;
    private float tempAccel = 0f;

    public Bullet(String name, int width, int height, String path, int totalFrames, float frameLife, int direction, WeaponComponent weapon)
    {
        super(name, width, height, path, totalFrames, frameLife);
        this.tag = "Bullet";
        this.direction = direction;

        this.weapon = weapon;
        this.owner = weapon.getParent();

        this.speed = weapon.getSpeed();

        if(weapon.isSlows())
        {
            tempSlow = weapon.getSlowRate();
        }

        if(weapon.isAccelerates())
        {
            tempAccel = weapon.getAccelRate();
        }

        this.setFrame(direction);
        this.addComponent(new AABBComponent(this, "bullet"));

    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        move(dt);

        this.offsetPos.x = (int)(this.position.x - (this.width / 2) + 320);
        this.offsetPos.y = (int)(this.position.y - (this.height / 2) + 180);
        this.updateComponents(main, gameManager, dt);
        //System.out.println(this.position.y);

        if(weapon.isAnimated())
        {
            this.animate(dt);
        }

        if(weapon.isSlows())
        {
            this.speed -= tempSlow;
            tempSlow += dt;
        }

        if(weapon.isAccelerates())
        {
            this.speed += tempAccel;
            tempAccel += dt;
        }
    }

    public void move(float dt)
    {
        //System.out.println(this.position.x + ", " + this.position.y);
        switch(direction)
        {
            case 0: this.position.y += speed * dt; break;
            case 1: this.position.y += speed * dt; this.position.x += speed * dt; break;
            case 2: this.position.x += speed * dt; break;
            case 3: this.position.y -= speed * dt; this.position.x += speed * dt; break;
            case 4: this.position.y -= speed * dt; break;
            case 5: this.position.y -= speed * dt; this.position.x -= speed * dt; break;
            case 6: this.position.x -= speed * dt; break;
            case 7: this.position.y += speed * dt; this.position.x -= speed * dt;
        }
    }

    @Override
    public void render(Main main, Renderer r)
    {
        super.render(main, r);
    }

    @Override
    public void collision(Object other, Main main)
    {
        if(other.getTag().equalsIgnoreCase("Wall") || other.getTag().equalsIgnoreCase("Bullet") )
        {
            GameManager.objects.remove(this);
        }
        else if(other.getTag().equalsIgnoreCase("Player"))
        {
            Player player = (Player)other;
            if(player.getPlayerNumber() != weapon.getPlayerNumber())
            {
                player.depleteTime(weapon.getDamage());
                if (weapon.isExplodes()) {
                    weapon.explode();
                }

                GameManager.objects.remove(this);
            }
        }
    }
}
