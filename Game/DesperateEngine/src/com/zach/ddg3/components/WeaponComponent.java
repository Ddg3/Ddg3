package com.zach.ddg3.components;

import com.ivan.xinput.enums.XInputButton;
import com.zach.ddg3.*;
import com.zach.ddg3.Object;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class WeaponComponent extends Component
{
    private float shotCooldown = 1.0f;
    private float altCooldown = 8.0f;
    private float tempAltCooldown = /*altCooldown*/ 0;
    private boolean isCharged = false;
    private boolean bounces = false;
    private int bounceCount = 0;
    private float speed = 50f;
    private float slowRate = 10f;
    private boolean slows = false;
    private boolean isAnimated = false;
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
    private boolean stopsAtWall = false;
    private boolean hasDirection = true;

    private boolean parentIsPlayer = false;
    private int playerNumber = Integer.MAX_VALUE;

    private String bulletPath = "/crownSpin.png";
    private int bulletWidth = 29;
    private int bulletHeight = 23;
    private int bulletFrames = 4;
    private float bulletFrameTime = 0.1f;
    private boolean exploding = false;
    private boolean isPlanted = false;
    private boolean planting = false;
    private boolean detonated = false;
    private int speedOnBounce = 0;
    private boolean collides = true;
    private boolean isChained = false;
    private float timer = 0f;
    private boolean isOstrich = false;

    private int explosionWidth = 90;
    private int explosionHeight = 82;
    private int explosionFrames = 25;
    private String explosionPath = "/explosion.png";

    public boolean isTriggered = false;
    public Vector triggerPoint = new Vector(0,0);

    private int altWidth;
    private int altHeight;
    private int altFrames;
    private String altPath;

    private Vector[] bulletOffsetD = new Vector[8];
    private Vector[] bulletOffsetG = new Vector[8];

    private int weaponFrameOffset = 0;
    private Vector[] bulletOffset = new Vector[8];

    public ArrayList<Bullet> bullets = new ArrayList<>(1);
    private int bulletMax = Integer.MAX_VALUE;
    private int firstBulletIndex = 0;

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    private Object parent;
    private Random random;

    public WeaponComponent(Object parent, String subTag)
    {
        random = new Random();
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
        if(parentIsPlayer)
        {
            Player player = (Player) parent;
            if (!this.isCharged) {
                shoot(main);
            }
            altShoot(main);
            if (this.isCharged) {
                chargeShoot();
            }

            if (this.detonated) {
                //Player player = (Player) parent;
                if (!player.isTimedOut()) {
                    if ((player.device.getDelta().getButtons().isPressed(XInputButton.LEFT_SHOULDER)
                            || (player.isKeyBoard() && main.getInput().isButton(MouseEvent.BUTTON3))))
                        if (!exploding) {
                            {
                                exploding = true;
                            }
                        }
                }
            }

            if (isPlanted) {
                //Player player = (Player) parent;
                if (!player.isTimedOut()) {
                    if (player.device.getDelta().getButtons().isPressed(XInputButton.LEFT_SHOULDER)
                            || (player.isKeyBoard() && main.getInput().isButton(MouseEvent.BUTTON3)) && !planting) {
                        planting = true;
                    }
                }
            }

            if (animChangedOnShoot && tempCooldown <= 0 && parent.getFrameOffset() > 0) {
                parent.setFrameOffset(0);
            }

            tempAltCooldown -= dt;

            GameManager.altReloadTime.set(player.getPlayerNumber(), (int)tempAltCooldown);

            if (tempAltCooldown <= 0 && !player.getIndicator().visible)
            {
                player.getIndicator().visible = true;
                GameManager.altReloadText.get(player.getPlayerNumber()).visible = false;
            }
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
            exploding = false;
            planting = false;
            Player player = (Player) parent;
            if(!player.isTimedOut())
            {
                if (tempCooldown <= 0 && parent.getFrameOffset() != 0) {
                    parent.setFrameOffset(0);
                    parent.setFrame(parent.getFrame() - (parent.getTotalFrames() / 2));
                }
                if ((player.device.getDelta().getButtons().isPressed(XInputButton.RIGHT_SHOULDER) ||
                        (player.isKeyBoard() && main.getInput().isButton(MouseEvent.BUTTON1))) && tempCooldown <= 0)
                {
                    tempCooldown = shotCooldown;
                    Bullet bullet = new Bullet("bullet" + player.getPlayerNumber(), bulletWidth, bulletHeight, bulletPath, bulletFrames, bulletFrameTime, player.getFrame() - parent.getFrameOffset(), this);
                    Vector offset;
                    if (!player.isGoose())
                    {
                        offset = bulletOffsetD[player.getFrame() - parent.getFrameOffset()];
                    }
                    else
                        {
                          offset = bulletOffsetG[player.getFrame() - parent.getFrameOffset()];
                        }
                    bullet.setPosition(parent.getPositionX() + offset.getX(), parent.getPositionY() + offset.getY());
                    bullet.getObjImage().changeColor(player.getSkinColors()[1], player.getSkinColors()[player.getSkIndex()]);
                    GameManager.objects.add(bullet);
                    bullets.add(bullet);

                    if(bullets.size() >= bulletMax)
                    {
                        if(bullets.get(0).getWeapon().isExplodes())
                        {
                            bullets.get(0).explode(false);
                        }
                    }

                    if (animChangedOnShoot)
                    {
                        parent.setFrameOffset(parent.getTotalFrames() / 2);
                        parent.setFrame(parent.getFrame() + (parent.getFrameOffset()));
                    }
                }
            }
        }
    }

    public void shoot(String name, int width, int height, String path, int frames, float frameLife, int frame, int direction)
    {
        Bullet bullet = new Bullet(name, width, height, path, frames, frameLife, frame, this);
        bullet.setPosition(parent.getPositionX(), parent.getPositionY());
        bullet.setDirection(direction);
        bullet.zIndex = parent.zIndex + 1;
        GameManager.objects.add(bullet);
        bullet.offsetPos = new Vector(bullet.getPositionX() + 6400, bullet.getPositionY() + 3600);
    }

    public void shoot(int direction)
    {
        if(tempCooldown <= 0)
        {
            Bullet bullet = new Bullet("bullet", bulletWidth, bulletHeight, bulletPath, bulletFrames, bulletFrameTime, direction, this);
            random = new Random();
            int randomOffsetX = random.nextInt(20) - 10;
            int randomOffsetY = random.nextInt(10) - 5;
            bullet.setPosition(parent.getPositionX() + randomOffsetX, parent.getPositionY() + randomOffsetY);
            bullet.setDirection(direction);
            bullet.zIndex = 1;
            GameManager.objects.add(bullet);
            bullet.offsetPos = new Vector(bullet.getPositionX() + 6400, bullet.getPositionY() + 3600);
            tempCooldown = shotCooldown;
        }
    }

    public void shoot(String name, int width, int height, String path, int frames, float frameLife, int direction, Vector triggerPoint)
    {
        Bullet bullet = new Bullet(name, width, height, path, frames, frameLife, direction, this);
        bullet.setPosition(parent.getPositionX(), parent.getPositionY());
        GameManager.objects.add(bullet);
        bullet.triggerPoint = triggerPoint;
        bullet.offsetPos = new Vector(bullet.getPositionX() + 640, bullet.getPositionY() + 360);
    }
    public void shoot(String name, int width, int height, String path, int frames, float frameLife, int direction, Vector triggerPoint, float speed)
    {
        Bullet bullet = new Bullet(name, width, height, path, frames, frameLife, direction, this);
        bullet.setPosition(parent.getPositionX(), parent.getPositionY());
        GameManager.objects.add(bullet);
        bullet.triggerPoint = triggerPoint;
        bullet.setSpeed(speed);
        bullet.offsetPos = new Vector(bullet.getPositionX() + 6400, bullet.getPositionY() + 3600);
    }

    public void altShoot(Main main)
    {
        if(parent.getTag().equalsIgnoreCase("Player"))
        {
            exploding = false;
            planting = false;
            Player player = (Player) parent;
            if(!player.isTimedOut())
            {
                if ((player.device.getDelta().getButtons().isPressed(XInputButton.Y) ||
                        (player.isKeyBoard() && main.getInput().isButton(MouseEvent.BUTTON2))) && tempAltCooldown <= 0)
                {
                    tempAltCooldown = altCooldown;
                    player.getIndicator().visible = false;
                    Bullet bullet = new Bullet("altBullet" + player.getPlayerNumber(), altWidth, altHeight, altPath, altFrames, 0.05f, player.getFrame() - parent.getFrameOffset(), this);
                    Vector offset;
                    if (!player.isGoose())
                    {
                        offset = bulletOffsetD[player.getFrame() - parent.getFrameOffset()];
                    }
                    else
                    {
                        offset = bulletOffsetG[player.getFrame() - parent.getFrameOffset()];
                    }
                    bullet.setPosition(parent.getPositionX() + offset.getX(), parent.getPositionY() + offset.getY());
                    bullet.getObjImage().changeColor(player.getSkinColors()[1], player.getSkinColors()[player.getSkIndex()]);
                    GameManager.objects.add(bullet);
                    bullets.add(bullet);
                    bullet.isAlt = true;
                    GameManager.altReloadText.get(player.getPlayerNumber()).visible = true;
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
                speed = 225f;
                explodes = true;
                accelerates = true;
                accelRate = 0.1f;
                detonated = true;
                animChangedOnShoot = true;

                bulletPath = "/rocketLauncher_Bullet.png";
                bulletWidth = 35;
                bulletHeight = 35;
                bulletFrames = 8;
                bulletFrameTime = 0.1f;

                altWidth = 74;
                altHeight = 74;
                altFrames = 8;
                altPath = "/missile.png";

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

            case "grenadeLauncher":
                speed = 120f;
                isAnimated = true;
                isPlanted = true;
                explodes = true;
                animChangedOnShoot = true;
                stopsAtWall = true;
                hasDirection = false;
                bulletMax = 14;
                collides = false;
                isChained = true;
                timer = 10f;
                shotCooldown = 0.65f;

                bulletPath = "/grenadeLauncher_Bullet.png";
                bulletWidth = 16;
                bulletHeight = 16;
                bulletFrames = 4;
                bulletFrameTime = 0.1f;

                altWidth = 38;
                altHeight = 45;
                altFrames = 8;
                altPath = "/mirv.png";


                /*explosionWidth /= 2;
                explosionHeight /= 2;
                explosionPath = "/smallExplosion.png";*/

                bulletOffsetD[0] = new Vector(-3,4);
                bulletOffsetD[1] = new Vector(22,22);
                bulletOffsetD[2] = new Vector(41,-2);
                bulletOffsetD[3] = new Vector(31,-24);
                bulletOffsetD[4] = new Vector(10,-4);
                bulletOffsetD[5] = new Vector(-31,-24);
                bulletOffsetD[6] = new Vector(-41,-2);
                bulletOffsetD[7] = new Vector(-22,22);

                bulletOffsetG[0] = new Vector(-10,4);
                bulletOffsetG[1] = new Vector(24,24);
                bulletOffsetG[2] = new Vector(32,-2);
                bulletOffsetG[3] = new Vector(31,-26);
                bulletOffsetG[4] = new Vector(10,-4);
                bulletOffsetG[5] = new Vector(-31,-26);
                bulletOffsetG[6] = new Vector(-32,-2);
                bulletOffsetG[7] = new Vector(-30,22);
                break;

            case "cannon":
                speed = 110f;
                explodes = true;
                bounces = true;
                detonated = true;
                hasDirection = false;
                accelerates = true;
                accelRate = 0.1f;
                bulletMax = 8;
                speedOnBounce = 1;
                isAnimated = true;
                shotCooldown = 0.5f;

                bulletPath = "/cannon_Bullet.png";
                bulletWidth = 29;
                bulletHeight = 29;
                bulletFrames = 19;
                bulletFrameTime = 0.05f;

                altWidth = 25;
                altHeight = 32;
                altFrames = 6;
                altPath = "/stunBomb.png";

                /*explosionWidth /= 2;
                explosionHeight /= 2;
                explosionPath = "/smallExplosion.png";*/

                bulletOffsetD[0] = new Vector(0,12);
                bulletOffsetD[1] = new Vector(42,32);
                bulletOffsetD[2] = new Vector(56,8);
                bulletOffsetD[3] = new Vector(40,-40);
                bulletOffsetD[4] = new Vector(2,-18);
                bulletOffsetD[5] = new Vector(-40,-40);
                bulletOffsetD[6] = new Vector(-56,8);
                bulletOffsetD[7] = new Vector(-42,32);

                bulletOffsetG[0] = new Vector(-10,4);
                bulletOffsetG[1] = new Vector(24,24);
                bulletOffsetG[2] = new Vector(32,-2);
                bulletOffsetG[3] = new Vector(31,-26);
                bulletOffsetG[4] = new Vector(10,-4);
                bulletOffsetG[5] = new Vector(-31,-26);
                bulletOffsetG[6] = new Vector(-32,-2);
                bulletOffsetG[7] = new Vector(-30,22);
                break;

            case "ostrichLauncher":
                speed = 90f;
                explodes = true;
                isAnimated = true;
                collides = false;
                shotCooldown = 0.2f;
                isOstrich = true;

                bulletPath = "/ostrich.png";
                bulletWidth = 91;
                bulletHeight = 119;
                bulletFrames = 7;
                bulletFrameTime = 0.15f;
                break;
        }
    }

    public boolean isOstrich() {
        return isOstrich;
    }

    public void setOstrich(boolean ostrich) {
        isOstrich = ostrich;
    }

    public float getTempAltCooldown() {
        return tempAltCooldown;
    }

    public void setTempAltCooldown(float tempAltCooldown) {
        this.tempAltCooldown = tempAltCooldown;
    }
    public int getAltWidth() {
        return altWidth;
    }

    public void setAltWidth(int altWidth) {
        this.altWidth = altWidth;
    }

    public int getAltHeight() {
        return altHeight;
    }

    public void setAltHeight(int altHeight) {
        this.altHeight = altHeight;
    }

    public int getAltFrames() {
        return altFrames;
    }

    public void setAltFrames(int altFrames) {
        this.altFrames = altFrames;
    }

    public String getAltPath() {
        return altPath;
    }

    public void setAltPath(String altPath) {
        this.altPath = altPath;
    }

    public float getAltCooldown() {
        return altCooldown;
    }

    public void setAltCooldown(float altCooldown) {
        this.altCooldown = altCooldown;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }
    public boolean isChained() {
        return isChained;
    }

    public void setChained(boolean chained) {
        isChained = chained;
    }

    public boolean isCollides() {
        return collides;
    }

    public void setCollides(boolean collides) {
        this.collides = collides;
    }
    public int getExplosionWidth() {
        return explosionWidth;
    }

    public void setExplosionWidth(int explosionWidth) {
        this.explosionWidth = explosionWidth;
    }

    public int getExplosionHeight() {
        return explosionHeight;
    }

    public void setExplosionHeight(int explosionHeight) {
        this.explosionHeight = explosionHeight;
    }

    public int getExplosionFrames() {
        return explosionFrames;
    }

    public void setExplosionFrames(int explosionFrames) {
        this.explosionFrames = explosionFrames;
    }

    public String getExplosionPath() {
        return explosionPath;
    }

    public void setExplosionPath(String explosionPath) {
        this.explosionPath = explosionPath;
    }
    public int getSpeedOnBounce() {
        return speedOnBounce;
    }

    public void setSpeedOnBounce(int speedOnBounce) {
        this.speedOnBounce = speedOnBounce;
    }
    public boolean isDetonated() {
        return detonated;
    }

    public void setDetonated(boolean detonated) {
        this.detonated = detonated;
    }
    public boolean isPlanting() {
        return planting;
    }

    public void setPlanting(boolean planting) {
        this.planting = planting;
    }
    public int getBulletMax() {
        return bulletMax;
    }

    public void setBulletMax(int bulletMax) {
        this.bulletMax = bulletMax;
    }

    public boolean isPlanted() {
        return isPlanted;
    }

    public void setPlanted(boolean planted) {
        isPlanted = planted;
    }
    public int getBounceCount() {
        return bounceCount;
    }

    public void setBounceCount(int bounceCount) {
        this.bounceCount = bounceCount;
    }

    public boolean isHasDirection() {
        return hasDirection;
    }

    public void setHasDirection(boolean hasDirection) {
        this.hasDirection = hasDirection;
    }

    public boolean isStopsAtWall() {
        return stopsAtWall;
    }

    public void setStopsAtWall(boolean stopsAtWall) {
        this.stopsAtWall = stopsAtWall;
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
