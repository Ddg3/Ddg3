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
    private float timeCountdown = 0.1f;
    private boolean counting = false;
    private float tempTimer = 0;
    public boolean isAlt = false;
    private boolean played = false;
    private int split = 0;
    private float splitBuffer = 1f;
    private float endBuffer = 0.5f;
    private boolean splitF = false;
    private boolean slowing = false;
    private float slowBuffer = 0.03f;
    private float tempSlowBuffer = 0f;
    public Vector triggerPoint = new Vector(0,0);

    public float getPlantTime() {
        return plantTime;
    }

    public void setPlantTime(float plantTime) {
        this.plantTime = plantTime;
    }

    private float plantTime = 0;
    private float tempPlantTime = 0;

    public Bullet(String name, int width, int height, String path, int totalFrames, float frameLife, int direction, WeaponComponent weapon)
    {
        super(name, width, height, path, totalFrames, frameLife);
        this.tag = "Bullet";
        this.direction = direction;

        tempTimer = weapon.getTimer();
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

        if(weapon.isHasDirection() && !isAlt)
        {
            this.setFrame(direction);
        }
        if(weapon.isAnimated())
        {
            if(direction >= 0 && direction <= 4)
            {
                this.playInRange(0, this.getTotalFrames() - 1);
            }
            else
                {
                    this.playReverseInRange(0, this.getTotalFrames() - 1);
                }
        }
        this.addComponent(new AABBComponent(this, "bullet"));

        this.paddingTop = this.height / 2;
        this.paddingSide = this.width / 4;

        if(isAlt)
        {
            altInit(weapon.getSubTag());
        }

        tempPlantTime = plantTime;
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        move(dt);
        this.offsetPos.x = (int) (this.position.x - (this.width / 2) + 320);
        this.offsetPos.y = (int) (this.position.y - (this.height / 2) + 180);
        this.updateComponents(main, gameManager, dt);
        this.animate(dt);

        if(!isAlt)
        {
            if (weapon.isExploding()) {
                explode(false);
                weapon.setExploding(false);
            }
            if (weapon.isPlanting()) {
                plant();
                //weapon.setPlanting(false);
            }
            //System.out.println(this.position.y);

            if (weapon.isSlows()) {
                if (this.speed <= 0 && weapon.isAnimated()) {
                    this.stop();
                } else {
                    this.speed -= tempSlow;
                    tempSlow += (dt / 25);
                    this.setFrameLife(this.getFrameLife() + (dt / 30));
                }
            }

            if (weapon.isAccelerates()) {
                this.speed += tempAccel;
            }

            if (colliding) {
                tempCollisionBuffer += dt;
                if (tempCollisionBuffer >= collisionBuffer) {
                    colliding = false;
                    collidingBottom = false;
                    collidingLeft = false;
                    collidingRight = false;
                    collidingTop = false;
                }
            }

            if (counting) {
                timeCountdown -= dt;
            }

            if (timeCountdown <= 0) {
                counting = false;
                explode(false);
            }

            if (weapon.getTimer() > 0) {
                if (tempTimer <= 0) {
                    if (weapon.isExplodes()) {
                        explode(false);
                    }
                }

                tempTimer -= dt;
            }

            if (this.speed <= 0) {
                this.stop();
            }
        }
        else
            {
                alt(weapon.getSubTag(), dt);
            }

        /*if(tempPlantTime > 0)
        {
            tempPlantTime -= dt;
        }

        if(tempPlantTime <= 0 && plantTime > 0)
        {
            plant();
        }*/

        if(isAlt && weapon.getSubTag() == "cannon")
        {
            setFrameLife(0.3f);
        }

        if(slowing)
        {
            tempSlowBuffer -= dt;
            if(tempSlowBuffer <= 0)
            {
                slowing = false;
            }
        }
    }

    public void altInit(String tag)
    {
        switch (tag)
        {
            case "rocketLauncher":
                direction = 10;
                setFrameLife(1f);
                break;
            case "grenadeLauncher":
                play();
                break;
            case "cannon":
                //playInRangeAndBack(0, 4);
                //play();
                playTo(0, 4);
                break;
        }
    }

    public void alt(String tag, float dt)
    {
        switch (tag)
        {
            case "rocketLauncher":
                homeIn();
                break;
            case "grenadeLauncher":
                mirv(dt);
                break;
            case "cannon":
                //stun();
                break;
        }
    }

    public void homeIn()
    {
        setFrameLife(0.3f);
        Player player = (Player) weapon.getParent();
        Player target = null;
        speed = 55f;
        for(int i = 0; i < GameManager.players.size(); i++)
        {
            if(GameManager.players.get(i).getPlayerNumber() != player.getPlayerNumber())
            {
                target = GameManager.players.get(i);
            }
        }
        int hDist = (int)(this.position.x - target.getPositionX());
        int vDist = (int)(this.position.y - target.getPositionY());

        if(direction == 10)
        {
            if (hDist >= vDist) {
                if (hDist < 0) {
                    direction = 2;
                } else {
                    direction = 6;
                }
            } else {
                if (vDist < 0) {
                    direction = 0;
                } else {
                    direction = 4;
                }
            }
        }

        switch (direction)
        {
            case 1:
                direction = 2;
                break;
            case 3:
                direction = 4;
                break;
            case 5:
                direction = 6;
                break;
            case 7:
                direction = 0;
                break;
        }
        if(direction == 2 || direction == 6)
        {
            if(hDist < 5 && hDist > -5)
            {
                if(vDist < 0)
                {
                    direction = 0;
                    played = false;
                }
                else
                {
                    direction = 4;
                    played = false;
                }
            }
        }
        else if(direction == 0 || direction == 4)
        {
            if(vDist < 5 && vDist > -5)
            {
                if (hDist < 0)
                {
                    direction = 2;
                    played = false;
                }
                else
                    {
                        direction = 6;
                        played = false;
                    }
            }
        }

        switch (direction)
        {
            case 0:
                if(!played)
                {
                    playInRange(2, 4);
                    played = true;
                }
                break;
            case 2:
                if(!played)
                {
                    playInRange(0, 2);
                    played = true;
                }
                break;
            case 4:
                if(!played)
                {
                    playInRange(6, 8);
                    played = true;
                }
                break;
            case 6:
                if(!played)
                {
                    playInRange(4, 6);
                    played = true;
                }
                break;
        }
    }

    public void mirv(float dt)
    {
        if (this.speed <= 0 && weapon.isAnimated())
        {
            this.stop();
        }
        else
            {
            this.speed -= tempSlow;
            tempSlow += (dt / 2);
            this.setFrameLife(this.getFrameLife() + (dt / 30));
        }

        if(speed <= 0)
        {
            if(split < 2 && !splitF)
            {
                for (int i = 0; i < 8; i++) {
                    Player player = (Player) weapon.getParent();
                    Bullet bullet = new Bullet("bulletMirv", 16, 16, "/grenadeLauncher_Bullet.png", 4, 0.1f, i, weapon);
                    bullet.getObjImage().changeColor(player.getSkinColors()[1], player.getSkinColors()[player.getSkIndex()]);
                    GameManager.objects.add(bullet);
                    int offsetX = 0;
                    int offsetY = 0;
                    switch (i)
                    {
                        case 0:
                            offsetY = 18;
                            break;
                        case 1:
                            offsetY = 16;
                            offsetX = 16;
                            break;
                        case 2:
                            offsetX = 18;
                            break;
                        case 3:
                            offsetY = -16;
                            offsetX = 16;
                            break;
                        case 4:
                            offsetY = -18;
                            break;
                        case 5:
                            offsetY = -16;
                            offsetX = -16;
                            break;
                        case 6:
                            offsetX = -18;
                            break;
                        case 7:
                            offsetY = 16;
                            offsetX = -16;
                            break;
                    }
                    bullet.setPosition(this.getPositionX() + offsetX, this.getPositionY() + offsetY);
                    //bullet.setPlantTime(2.0f);
                }
                split++;
                splitF = true;
            }
            if(split == 1)
            {
                splitBuffer -= dt;
                if(splitBuffer <= 0)
                {
                    splitF = false;
                }
            }
            if(split == 2)
            {
                endBuffer -= dt;
                if(endBuffer <= 0)
                {
                    explode(false);
                }
            }
        }
    }

    public void stun()
    {
        float tempPosX = this.position.x;
        float tempPosY = this.position.y;
        //weapon.bullets.remove(this);
        GameManager.objects.remove(this);

        //weapon.setExploding(false);
        Explosion explosion = new Explosion("stun", 90, 82, "/smokeExplosion.png", 20, 0.03f, weapon);
        explosion.isStun = true;
        explosion.setPosition(tempPosX, tempPosY);
        GameManager.objects.add(explosion);
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
        Explosion explosion = new Explosion("explosion", weapon.getExplosionWidth(), weapon.getExplosionHeight(), weapon.getExplosionPath(), weapon.getExplosionFrames(), 0.03f, weapon);
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
        if(!weapon.isTriggered)
        {
            if (other.getTag().equalsIgnoreCase("Wall"))
            {
                AABBComponent myC = (AABBComponent) this.findComponentBySubtag("bullet");
                AABBComponent otherC = (AABBComponent) other.findComponentBySubtag("wall");
                //System.out.println(Math.abs(myC.getLastCenterX() - otherC.getLastCenterX()) + " < " + (myC.getHalfWidth() + otherC.getHalfWidth()));
                if (Math.abs(myC.getLastCenterX() - otherC.getLastCenterX()) < (myC.getHalfWidth() + otherC.getHalfWidth()) - 2) {
                    //Top/bottom collision because X values are closer than Y
                    //Top
                    if (myC.getCenterY() < otherC.getCenterY()) {
                        collidingTop = true;
                    }

                    //Bottom
                    if (myC.getCenterY() > otherC.getCenterY()) {
                        collidingBottom = true;
                    }
                } else {
                    //Side collision bc vice versa
                    //Left
                    if (myC.getCenterX() < otherC.getCenterX()) {
                        collidingRight = true;
                    }

                    //Right
                    if (myC.getCenterX() > otherC.getCenterX()) {
                        collidingLeft = true;
                    }
                }
                if (weapon.isBounces() && !isAlt) {
                    if (!colliding && !slowing) {
                        switch (direction) {
                            case 0:
                                direction = 4;
                                break;
                            case 1:
                                if (collidingTop) {
                                    direction = 3;
                                }
                                if (collidingLeft || collidingRight) {
                                    direction = 7;
                                }
                                break;
                            case 2:
                                direction = 6;
                                break;
                            case 3:
                                if (collidingBottom) {
                                    direction = 1;
                                }
                                if (collidingLeft || collidingRight) {
                                    direction = 5;
                                }
                                break;
                            case 4:
                                direction = 0;
                                break;
                            case 5:
                                if (collidingBottom) {
                                    direction = 7;
                                }
                                if (collidingLeft || collidingRight) {
                                    direction = 3;
                                }
                                break;
                            case 6:
                                direction = 2;
                                break;
                            case 7:
                                if (collidingTop) {
                                    direction = 5;
                                }
                                if (collidingLeft || collidingRight) {
                                    direction = 1;
                                }
                                break;
                        }

                        colliding = true;
                        speed += weapon.getSpeedOnBounce();
                        tempBounces++;
                        if (tempBounces == 1) {
                            speed += (weapon.getSpeedOnBounce() * 144);
                        }
                        if (tempBounces == 2) {
                            speed -= (weapon.getSpeedOnBounce() * 145);
                            slowing = true;
                            tempSlowBuffer = slowBuffer;
                        }
                    }


                    if (weapon.getBounceCount() > 0) {
                        if (tempBounces == weapon.getBounceCount()) {
                            if (weapon.isExplodes()) {
                                weapon.bullets.remove(this);
                                explode(false);
                            } else {
                                weapon.bullets.remove(this);
                                GameManager.objects.remove(this);
                            }
                        }
                    }
                } else if (!weapon.isStopsAtWall()) {
                    if (isAlt && weapon.getSubTag() == "cannon") {
                        stun();
                    } else if (weapon.isExplodes()) {
                        explode(false);
                        weapon.bullets.remove(this);
                    } else {
                        GameManager.objects.remove(this);
                    }
                } else {
                    this.speed = 0;
                    this.stop();
                }
            }
            else if (other.getTag().equalsIgnoreCase("Bullet"))
            {
                Bullet otherB = (Bullet) other;
                if (weapon.isBounces() && otherB.weapon.isBounces())
                {
                    if (!colliding)
                    {
                        switch (direction) {
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
                }
                else if (weapon.isExplodes() && otherB.getWeapon().isCollides() && weapon.isCollides() && !isAlt)
                {
                    explode(true);
                    //weapon.setExploding(false);
                }
                else if (otherB.weapon.getSubTag() == weapon.getSubTag())
                {
                    if (weapon.isStopsAtWall())
                    {
                        this.speed = 0;
                        this.stop();
                        ((Bullet) other).setSpeed(0);
                        other.stop();
                    }
                }
            } else if (other.getTag().equalsIgnoreCase("Explosion")) {
                if (weapon.isChained() && !counting) {
                    counting = true;
                }
            } else if (other.getTag().equalsIgnoreCase("Player")) {
                Player player = (Player) other;
                Player ownerP = (Player) owner;
                if (player.getPlayerNumber() != weapon.getPlayerNumber()) {
                    if (player.isGoose())
                        player.depleteTime(weapon.getDamage());
                    else if (ownerP.isGoose()) {
                        player.setGoose(true);
                        player.changeSpecies();
                        ownerP.setGoose(false);
                        ownerP.changeSpecies();
                    }

                    if (isAlt && weapon.getSubTag() == "cannon") {
                        stun();
                    } else if (weapon.isExplodes()) {
                        explode(true);
                        //weapon.setExploding(false);
                    }
                }
            }
        }
        else
            {
                if(other.getTag().equalsIgnoreCase("Trigger"))
                {
                    if ((position.x <= triggerPoint.x + 6 && position.x >= triggerPoint.x - 6) && (position.y <= triggerPoint.y + 6 && position.y >= triggerPoint.y - 6))
                    {
                        if (weapon.isExplodes())
                        {
                            explode(false);
                        }
                        GameManager.objects.remove(other);
                    }
                }
            }
    }
}
