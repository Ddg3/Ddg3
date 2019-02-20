package com.zach.ddg3;

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

    public Bullet(String name, int width, int height, String path, int totalFrames, float frameLife, int direction, float posX, float posY, WeaponComponent weapon)
    {
        super(name, width, height, path, totalFrames, frameLife);
        this.position.x = posX;
        this.position.y = posY;
        this.direction = direction;

        this.weapon = weapon;
        this.owner = weapon.getParent();

        this.speed = weapon.getSpeed();
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        move(dt);
    }

    public void move(float dt)
    {
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
    public void collision(Object other)
    {

    }
}
