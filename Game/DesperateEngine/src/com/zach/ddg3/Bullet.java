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
    private int offsetX;
    private int offsetY;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

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

        this.paddingTop = this.height / 2;
        this.paddingSide = this.width / 4;
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        move(dt);
        if(weapon.isExploding())
        {
            explode(false);
        }
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
            case 0: this.position.y += speed * dt;
                this.zIndex = weapon.getParent().zIndex;
                this.maxzIndex = weapon.getParent().maxzIndex;
                break;
            case 1: this.position.y += speed * dt; this.position.x += speed * dt;
                this.zIndex = weapon.getParent().zIndex;
                this.maxzIndex = weapon.getParent().maxzIndex;
                break;
            case 2: this.position.x += speed * dt;
                this.zIndex = weapon.getParent().zIndex - 1;
                this.maxzIndex = weapon.getParent().maxzIndex - 1;
                break;
            case 3: this.position.y -= speed * dt; this.position.x += speed * dt;
                this.zIndex = weapon.getParent().zIndex - 1;
                this.maxzIndex = weapon.getParent().maxzIndex - 1;
                break;
            case 4: this.position.y -= speed * dt;
                this.zIndex = weapon.getParent().zIndex - 1;
                this.maxzIndex = weapon.getParent().maxzIndex - 1;
                break;
            case 5: this.position.y -= speed * dt; this.position.x -= speed * dt;
                this.zIndex = weapon.getParent().zIndex - 1;
                this.maxzIndex = weapon.getParent().maxzIndex - 1;
                break;
            case 6: this.position.x -= speed * dt;
                this.zIndex = weapon.getParent().zIndex - 1;
                this.maxzIndex = weapon.getParent().maxzIndex - 1;
                break;
            case 7: this.position.y += speed * dt; this.position.x -= speed * dt;
                this.zIndex = weapon.getParent().zIndex;
                this.maxzIndex = weapon.getParent().maxzIndex;
        }
    }

    public void explode(boolean alreadyHit)
    {
        float tempPosX = this.position.x;
        float tempPosY = this.position.y;
        GameManager.objects.remove(this);

        weapon.setExploding(false);
        Explosion explosion = new Explosion("explosion",90, 82, "/explosion.png", 25, 0.03f, weapon);
        explosion.setPosition(tempPosX, tempPosY);
        GameManager.objects.add(explosion);
        if(alreadyHit)
        {
            explosion.setDamage(0);
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
            if (weapon.isExplodes())
            {
                explode(false);
                //weapon.setExploding(false);
            }
        }
        else if(other.getTag().equalsIgnoreCase("Player"))
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
                if (weapon.isExplodes())
                {
                    explode(true);
                    //weapon.setExploding(false);
                }
            }
        }
    }
}
