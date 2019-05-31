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
    private int tempBounces = 0;
    private boolean colliding = false;
    private float tempCollisionBuffer = 0;
    private float collisionBuffer = 0.02f;

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

        if(weapon.isHasDirection())
        {
            this.setFrame(direction);
        }
        if(weapon.isAnimated())
        {
            this.playInRange(0,3);
        }
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
            weapon.setExploding(false);
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
            if(this.speed <= 0 && weapon.isAnimated())
            {
                this.stop();
            }
            else
            {
                this.speed -= tempSlow;
                tempSlow += (dt / 25);
                this.setFrameLife(this.getFrameLife() + (dt / 30));
            }
        }

        if(weapon.isAccelerates())
        {
            this.speed += tempAccel;
            tempAccel += dt;
        }

        if(colliding)
        {
            tempCollisionBuffer += dt;
            if(tempCollisionBuffer >= collisionBuffer)
            {
                colliding = false;
            }
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
            if(weapon.isBounces())
            {
                /*int remainder = 0;
                if (direction + 4 - 8 > 0)
                {
                    remainder = direction + 4 - 8;
                }

                direction += 4;*/

                if(!colliding)
                {
                    switch (direction)
                    {
                        case 0:
                        direction = 4;
                        break;
                        case 1:
                            direction = 5;
                            break;
                        case 2:
                            direction = 6;
                            break;
                        case 3:
                            direction = 7;
                            break;
                        case 4:
                            direction = 0;
                            break;
                        case 5:
                            direction = 1;
                            break;
                        case 6:
                            direction = 2;
                            break;
                        case 7:
                            direction = 3;
                            break;
                    }
                }

                colliding = true;

                if (weapon.getBounceCount() > 0)
                {
                    tempBounces++;
                    if (tempBounces == weapon.getBounceCount())
                    {
                        if (weapon.isExplodes())
                        {
                            explode(false);
                        }
                        else
                            {
                                GameManager.objects.remove(this);
                            }
                    }
                }
            }
            else if(!weapon.isStopsAtWall())
            {
                GameManager.objects.remove(this);
                if (weapon.isExplodes())
                {
                    explode(false);
                }
            }

            else
            {
                this.speed = 0;
                this.stop();
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
