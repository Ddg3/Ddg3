package com.zach.ddg3.components;

import com.ivan.xinput.enums.XInputButton;
import com.zach.ddg3.*;
import com.zach.ddg3.Object;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

public class WeaponComponent extends Component
{
    private float shotCooldown = 1.0f;
    private boolean isCharged = false;
    private boolean bounces = false;
    private int bounceCount = 0;
    private float speed = 50f;
    private float slowRate = 10f;
    private boolean slows = false;
    private boolean isAnimated = true;
    private float chargeTime = 0;
    private int chargeIntervals = 0;
    private boolean explodes = false;
    private boolean animatedTrails = false;
    private int damage = 2;
    private boolean effectTrails = false;
    private boolean chains = false;
    private boolean isCharging = false;
    private float tempCooldown = 0;
    private boolean accelerates = false;
    private float accelRate = 0;
    private boolean animChangedOnShoot = false;

    private boolean parentIsPlayer = false;
    private int playerNumber = Integer.MAX_VALUE;

    private String bulletPath = "/crownSpin.png";
    private int bulletWidth = 29;
    private int bulletHeight = 23;
    private int bulletFrames = 4;
    private float bulletFrameTime = 0.1f;
    private boolean exploding = false;

    private Vector[] bulletOffsetD = new Vector[8];
    private Vector[] bulletOffsetG = new Vector[8];

    private int weaponFrameOffset = 0;

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

        chooseWeapon(subTag);
        if(parent.getTag().equalsIgnoreCase("Player"))
        {
            parentIsPlayer = true;
            Player player = (Player) parent;
            playerNumber = player.getPlayerNumber();
        }
    }
    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        if (!this.isCharged)
        {
            shoot(main);
        }
        if (this.isCharged)
        {
            chargeShoot();
        }

        if (this.explodes)
        {
            Player player = (Player) parent;
            if((player.device.getDelta().getButtons().isPressed(XInputButton.LEFT_SHOULDER) || main.getInput().isKey(player.getKeyAltShoot())) && !exploding)
            {
                exploding = true;
            }
        }
        if(animChangedOnShoot && tempCooldown <= 0 && parent.getFrameOffset() > 0)
        {
            parent.setFrameOffset(0);
        }

        tempCooldown -= dt;
    }

    @Override
    public void render(Main main, Renderer renderer)
    {

    }

    public void shoot(Main main)
    {
        if(parent.getTag().equalsIgnoreCase("Player"))
        {
            Player player = (Player) parent;
            if(tempCooldown <= 0 && parent.getFrameOffset() != 0)
            {
                parent.setFrameOffset(0);
                parent.setFrame(parent.getFrame() - (parent.getTotalFrames() / 2));
            }
            if((player.device.getDelta().getButtons().isPressed(XInputButton.RIGHT_SHOULDER) || main.getInput().isKey(player.getKeyShoot())) && tempCooldown <= 0)
            {
                tempCooldown = shotCooldown;
                Bullet bullet = new Bullet("bullet" + player.getPlayerNumber(), bulletWidth, bulletHeight, bulletPath, bulletFrames, bulletFrameTime, player.getFrame() - parent.getFrameOffset(), this);
                Vector offset;
                if(!player.isGoose())
                {
                    offset = bulletOffsetD[player.getFrame() - parent.getFrameOffset()];
                }
                else
                    {
                        offset = bulletOffsetG[player.getFrame() - parent.getFrameOffset()];
                    }
                bullet.setPosition(parent.getPositionX() + offset.getX(), parent.getPositionY() + offset.getY());
                GameManager.objects.add(bullet);

                if(animChangedOnShoot)
                {
                    parent.setFrameOffset(parent.getTotalFrames() / 2);
                    parent.setFrame(parent.getFrame() + (parent.getFrameOffset()));
                }
            }
        }
    }

    public void chargeShoot()
    {

    }

    public void chooseWeapon(String weaponName)
    {
        switch (weaponName)
        {
            case "rocketLauncher":
                speed = 50f;
                isAnimated = true;
                explodes = true;
                accelerates = true;
                accelRate = 0.1f;
                animChangedOnShoot = true;

                bulletPath = "/rocketLauncher_Bullet.png";
                bulletWidth = 33;
                bulletHeight = 33;
                bulletFrames = 8;
                bulletFrameTime = 0.1f;

                bulletOffsetD[0] = new Vector(-8,2);
                bulletOffsetD[1] = new Vector(30,22);
                bulletOffsetD[2] = new Vector(32,-2);
                bulletOffsetD[3] = new Vector(31,-26);
                bulletOffsetD[4] = new Vector(10,-4);
                bulletOffsetD[5] = new Vector(-31,-26);
                bulletOffsetD[6] = new Vector(-32,-2);
                bulletOffsetD[7] = new Vector(-30,22);

                bulletOffsetG[0] = new Vector(-10,4);
                bulletOffsetG[1] = new Vector(24,24);
                bulletOffsetG[2] = new Vector(32,-2);
                bulletOffsetG[3] = new Vector(31,-26);
                bulletOffsetG[4] = new Vector(10,-4);
                bulletOffsetG[5] = new Vector(-31,-26);
                bulletOffsetG[6] = new Vector(-32,-2);
                bulletOffsetG[7] = new Vector(-30,22);
                break;

        }
    }
    public boolean isParentIsPlayer() {
        return parentIsPlayer;
    }

    public void setParentIsPlayer(boolean parentIsPlayer) {
        this.parentIsPlayer = parentIsPlayer;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
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

    public boolean isAccelerates() {
        return accelerates;
    }

    public void setAccelerates(boolean accelerates) {
        this.accelerates = accelerates;
    }

    public float getAccelRate() {
        return accelRate;
    }

    public void setAccelRate(float accelRate) {
        this.accelRate = accelRate;
    }

    public boolean isSlows() {
        return slows;
    }

    public void setSlows(boolean slows) {
        this.slows = slows;
    }

    public boolean isAnimChangedOnShoot() {
        return animChangedOnShoot;
    }

    public void setAnimChangedOnShoot(boolean animChangedOnShoot) {
        this.animChangedOnShoot = animChangedOnShoot;
    }

    public boolean isExploding() {
        return exploding;
    }

    public void setExploding(boolean exploding) {
        this.exploding = exploding;
    }
}
