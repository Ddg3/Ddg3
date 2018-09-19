package com.zach.ddg3;

/**
 * Created by Zach on 2/16/2018.
 */
public class Weapon
{

    public String weaponTag;

    public int getAnimationSet() {
        return animationSet;
    }

    public void setAnimationSet(int animationSet) {
        this.animationSet = animationSet;
    }

    public int animationSet;
    public int getDweaponWidth() {
        return dweaponWidth;
    }

    public void setDweaponWidth(int dweaponWidth) {
        this.dweaponWidth = dweaponWidth;
    }

    public int getDweaponHeight() {
        return dweaponHeight;
    }

    public void setDweaponHeight(int dweaponHeight) {
        this.dweaponHeight = dweaponHeight;
    }

    public int getGweaponWidth() {
        return gweaponWidth;
    }

    public void setGweaponWidth(int gweaponWidth) {
        this.gweaponWidth = gweaponWidth;
    }

    public int getGweaponHeight() {
        return gweaponHeight;
    }

    public void setGweaponHeight(int gweaponHeight) {
        this.gweaponHeight = gweaponHeight;
    }

    public int dweaponWidth;
    public int dweaponHeight;

    public int gweaponWidth;
    public int gweaponHeight;

    public Bullet weaponBullet;

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public float cooldown;

    public Bullet getBullet() {
        return weaponBullet;
    }

    public void setBullet(Bullet weaponBullet) {
        this.weaponBullet = weaponBullet;
    }
    public void setWeaponTag(String weaponTag) {
        this.weaponTag = weaponTag;
    }

    public String getWeaponTag() {
        return weaponTag;
    }






}
