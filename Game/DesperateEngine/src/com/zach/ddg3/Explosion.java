package com.zach.ddg3;

import com.zach.ddg3.components.AABBComponent;
import com.zach.ddg3.components.WeaponComponent;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

import java.util.ArrayList;

public class Explosion extends Object
{
    WeaponComponent weapon;
    Object owner;
    private float lifeTime = 0.2f;
    public boolean isStun = false;
    private Player ownerP;
    private Vector lastKnockback = new Vector(0,0);

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    private int damage;
    //Vector knockback = new Vector(0,0);
    private ArrayList<Object> knockedObjects = new ArrayList<>(1);

    public Explosion(String name, int width, int height, String path, int totalFrames, float frameLife, WeaponComponent weapon)
    {
        super(name, width, height, path, totalFrames, frameLife);

        this.playToAndDestroy(0, totalFrames);
        this.tag = "Explosion";
        this.addComponent(new AABBComponent(this, "explosion"));

        this.weapon = weapon;
        this.owner = weapon.getParent();
        this.zIndex = owner.zIndex + 1;
        this.damage = weapon.getDamage();
        this.paddingSide = width / 8;
        this.paddingTop = height / 8;
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        this.animate(dt);
        this.offsetPos.x = (int)(this.position.x - (this.width / 2) + 320);
        this.offsetPos.y = (int)(this.position.y - (this.height / 2) + 180);
        this.updateComponents(main, gameManager, dt);

        for(int i = 0; i < knockedObjects.size(); i++)
        {
            /*if (knockedObjects.get(i) != null && knockedObjects.get(i).isTempKnocked())
            {
                knockedObjects.get(i).setKnocked(true);
                Vector knockback = knockedObjects.get(i).findVector(this.position, knockedObjects.get(i).position);
                knockback.subtractVector(lastKnockback);
                System.out.println(knockback.x + ", " + knockback.y);
                knockedObjects.get(i).applyKnockback(knockback);
                //knockedObjects.remove(i);
                lastKnockback = knockback;
            }
            if (knockedObjects.get(i) != null && !knockedObjects.get(i).isTempKnocked())
            {
                knockedObjects.remove(i);
                System.out.println("Removed");
            }*/

            if (knockedObjects.get(i) != null && knockedObjects.get(i).isKnocked())
            {
                Vector knockback = knockedObjects.get(i).findVector(this.position, knockedObjects.get(i).position);
                Vector deltaKB = knockback;
                deltaKB.subtractVector(lastKnockback);
                knockedObjects.get(i).getKnockback().addVector(deltaKB);
                //System.out.println(knockedObjects.get(i).getKnockback().x + ", " + knockedObjects.get(i).getKnockback().y);
                if((knockedObjects.get(i).getKnockback().x < knockedObjects.get(i).getKnockbackCap() && knockedObjects.get(i).getKnockback().y < knockedObjects.get(i).getKnockbackCap())
                         && (-knockedObjects.get(i).getKnockback().x > -knockedObjects.get(i).getKnockbackCap() && -knockedObjects.get(i).getKnockback().y > -knockedObjects.get(i).getKnockbackCap()))
                {
                    knockedObjects.get(i).applyKnockback(knockback, dt);
                }

                else
                    {
                        knockedObjects.get(i).setKnockback(new Vector(0,0));
                        //knockedObjects.get(i).setKnocked(false);
                        knockedObjects.remove(i);
                    }

                lastKnockback = deltaKB;
            }
            try {
                if (knockedObjects.get(i) != null && !knockedObjects.get(i).isKnocked()) {
                    knockedObjects.remove(i);
                }
            }
            catch(IndexOutOfBoundsException e)
            {

            }
        }
        if(lifeTime <= 0)
        {
            this.removeComponentBySubtag("explosion");
            for(int i = 0; i < knockedObjects.size(); i++)
            {
                //knockedObjects.get(i).setKnocked(false);
                //knockedObjects.remove(i);
            }
        }
        else
            {
                lifeTime -= dt;
            }
    }

    @Override
    public void collision(Object other, Main main)
    {
        if(other.getTag().equalsIgnoreCase("Player"))
        {
            if (!isStun)
            {
                Player player = (Player) other;
                if(weapon.isParentIsPlayer())
                {
                    ownerP = (Player) owner;
                }

            /*for(int i = 0; i <= knockedObjects.size(); i++)
            {
                if(knockedObjects.size() > 0 && other == knockedObjects.get(i))
                {
                    alreadyExists = true;
                }
            }

            if(!alreadyExists)
            {*/
                if(other.isKnockable())
                {
                    knockedObjects.add(other);
                    if (!knockedObjects.get(knockedObjects.indexOf(other)).isKnocked())
                    {
                        knockedObjects.get(knockedObjects.indexOf(other)).setKnocked(true);
                    }
                }
                //}

                if (player.getPlayerNumber() != weapon.getPlayerNumber())
                {
                    if (player.isGoose())
                        player.depleteTime(damage);

                    else if (weapon.isParentIsPlayer() && ownerP.isGoose() && !owner.isDead() && !player.isDead())
                    {
                        player.setGoose(true);
                        player.changeSpecies();
                        ownerP.setGoose(false);
                        ownerP.changeSpecies();
                    }
                }
                removeComponentBySubtag("explosion");
            }
            else
                {
                    Player player = (Player) other;
                    if (player.getPlayerNumber() != weapon.getPlayerNumber())
                    {
                        player.stun();
                    }
                }
        }
    }

    @Override
    public void render(Main main, Renderer r)
    {
        super.render(main, r);
    }
}
