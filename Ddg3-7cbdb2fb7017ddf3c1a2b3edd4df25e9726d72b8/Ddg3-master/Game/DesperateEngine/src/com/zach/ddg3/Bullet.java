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

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    private int direction;
    private Object owner;

    public void setOwner(Object owner) {
        this.owner = owner;
    }

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
    private float explosionBuffer = 0.01f;
    private float tempExplosionBuffer = explosionBuffer;
    private boolean exploding = false;
    private float xSpeed = 0;
    private float mirvTimer = 0.6f;
    private float tempMirvTimer = mirvTimer;
    public float explosionTimer = 0;
    private boolean missileDirChanged = false;

    public int getDirChanges() {
        return dirChanges;
    }

    public void setDirChanges(int dirChanges) {
        this.dirChanges = dirChanges;
    }

    private int dirChanges = 0;

    public float getDecelRate() {
        return decelRate;
    }

    public void setDecelRate(float decelRate) {
        this.decelRate = decelRate;
    }

    private float decelRate = 0;

    public float getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    private float ySpeed = 0;

    public float getPlantTime() {
        return plantTime;
    }

    public void setPlantTime(float plantTime) {
        this.plantTime = plantTime;
    }

    private float plantTime = 0;
    private float tempPlantTime = 0;

    private boolean fullyCharged = false;
    private int currMissileDir = 100;
    private float missileDirBuffer = 0.1f;
    private float tempMissileDirBuffer = missileDirBuffer;
    private float missileLife = 5f;

    public int getDirChangeNum() {
        return dirChangeNum;
}

    public void setDirChangeNum(int dirChangeNum) {
        this.dirChangeNum = dirChangeNum;
    }

    private int dirChangeNum = 0;

    public void setFullyCharged(boolean fullyCharged) {
        this.fullyCharged = fullyCharged;
    }

    public Bullet(String name, int width, int height, String path, int totalFrames, float frameLife, int direction, WeaponComponent weapon)
    {
        super(name, width, height, path, totalFrames, frameLife);
        this.tag = "Bullet";
        this.direction = direction;

        tempTimer = weapon.getTimer();
        this.weapon = weapon;
        this.owner = weapon.getParent();

        this.speed = weapon.getSpeed();

        if(this.getObjImage().getPath() == "/owlGuard.png")
        {
            fullyCharged = true;
        }
        if(decelRate == 0)
        {
            decelRate = weapon.getSlowRate();
        }
        if(weapon.isSlows() || decelRate > 0)
        {
            tempSlow = decelRate;
        }

        if(weapon.isAccelerates())
        {
            tempAccel = weapon.getAccelRate();
        }

        if((weapon.isHasDirection() && !isAlt) || fullyCharged)
        {
            this.setFrame(direction);
        }
        if(weapon.isAnimated())
        {
            System.out.println(fullyCharged);
            if(!fullyCharged)
            {
                if (direction >= 0 && direction <= 4)
                {
                    this.playInRange(0, this.getTotalFrames() - 1);
                } else
                    {
                    this.playReverseInRange(0, this.getTotalFrames() - 1);
                }
            }
            else
                {
                    playInRange(direction * 8, (direction * 8) + 8);
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
        if(weapon.isOstrich())
        {
            zIndex = 1;
        }
        this.offsetPos.x = (int) (this.position.x - (this.width / 2) + 320);
        this.offsetPos.y = (int) (this.position.y - (this.height / 2) + 180);
        this.updateComponents(main, gameManager, dt);
        this.animate(dt);

        if(!isAlt)
        {
            if(explosionTimer > 0)
            {
                if(speed <= 0)
                {
                    explosionTimer -= dt;
                    if(explosionTimer <= 0)
                    {
                        explode(false);
                    }
                }
            }
            /*if (weapon.isExploding())
            {
                explode(false);
                weapon.setExploding(false);
            }*/
            if (weapon.isPlanting())
            {
                plant();
                //weapon.setPlanting(false);
            }
            //System.out.println(this.position.y);

            if (weapon.isSlows() || tempSlow > 0)
            {
                if (this.speed <= 0 && weapon.isAnimated())
                {
                    this.stop();
                    this.speed = 0;
                }
                else
                    {
                    this.speed -= tempSlow;
                    //tempSlow += (dt / 25);
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

            if (weapon.getTimer() > 0)
            {
                if (tempTimer <= 0)
                {
                    if (weapon.isExplodes())
                    {
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
        if(weapon.isExploding())
        {
            tempExplosionBuffer -= dt / 100;
            if(tempExplosionBuffer <= 0)
            {
                weapon.setExploding(false);
                tempExplosionBuffer = explosionBuffer;
            }
        }
    }

    public void altInit(String tag)
    {
        switch (tag)
        {
            case "rocketLauncher":
                direction = 10;
                tempTimer = 8f;
                setFrameLife(1f);

                Player player = (Player) weapon.getParent();
                playInRange((player.getFrame() - player.getFrameOffset()) * 2, ((player.getFrame() - player.getFrameOffset()) * 2) + 2);
                //setFrame(player.getFrame() - player.getFrameOffset());
                break;
            case "grenadeLauncher":
                play();
                break;
            case "cannon":
                //playInRangeAndBack(0, 4);
                //play();
                playTo(0, 4);
                break;
            case "sniper":
                speed = 300f;
                break;
        }
    }

    public void alt(String tag, float dt)
    {
        switch (tag)
        {
            case "rocketLauncher":
                smartHomeIn(dt);
                break;
            case "grenadeLauncher":
                mirv(dt);
                break;
            case "cannon":
                //stun();
                break;
            case "sniper":
                int newDir = 0;
                switch(direction)
                {
                    case 2:
                        newDir = 1;
                        break;
                    case 4:
                        newDir = 2;
                        break;
                    case 6:
                        newDir = 3;
                        break;
                }
                playInRange(newDir * 2, (newDir * 2) + 2);
                break;
        }
    }
    public void leaveTrail()
    {
        int offsetX = 0;
        int offsetY = 0;
        switch(direction)
        {
            case 0:
                offsetY = -45;
                break;
            case 2:
                offsetX = -45;
                break;
            case 4:
                offsetY = 45;
                break;
            case 6:
                offsetX = 45;
                break;
        }

        int newDir = 0;
        switch(direction)
        {
            case 2:
                newDir = 1;
                break;
            case 4:
                newDir = 2;
                break;
            case 6:
                newDir = 3;
                break;
        }
        Object trail = new Object("trail", 95, 95, "/sniperUltTrail.png", 16, 0.15f);
        trail.setPosition(this.getPositionX() + offsetX, this.getPositionY() + offsetY);
        trail.playToAndDestroy(newDir * 4, (newDir * 4) + 4);
        GameManager.objects.add(trail);
    }

    public void smartHomeIn(float dt)
    {
        missileLife -= dt;
        if(missileLife <= 0)
        {
            explode(false);
        }
        Player player = (Player) weapon.getParent();
        Player target = null;

        if(target == null)
        {
            for (int i = 0; i < GameManager.players.size(); i++)
            {
                if (GameManager.players.get(i).getPlayerNumber() != player.getPlayerNumber())
                {
                    target = GameManager.players.get(i);
                }
            }
        }

        if(target != null)
        {
            double playerX = target.position.x;
            double playerY = target.position.y;
            double angle = Math.toDegrees(Math.atan2(playerY - this.getPositionY(), playerX - this.getPositionX()));
            System.out.println(angle);
            int newDir = 0;
                if ((angle < -150 && angle > -180) || (angle > 150 && angle < 180)) {
                    //Left
                    newDir = 6;
                }
                if ((angle > -30 && angle < 0) || (angle < 30 && angle > 0)) {
                    //Right
                    newDir = 2;
                }
                if ((angle > 60 && angle < 120)) {
                    //Down
                    newDir = 0;
                }
                if (angle < -60 && angle > -120) {
                    //Up
                    newDir = 4;
                }
                if (angle > -150 && angle < -120) {
                    //Left and Up
                    newDir = 5;
                }
                if (angle < 150 && angle > 120) {
                    //Left and Down
                    newDir = 7;
                }
                if (angle > 30 && angle < 60) {
                    //Right and Down
                    newDir = 1;
                }
                if (angle < -30 && angle > -60) {
                    //Right and Up
                    newDir = 3;
                }
                if(currMissileDir == 100)
                {
                    currMissileDir = (player.getFrame() - player.getFrameOffset()) * 2;
                    playInRange(currMissileDir, currMissileDir + 2);
                }
                if(newDir != currMissileDir)
                {
                    currMissileDir = newDir;
                    if(tempMissileDirBuffer <= 0)
                    {
                        playInRange(currMissileDir * 2, (currMissileDir * 2) + 2);
                        tempMissileDirBuffer = missileDirBuffer;
                    }
                } tempMissileDirBuffer -= dt;
            direction = currMissileDir;
            move(dt);
        }
        else
            {
            direction = player.getFrame();
            move(dt);
        }

        this.speed = 20f;
    }

    public void mirv(float dt)
    {
        /*if (this.speed <= 0 && weapon.isAnimated())
        {
            this.stop();
        }
        else
            {
            this.speed -= tempSlow;
            tempSlow += (dt / 2);
            this.setFrameLife(this.getFrameLife() + (dt / 30));
        }*/
        tempMirvTimer -= dt;

        if(tempMirvTimer <= 0)
        {
            for (int i = 0; i < 8; i++)
            {
                Player player = (Player) weapon.getParent();
                Bullet bullet = new Bullet("bulletMirv", 16, 16, "/grenadeLauncher_Bullet.png", 4, 0.1f, i, weapon);
                bullet.getObjImage().changeColor(player.getSkinColors()[1], player.getSkinColors()[player.getSkIndex()]);
                GameManager.objects.add(bullet);
                int offsetX = 0;
                int offsetY = 0;
                switch (i)
                {
                        case 0:
                            offsetY = 30;
                            bullet.tempSlow = 1.6f;
                            break;
                        case 1:
                            offsetY = 24;
                            offsetX = 24;
                            bullet.tempSlow = 1.6f;
                            break;
                        case 2:
                            offsetX = 30;
                            bullet.tempSlow = 1.6f;
                            break;
                        case 3:
                            offsetY = -24;
                            offsetX = 24;
                            bullet.tempSlow = 1.6f;
                            break;
                        case 4:
                            offsetY = -30;
                            bullet.tempSlow = 1.6f;
                            break;
                        case 5:
                            offsetY = -24;
                            offsetX = -24;
                            bullet.tempSlow = 1.6f;
                            break;
                        case 6:
                            offsetX = -30;
                            bullet.tempSlow = 1.6f;
                            break;
                        case 7:
                            offsetY = 24;
                            offsetX = -24;
                            bullet.tempSlow = 1.6f;
                            break;
                    }
                    bullet.explosionTimer = 0.5f;
                    bullet.speed = 160f;
                    bullet.direction = this.direction;
                    bullet.setPosition(this.getPositionX() + offsetX, this.getPositionY() + offsetY);
                    //bullet.setPlantTime(2.0f);
                }

                explode();
            }
                /*split++;
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
        }*/
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

    public void move(float dt) {
        //System.out.println(this.position.x + ", " + this.position.y);
        if (xSpeed == 0 && ySpeed == 0)
        {
            switch (direction) {
                case 0:
                    this.position.y += speed * dt;
                    this.zIndex = weapon.getParent().zIndex;
                    this.maxzIndex = weapon.getParent().maxzIndex;
                    break;
                case 1:
                    this.position.y += speed * dt;
                    this.position.x += speed * dt;
                    this.zIndex = weapon.getParent().zIndex;
                    this.maxzIndex = weapon.getParent().maxzIndex;
                    break;
                case 2:
                    this.position.x += speed * dt;
                    this.zIndex = weapon.getParent().zIndex - 1;
                    this.maxzIndex = weapon.getParent().maxzIndex - 1;
                    break;
                case 3:
                    this.position.y -= speed * dt;
                    this.position.x += speed * dt;
                    this.zIndex = weapon.getParent().zIndex - 1;
                    this.maxzIndex = weapon.getParent().maxzIndex - 1;
                    break;
                case 4:
                    this.position.y -= speed * dt;
                    this.zIndex = weapon.getParent().zIndex - 1;
                    this.maxzIndex = weapon.getParent().maxzIndex - 1;
                    break;
                case 5:
                    this.position.y -= speed * dt;
                    this.position.x -= speed * dt;
                    this.zIndex = weapon.getParent().zIndex - 1;
                    this.maxzIndex = weapon.getParent().maxzIndex - 1;
                    break;
                case 6:
                    this.position.x -= speed * dt;
                    this.zIndex = weapon.getParent().zIndex - 1;
                    this.maxzIndex = weapon.getParent().maxzIndex - 1;
                    break;
                case 7:
                    this.position.y += speed * dt;
                    this.position.x -= speed * dt;
                    this.zIndex = weapon.getParent().zIndex;
                    this.maxzIndex = weapon.getParent().maxzIndex;
            }
        }
        else
            {
                this.position.y += ySpeed * dt;
                this.position.x += xSpeed * dt;
            }
    }

    public void explode(boolean alreadyHit)
    {
            float tempPosX = this.position.x;
            float tempPosY = this.position.y;
            weapon.bullets.remove(this);
            GameManager.objects.remove(this);

            //exploding = true;
            weapon.setExploding(true);
            Explosion explosion = new Explosion("explosion", weapon.getExplosionWidth(), weapon.getExplosionHeight(), weapon.getExplosionPath(), weapon.getExplosionFrames(), 0.03f, weapon);
            explosion.setPosition(tempPosX, tempPosY);
            GameManager.objects.add(explosion);
            if (alreadyHit)
            {
                explosion.setDamage(0);
            }
    }

    public void explode()
    {
        float tempPosX = this.position.x;
        float tempPosY = this.position.y;
        weapon.bullets.remove(this);
        GameManager.objects.remove(this);

        //exploding = true;
        //weapon.setExploding(true);
        Object explosion = new Object("explosion", weapon.getExplosionWidth(), weapon.getExplosionHeight(), weapon.getExplosionPath(), weapon.getExplosionFrames(), 0.03f);
        explosion.setPosition(tempPosX, tempPosY);
        explosion.playToAndDestroy(0, 18);
        GameManager.objects.add(explosion);
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
            if ((other.getTag().equalsIgnoreCase("Wall") || other.getTag().equalsIgnoreCase("Pelican")) && other != this.owner)
            {
                AABBComponent myC = (AABBComponent) this.findComponentBySubtag("bullet");
                AABBComponent otherC = (AABBComponent) other.findComponentBySubtag("wall");
                //System.out.println(Math.abs(myC.getLastCenterX() - otherC.getLastCenterX()) + " < " + (myC.getHalfWidth() + otherC.getHalfWidth()));
                try {
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
                }
                catch(java.lang.NullPointerException e)
                {

                }
                if (weapon.isBounces() && !isAlt)
                {
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


                    if (weapon.getBounceCount() > 0)
                    {
                        if (tempBounces == weapon.getBounceCount())
                        {
                            if (weapon.isExplodes()) {
                                weapon.bullets.remove(this);
                                explode(false);
                            } else
                                {
                                weapon.bullets.remove(this);
                                GameManager.objects.remove(this);
                            }
                        }
                    }
                }
                else if (!weapon.isStopsAtWall() && !weapon.isOstrich())
                {
                    if (isAlt && weapon.getSubTag() == "cannon")
                    {
                        stun();
                    }
                    else if (weapon.isExplodes())
                    {
                        if(isAlt && weapon.getTag() == "sniper")
                        {
                            leaveTrail();
                            GameManager.objects.remove(this);
                        }
                        else
                            {
                            explode(false);
                            weapon.bullets.remove(this);
                        }
                    }
                    else
                        {
                            GameManager.objects.remove(this);
                        }
                }
                else if(!weapon.isOstrich())
                    {
                    this.speed = 0;
                    this.stop();
                }
            }
            else if (other.getTag().equalsIgnoreCase("Bullet") && !weapon.isOstrich())
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
                else if (weapon.isExplodes() && otherB.getWeapon().isCollides() && weapon.isCollides() && !isAlt && !weapon.isOstrich() && !otherB.isAlt)
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
            } else if (other.getTag().equalsIgnoreCase("Explosion") && !weapon.isOstrich()) {
                if (weapon.isChained() && !counting)
                {
                    counting = true;
                }
            }
            else if (other.getTag().equalsIgnoreCase("Player"))
            {
                Player player = (Player) other;

                if(weapon.isParentIsPlayer())
                {
                    Player ownerP = (Player) owner;
                    if (player.getPlayerNumber() != weapon.getPlayerNumber())
                    {
                        if (ownerP.isGoose() && !owner.isDead() && !player.isDead())
                        {
                            player.setGoose(true);
                            player.changeSpecies();
                            ownerP.setGoose(false);
                            ownerP.changeSpecies();
                        }
                        if(isAlt && weapon.getSubTag() == "sniper")
                        {
                            AABBComponent myC = (AABBComponent) this.findComponentBySubtag("bullet");
                            AABBComponent head = (AABBComponent) other.findComponentBySubtag("head");
                            /*AABBComponent body = (AABBComponent) other.findComponentBySubtag("body");
                            if (head != null || body != null)
                            {
                                if (Math.abs(myC.getLastCenterX() - head.getLastCenterX()) < (myC.getHalfWidth() + head.getHalfWidth()) - 2)
                                {
                                    if ((myC.getCenterY() < head.getCenterY()) || (myC.getCenterY() > head.getCenterY()) || (myC.getCenterX() < head.getCenterX()) || (myC.getCenterX() > head.getCenterX()))
                                    {
                                        player.stun();
                                        player.depleteTime(6 + (2 * dirChanges));
                                        explode(true);
                                    }
                                }*/
                            if (head.isHeadshot())
                            {
                                player.stun();
                                if(GameManager.gameLevelManager.getGameState() == GameLevelManager.GameState.MAIN_STATE)
                                    player.depleteTime(6 + (2 * dirChanges));

                                leaveTrail();
                                explode(true);
                                head.setHeadshot(false);
                            }
                                else
                                    {
                                        leaveTrail();
                                        explode(false);
                                    }
                        }
                        else
                            {
                            if (weapon.isExplodes())
                            {
                                if(GameManager.gameLevelManager.getGameState() == GameLevelManager.GameState.MAIN_STATE)
                                    player.depleteTime(weapon.getDamage());

                                explode(true);
                            }
                        }
                    }
                }
                else
                    {
                        if (weapon.isExplodes())
                        {
                            if(GameManager.gameLevelManager.getGameState() == GameLevelManager.GameState.MAIN_STATE)
                                player.depleteTime(weapon.getDamage());

                            explode(true);
                        }
                    }

                    if (isAlt && weapon.getSubTag() == "cannon")
                    {
                        stun();
                        return;
                    }
            }

            if(weapon.isOstrich())
            {
                if(other.getTag().equalsIgnoreCase("Trigger"))
                {
                    {
                        explode(false);
                    }
                }
            }
        }
        else
            {
                if(other.getTag().equalsIgnoreCase("Trigger"))
                {
                    if (((position.x <= triggerPoint.x + 6 && position.x >= triggerPoint.x - 6) && (position.y <= triggerPoint.y + 6 && position.y >= triggerPoint.y - 6)))
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
