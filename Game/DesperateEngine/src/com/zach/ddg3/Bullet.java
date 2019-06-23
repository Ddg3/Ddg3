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
    private boolean collidingTop = false;
    private boolean collidingBottom = false;
    private boolean collidingLeft = false;
    private boolean collidingRight = false;
    private boolean planted = false;

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
            this.playInRange(0, this.getTotalFrames() - 1);
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
        if(weapon.isPlanting())
        {
            plant();
            //weapon.setPlanting(false);
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
        }

        if(colliding)
        {
            tempCollisionBuffer += dt;
            if(tempCollisionBuffer >= collisionBuffer)
            {
                colliding = false;
                collidingBottom = false;
                collidingLeft = false;
                collidingRight = false;
                collidingTop = false;
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
        weapon.bullets.remove(this);
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

    public void plant()
    {
        this.speed = 0;
        this.stop();
    }

    @Override
    public void render(Main main, Renderer r)
    {
        super.render(main, r);
    }

    @Override
    public void collision(Object other, Main main)
    {
        if(other.getTag().equalsIgnoreCase("Wall"))
        {
            AABBComponent myC = (AABBComponent)this.findComponentBySubtag("bullet");
            AABBComponent otherC = (AABBComponent)other.findComponentBySubtag("wall");
            //System.out.println(Math.abs(myC.getLastCenterX() - otherC.getLastCenterX()) + " < " + (myC.getHalfWidth() + otherC.getHalfWidth()));
            if (Math.abs(myC.getLastCenterX() - otherC.getLastCenterX()) < (myC.getHalfWidth() + otherC.getHalfWidth()) - 2) {
                //Top/bottom collision because X values are closer than Y
                //Top
                if (myC.getCenterY() < otherC.getCenterY())
                {
                    collidingTop = true;
                }

                //Bottom
                if (myC.getCenterY() > otherC.getCenterY())
                {
                    collidingBottom = true;
                }
            }
            else
            {
                //Side collision bc vice versa
                //Left
                if (myC.getCenterX() < otherC.getCenterX())
                {
                    collidingRight = true;
                }

                //Right
                if (myC.getCenterX() > otherC.getCenterX())
                {
                    collidingLeft = true;
                }
            }
            if(weapon.isBounces())
            {
                if(!colliding)
                {
                    switch (direction)
                    {
                        case 0:
                            direction = 4;
                            break;
                        case 1:
                            if(collidingTop)
                            {
                                direction = 3;
                            }
                            if(collidingLeft || collidingRight)
                            {
                                direction = 7;
                            }
                            break;
                        case 2:
                            direction = 6;
                            break;
                        case 3:
                            if(collidingBottom)
                            {
                                direction = 1;
                            }
                            if(collidingLeft || collidingRight)
                            {
                                direction = 5;
                            }
                            break;
                        case 4:
                            direction = 0;
                            break;
                        case 5:
                            if(collidingBottom)
                            {
                                direction = 7;
                            }
                            if(collidingLeft || collidingRight)
                            {
                                direction = 3;
                            }
                            break;
                        case 6:
                            direction = 2;
                            break;
                        case 7:
                            if(collidingTop)
                            {
                                direction = 5;
                            }
                            if(collidingLeft || collidingRight)
                            {
                                direction = 1;
                            }
                            break;
                    }
                }

                colliding = true;

                if (weapon.getBounceCount() > 0)
                {
                    tempBounces++;
                    speed += weapon.getSpeedOnBounce();
                    if (tempBounces == weapon.getBounceCount())
                    {
                        if (weapon.isExplodes())
                        {
                            weapon.bullets.remove(this);
                            explode(false);
                        }
                        else
                            {
                                weapon.bullets.remove(this);
                                GameManager.objects.remove(this);
                            }
                    }
                }
            }
            else if(!weapon.isStopsAtWall())
            {
                if (weapon.isExplodes())
                {
                    explode(false);
                    weapon.bullets.remove(this);
                }
                else
                    {
                        GameManager.objects.remove(this);
                    }
            }

            else
            {
                this.speed = 0;
                this.stop();
            }
        }
        else if(other.getTag().equalsIgnoreCase("Bullet"))
        {
            if (weapon.isExplodes())
            {
                explode(true);
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
