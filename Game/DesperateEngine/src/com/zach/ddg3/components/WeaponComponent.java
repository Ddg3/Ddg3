package com.zach.ddg3.components;

import com.zach.ddg3.GameManager;
import com.zach.ddg3.Object;
import com.zach.ddg3.Player;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

public class WeaponComponent extends Component
{
    private float shotCooldown = 0.5f;
    private boolean isCharged = false;
    private boolean bounces = false;
    private int bounceCount = 0;
    private float speed = 75f;
    private float slowRate = 10f;
    private boolean slows = false;
    private boolean isAnimated = true;
    private float chargeTime = 0;
    private int chargeIntervals = 0;
    private boolean explodes = false;
    private boolean animatedTrails = false;
    private int damage = 1;
    private boolean effectTrails = false;
    private boolean chains = false;
    private boolean isCharging = false;
    private float tempCooldown = 0;

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    private Object parent;

    public WeaponComponent(Object parent, String subTag)
    {
        this.parent = parent;
        this.tag = "Weapon";
        this.subTag = subTag;

        this.tempCooldown = shotCooldown;
    }
    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        if (!this.isCharged)
        {
            shoot();
        }
        if (this.isCharged)
        {
            chargeShoot();
        }

        if (this.explodes)
        {
            explode();
        }

        tempCooldown -= dt;
    }

    @Override
    public void render(Main main, Renderer renderer)
    {

    }

    public void shoot()
    {
        if(parent.getTag().equalsIgnoreCase("Player"))
        {
            Player player = (Player) parent;
            if(player.device.poll() && player.getrTrigger() == -1.0f && tempCooldown <= 0)
            {
                System.out.println("BANG");
                tempCooldown = shotCooldown;
            }
        }
    }

    public void chargeShoot()
    {

    }

    public void explode()
    {

    }

    public float getShotCooldown() {
        return shotCooldown;
    }

    public void setShotCooldown(float shotCooldown) {
        this.shotCooldown = shotCooldown;
    }

    public boolean isCharged() {
        return isCharged;
    }

    public void setCharged(boolean charged) {
        isCharged = charged;
    }

    public boolean isBounces() {
        return bounces;
    }

    public void setBounces(boolean bounces) {
        this.bounces = bounces;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSlowRate() {
        return slowRate;
    }

    public void setSlowRate(float slowRate) {
        this.slowRate = slowRate;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public void setAnimated(boolean animated) {
        isAnimated = animated;
    }

    public float getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(float chargeTime) {
        this.chargeTime = chargeTime;
    }

    public int getChargeIntervals() {
        return chargeIntervals;
    }

    public void setChargeIntervals(int chargeIntervals) {
        this.chargeIntervals = chargeIntervals;
    }

    public boolean isExplodes() {
        return explodes;
    }

    public void setExplodes(boolean explodes) {
        this.explodes = explodes;
    }

    public boolean isAnimatedTrails() {
        return animatedTrails;
    }

    public void setAnimatedTrails(boolean animatedTrails) {
        this.animatedTrails = animatedTrails;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isEffectTrails() {
        return effectTrails;
    }

    public void setEffectTrails(boolean effectTrails) {
        this.effectTrails = effectTrails;
    }

    public boolean isChains() {
        return chains;
    }

    public void setChains(boolean chains) {
        this.chains = chains;
    }
}
