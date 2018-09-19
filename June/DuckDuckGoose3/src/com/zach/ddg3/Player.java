package com.zach.ddg3;

import com.ivan.xinput.XInputAxesDelta;
import com.ivan.xinput.XInputButtonsDelta;
import com.ivan.xinput.XInputComponentsDelta;
import com.ivan.xinput.XInputDevice;
import com.ivan.xinput.enums.XInputAxis;
import com.ivan.xinput.enums.XInputButton;
import edu.digipen.Game;
import edu.digipen.GameObject;
import edu.digipen.ObjectManager;
import edu.digipen.math.PFRandom;

import java.util.ArrayList;

import static com.zach.ddg3.FightLevel.players;
import static com.zach.ddg3.newCharChoose.*;

/**
 * Created by Zach on 2/16/2018.
 */
public class Player extends GameObject {
    XInputDevice device;
    private XInputButton shoot;
    private XInputButton start;

    XInputComponentsDelta delta;
    XInputButtonsDelta buttons;
    XInputAxesDelta axes;

    public float getLeftStickX() {
        return leftStickX;
    }

    public void setLeftStickX(float leftStickX) {
        this.leftStickX = leftStickX;
    }

    public float getLeftStickY() {
        return leftStickY;
    }

    public void setLeftStickY(float leftStickY) {
        this.leftStickY = leftStickY;
    }

    public float getRightStickX() {
        return rightStickX;
    }

    public void setRightStickX(float rightStickX) {
        this.rightStickX = rightStickX;
    }

    public float getRightStickY() {
        return rightStickY;
    }

    public void setRightStickY(float rightStickY) {
        this.rightStickY = rightStickY;
    }

    public float leftStickX;
    public float leftStickY;
    public float rightStickX;
    public float rightStickY;

    float leftTrigger;
    float rightTrigger;

    Bullet bullets;
    Weapon weapon;

    boolean shot;
    boolean firstFire;
    float cooldown;
    boolean moved;

    boolean exploded;
    boolean still = true;

    public static boolean hit;
    public static int time;
    public boolean walled;
    public GameObject reflector;
    public boolean reflected;
    public boolean bulletRef;

    public GameObject dasher;
    public boolean dashing;
    public float speed;

    public static float accumTime = 60;

    float second = 2.0f;

    XInputButton playerButtons[] = new XInputButton[6];

    Weapon playerWeapon;
    int timer;
    Breed pBreed;
    boolean collided;
    boolean charging;
    float chargeTimer;
    public int playerNum;

    GameObject fade;
    boolean fading;

    public boolean fullCharge;
    public GameObject fullChargeAnim;
    public boolean chargeAnim;
    
    public int inLevel;

    public static int currLaser;
    public static GameObject[] laserArray = new GameObject[100];
    //GameObject explosion;

    public String getSpecies() {
        return Species;
    }

    public void setSpecies(String species) {
        Species = species;
    }

    public String Species;

    public boolean atProtoStand;
    public boolean atExploStand;
    public boolean pointing;
    public int[] pointerBounds = new int[3];

    public boolean ready;
    public GameObject readyUpper;

    public Player(String name_, int width_, int height_, String textureName_, int totalFrames, int numberOfRows, int numberOfCols,
                  float frameLifetime, XInputDevice pDevice, Weapon playerWeapon, Breed pBreed)
    {
        super(name_, width_, height_, textureName_, totalFrames, numberOfRows, numberOfCols, frameLifetime);
        device = pDevice;

        delta = device.getDelta();
        buttons = delta.getButtons();
        axes = delta.getAxes();

        speed = 7;

        weapon = playerWeapon;
        this.setCircleCollider(100);
    }

    public void setWeapon(Weapon newWeapon) {
        this.playerWeapon = newWeapon;
    }

    public Weapon getWeapon() {
        return playerWeapon;
    }

    @Override
    public void update(float dt) {
        controlManager();
        if (exploded) {
            ArrayList<GameObject> explosion = new ArrayList<GameObject>();
            if (this == players[1]) {
                explosion = ObjectManager.getGameObjectsByName("explosion1");
            }
            if (this == players[2]) {
                explosion = ObjectManager.getGameObjectsByName("explosion2");
            }
            for (int i = 0; i < explosion.size(); i++) {
                if (explosion.get(i) != null && explosion.get(i).animationData.getCurrentFrame() == 5) {
                    explosion.get(i).setCircleCollider(0);
                }
                if (explosion.get(i) != null && explosion.get(i).animationData.getCurrentFrame() == 24) {
                    explosion.get(i).kill();
                    hit = false;
                }
            }
            for (int i = 0; i < explosion.size(); i++) {
                if (explosion.get(explosion.size() - 1).isDead()) {
                    exploded = false;
                }
            }
        }
        if(fullChargeAnim != null)
        {
            fullChargeAnim.setPosition(this.getPositionX(), this.getPositionY() - 100);
            if(fullChargeAnim.animationData.getCurrentFrame() == 17)
            {
                fullChargeAnim.kill();
            }
        }
        if (reflected) {
            if (reflector.animationData.getCurrentFrame() == 19) {
                reflector.kill();
                reflected = false;
                this.setOpacity(1);
            }
        }
        if (shot) {
            cooldown -= dt;
        }
        if (cooldown <= 0 && shot) {
            shot = false;
            if (!hit) {
                if (weapon.getWeaponTag() == "rocketLauncher" || weapon.getWeaponTag() == "grenadeLauncher" || weapon.getWeaponTag() == "shotgun") {
                    this.animationData.goToFrame(this.animationData.getCurrentFrame() - 8);
                }
            }
            if (this.getSpecies() == "Duck_") {
                if (weapon.getWeaponTag() == "rocketLauncher" || weapon.getWeaponTag() == "grenadeLauncher" || weapon.getWeaponTag() == "shotgun") {
                    weapon.setAnimationSet(0);
                }
            }
            if (this.getSpecies() == "Goose_") {
                if (weapon.getWeaponTag() == "rocketLauncher" || weapon.getWeaponTag() == "grenadeLauncher" || weapon.getWeaponTag() == "shotgun") {
                    weapon.setAnimationSet(16);
                }
            }
            hit = false;
        }

        second -= dt;
        if (second <= 0) {
            if (this.getSpecies() == "Goose_") {
                if (weapon.getBullet().getPlayerNumber() == 1) {
                    FightLevel.p1timer.animationData.goToNextFrame();
                }
                if (weapon.getBullet().getPlayerNumber() == 2) {
                    FightLevel.p2timer.animationData.goToNextFrame();
                }
                time--;
                second = 1.0f;
            }
        }

        if (dasher != null && dashing) {
            this.setPosition(dasher.getPosition());
            if (dasher.getPositionX() <= -960 || dasher.getPositionX() >= 960 || dasher.getPositionY() <= -540 || dasher.getPositionY() >= 540) {
                dasher.kill();
                this.setOpacity(1);
                dashing = false;
            }
        }

        if (fade != null && fade.animationData.getCurrentFrame() == 51) {
            fade.kill();
        }
        if (charging) {
            chargeTimer += dt;
            if (speed > 0.1f) {
                speed -= dt;
            }
            if (chargeTimer >= 1) {
                if (weapon.getWeaponTag() == "alienDuckHead") {
                    weapon.getBullet().setAnimSet(3);
                    weapon.getBullet().setScale(1.25f, 1.25f);
                    weapon.getBullet().setColliderSize(25);
                }
            }
            if (chargeTimer >= 2) {
                if (weapon.getWeaponTag() == "alienDuckHead") {
                    weapon.getBullet().setAnimSet(6);
                    weapon.getBullet().setScale(2.5f, 2.5f);
                    weapon.getBullet().setColliderSize(50);
                }
            }
            if (chargeTimer >= 3)
            {
                if (weapon.getWeaponTag() == "alienDuckHead")
                {
                    weapon.getBullet().setAnimSet(9);
                    weapon.getBullet().setScale(3.5f, 3.5f);
                    weapon.getBullet().setColliderSize(100);
                    if(!fullCharge)
                    {
                        fullCharge = true;
                        fullChargeAnim = new GameObject("fullCharger", 104, 58, "fullCharge.png", 18, 1, 18, 0.05f);
                        fullChargeAnim.setPosition(this.getPositionX(), this.getPositionY() - 100);
                        fullChargeAnim.setZOrder(0);
                        fullChargeAnim.animationData.play();
                    }
                }
                if (weapon.getWeaponTag() == "laserMatrix" && !fullCharge)
                {
                    if (this.getSpecies() == "Duck_")
                    {
                        weapon.setAnimationSet(8);
                    }
                    if (this.getSpecies() == "Goose_")
                    {
                        weapon.setAnimationSet(24);
                    }
                    fullCharge = true;
                    fullChargeAnim = new GameObject("fullCharger", 104, 58, "fullCharge.png", 18, 1, 18, 0.05f);
                    fullChargeAnim.setPosition(this.getPositionX(), this.getPositionY() - 100);
                    fullChargeAnim.setZOrder(0);
                    fullChargeAnim.animationData.play();
                }
            }
        }
        if (dashing) {
            if (dasher.getPositionX() <  newCharChoose.boundaries[1] || dasher.getPositionX() >  newCharChoose.boundaries[0]
                    || dasher.getPositionY() <  newCharChoose.boundaries[3] || dasher.getPositionY() >  newCharChoose.boundaries[2]) {
                dashing = false;
                this.setOpacity(1);
                dasher.kill();
            }
        }
        if(ready)
        {
            if(readyUpper.animationData.getCurrentFrame() == 9)
            {
                readyUpper.animationData.stop();
            }
        }
    }

    public void controlManager()
    {
        if(device.poll())
        {
            leftStickX += axes.getDelta(XInputAxis.LEFT_THUMBSTICK_X);
            leftStickY += axes.getDelta(XInputAxis.LEFT_THUMBSTICK_Y);

            rightStickX += axes.getDelta(XInputAxis.RIGHT_THUMBSTICK_X);
            rightStickY += axes.getDelta(XInputAxis.RIGHT_THUMBSTICK_Y);

            if(!collided && !reflected && !dashing && !pointing && !ready)
            {
                move();
            }
            if(!reflected)
            {
                look();
            }

            if (buttons.isPressed(XInputButton.RIGHT_SHOULDER) && !shot && !reflected && inLevel != 1)
            {
                if(weapon.getWeaponTag() == "rocketLauncher" || weapon.getWeaponTag() == "grenadeLauncher" || weapon.getWeaponTag() == "shotgun" || weapon.getWeaponTag() == "cannon")
                {
                    shoot();
                }
                if(weapon.getWeaponTag() == "alienDuckHead")
                {
                    weapon.getBullet().setScale(1,1);
                    weapon.getBullet().setColliderSize(5);
                    charging = true;
                    if(this.getSpecies() == "Duck_")
                    {
                        weapon.setAnimationSet(8);
                    }
                    if(this.getSpecies() == "Goose_")
                    {
                        weapon.setAnimationSet(24);
                    }
                }
                if(weapon.getWeaponTag() == "laserMatrix")
                {
                    charging = true;
                }
            }
            if(buttons.isReleased(XInputButton.RIGHT_SHOULDER) && !shot && !reflected && charging)
            {
                if(weapon.getWeaponTag() == "alienDuckHead")
                {
                    charging = false;
                    chargeTimer = 0;
                    shoot();
                    speed = 7;
                    weapon.getBullet().setAnimSet(0);
                    fullCharge = false;
                }
                if(weapon.getWeaponTag() == "laserMatrix" && !fullCharge)
                {
                    chargeTimer = 0;
                    charging = false;
                    speed = 7;
                    fullCharge = false;
                }
                if(weapon.getWeaponTag() == "laserMatrix" && fullCharge)
                {
                    chargeTimer = 0;
                    charging = false;
                    shoot();
                    speed = 7;
                    fullCharge = false;
                }
                if(this.getSpecies() == "Duck_")
                {
                    weapon.setAnimationSet(0);
                }
                if(this.getSpecies() == "Goose_")
                {
                    weapon.setAnimationSet(16);
                }
            }
            if (buttons.isPressed(XInputButton.LEFT_SHOULDER))
            {
                if(weapon.getWeaponTag() == "rocketLauncher" || weapon.getWeaponTag() == "grenadeLauncher" || weapon.getWeaponTag() == "cannon")
                {
                    remoteDetonation();
                }
            }
            if(buttons.isPressed(XInputButton.Y))
            {
                //GameLevelManager.goToLevel(new FightLevel());
            }
            if(buttons.isPressed(XInputButton.X) && !reflected && inLevel != 1)
            {
                reflect();
            }
            if(buttons.isPressed(XInputButton.A) && !dashing && !reflected)
            {
                if(this.inLevel != 1 && !atProtoStand)
                {
                    dash();
                }
                if(this.inLevel == 1 && pointing)
                {
                    if(playerNum == 1)
                    {
                        newCharChoose.previewDuck(this, 1, device, pBreed, p1LeftX);
                    }
                    if(playerNum == 2)
                    {
                        newCharChoose.previewDuck(this, 2, device, pBreed, p2LeftX);
                    }
                    //GameLevelManager.goToLevel(new FightLevel());
                }
                if(this.inLevel == 1 && !pointing)
                {
                    if (atProtoStand)
                    {
                        atExploStand = false;
                        pointing = true;
                        pointerBounds[0] = 250;
                        pointerBounds[1] = 450;
                        pointerBounds[2] = 650;
                        if (playerNum == 1)
                        {
                            pointer1 = new GameObject("pointer", 39, 33, "pointer.png");
                            pointer1.setPosition(250, 400);
                            ObjectManager.addGameObject(pointer1);
                        }
                        if (playerNum == 2)
                        {
                            pointer2 = new GameObject("pointer", 39, 33, "pointer.png");
                            pointer2.setPosition(250, 450);
                            ObjectManager.addGameObject(pointer2);
                        }
                    }
                    if(atExploStand)
                    {
                        atProtoStand = false;
                        pointing = true;
                        pointerBounds[0] = -700;
                        pointerBounds[1] = -500;
                        pointerBounds[2] = -300;
                        if (playerNum == 1) {
                            pointer1 = new GameObject("pointer", 39, 33, "pointer.png");
                            pointer1.setPosition(-700, 400);
                            ObjectManager.addGameObject(pointer1);
                        }
                        if (playerNum == 2) {
                            pointer2 = new GameObject("pointer", 39, 33, "pointer.png");
                            pointer2.setPosition(-700, 450);
                            ObjectManager.addGameObject(pointer2);
                        }
                    }
                }
            }
            if(buttons.isPressed(XInputButton.B) && this.inLevel == 1)
            {
                if(pointing)
                {
                    pointing = false;
                    atProtoStand = false;
                    atExploStand = false;
                    if (playerNum == 1) {
                        pointer1.kill();
                    }
                    if (playerNum == 2) {
                        pointer2.kill();
                    }
                }
                if(ready)
                {
                    ready = false;
                    readyUpper.kill();
                    this.setOpacity(1);
                    if(playerNum == 1)
                    {
                        newCharChoose.player1.ready = false;
                    }
                    if(playerNum == 2)
                    {
                        newCharChoose.player2.ready = false;
                    }
                }
            }

            if(buttons.isPressed(XInputButton.START))
            {
                if(this.inLevel == 1)
                {
                    if(!pointing && weapon.getWeaponTag() != null)
                    {
                        readyUp();
                    }
                }
            }
        }
    }
    public void look()
    {
        //Checks right thumbstick and changes frame of animation if the position is not in the deadzones
        if (rightStickX > 0.4f) {
            //Left
            moved = true;
            this.animationData.goToFrame(6+ weapon.getAnimationSet());
        }
        if (rightStickX < -0.4f) {
            //Right
            moved = true;
            this.animationData.goToFrame(2+ weapon.getAnimationSet());
        }
        if (rightStickY > 0.4f) {
            //Down
            moved = true;
            this.animationData.goToFrame(0+ weapon.getAnimationSet());
        }
        if (rightStickY < -0.4f) {
            //Up
            moved = true;
            this.animationData.goToFrame(4+ weapon.getAnimationSet());
        }


        if (rightStickX > 0.4f && rightStickY < -0.4f) {
            //Left and Up
            moved = true;
            this.animationData.goToFrame(5+ weapon.getAnimationSet());
        }
        if (rightStickX > 0.4f && rightStickY > 0.4f) {
            //Left and Down
            moved = true;
            this.animationData.goToFrame(7+ weapon.getAnimationSet());
        }
        if (rightStickY > 0.4f && rightStickX < -0.4f) {
            //Right and Down
            moved = true;
            this.animationData.goToFrame(1+ weapon.getAnimationSet());
        }
        if (rightStickY < -0.4f && rightStickX < -0.4f) {
            //Right and Up
            moved = true;
            this.animationData.goToFrame(3+ weapon.getAnimationSet());
        }

        if(leftStickX < 0.4f && leftStickX > -0.4f && rightStickX < 0.4f && rightStickX > -0.4f)
        {
            still = true;
        }
    }
    public void move() {
            //Checks if position of thumbstick is not in deadzones and changes position accordingly
            //Also changes frame of animation if the right thumbstick is not being used
            if (leftStickX > 0.4f && this.getPositionX() > newCharChoose.boundaries[1])
            {
                this.setPosition(this.getPositionX() - speed, this.getPositionY());
                if (rightStickX < 0.4f && rightStickX > -0.4f) {
                    moved = true;
                    this.animationData.goToFrame(6 + weapon.getAnimationSet());
                }
            }
            if (leftStickX < -0.4f && this.getPositionX() <  newCharChoose.boundaries[0]) {
                this.setPosition(this.getPositionX() + speed, this.getPositionY());
                if (rightStickX < 0.4f && rightStickX > -0.4f) {
                    moved = true;
                    this.animationData.goToFrame(2 + weapon.getAnimationSet());
                }
            }

            if (leftStickY > 0.4f && this.getPositionY() >  newCharChoose.boundaries[3]) {
                this.setPosition(this.getPositionX(), this.getPositionY() - speed);
                if (rightStickY < 0.4f && rightStickY > -0.4f) {
                    moved = true;
                    this.animationData.goToFrame(0 + weapon.getAnimationSet());
                }
            }
            if (leftStickY < -0.4f && this.getPositionY() <  newCharChoose.boundaries[2]) {
                this.setPosition(this.getPositionX(), this.getPositionY() + speed);
                if (rightStickY < 0.4f && rightStickY > -0.4f) {
                    moved = true;
                    this.animationData.goToFrame(4 + weapon.getAnimationSet());
                }
            }

            //Changes frame of animation on the diagonals if the right thumbstick is not being used
            if (leftStickX > 0.4f && leftStickY < -0.4f)
            {
                if (rightStickX < 0.4f && rightStickX > -0.4f && rightStickY < 0.4f && rightStickY > -0.4f)
                {
                    moved = true;
                    this.animationData.goToFrame(5 + weapon.getAnimationSet());
                }
            }
            if (leftStickX > 0.4f && leftStickY > 0.4f) {
                if (rightStickX < 0.4f && rightStickX > -0.4f && rightStickY < 0.4f && rightStickY > -0.4f) {
                    moved = true;
                    this.animationData.goToFrame(7 + weapon.getAnimationSet());
                }
            }
            if (leftStickY > 0.4f && leftStickX < -0.4f) {
                if (rightStickX < 0.4f && rightStickX > -0.4f && rightStickY < 0.4f && rightStickY > -0.4f) {
                    moved = true;
                    this.animationData.goToFrame(1 + weapon.getAnimationSet());
                }
            }
            if (leftStickY < -0.4f && leftStickX < -0.4f) {
                if (rightStickX < 0.4f && rightStickX > -0.4f && rightStickY < 0.4f && rightStickY > -0.4f) {
                    moved = true;
                    this.animationData.goToFrame(3 + weapon.getAnimationSet());
                }
            }
        }
    public void shoot() {
        float rotation = 0;
        float offsetX = 0;
        float offsetY = 0;
        shot = true;
        firstFire = true;
        cooldown = weapon.getCooldown();

        if (this.animationData.getCurrentFrame() == 0 + weapon.getAnimationSet()) {
            rotation = -90;
        }
        if (this.animationData.getCurrentFrame() == 1+ weapon.getAnimationSet()) {
            rotation = -45;
        }
        if (this.animationData.getCurrentFrame() == 2+ weapon.getAnimationSet())
        {
            rotation = 0;
        }
        if (this.animationData.getCurrentFrame() == 3+ weapon.getAnimationSet())
        {
            rotation = 45;
        }
        if (this.animationData.getCurrentFrame() == 4+ weapon.getAnimationSet()) {
            rotation = 90;
        }
        if (this.animationData.getCurrentFrame() == 5+ weapon.getAnimationSet()) {
            rotation = 135;
        }
        if (this.animationData.getCurrentFrame() == 6+ weapon.getAnimationSet()) {
            rotation = 180;
        }
        if (this.animationData.getCurrentFrame() == 7+ weapon.getAnimationSet()) {
            rotation = 225;
        }

        if(weapon.getBullet().getBulletTag() != "shotgun")
        {
            bullets = new Bullet(rotation,
                    weapon.getBullet().getSpeed(),
                    "bullets" + weapon.getBullet().getPlayerNumber(),
                    weapon.getBullet().getWidth(),
                    weapon.getBullet().getHeight(),
                    weapon.getBullet().getBulletTag() + "_Bullet.png"
                    , weapon.getBullet().getTotalFrames(), 1, weapon.getBullet().getTotalFrames(), 0.1f);
            ObjectManager.addGameObject(bullets);
            bullets.setPosition(this.getPositionX() + offsetX, this.getPositionY() + offsetY);
            bullets.setRotation(rotation);
            bullets.setCircleCollider(weapon.getBullet().getColliderSize());
            bullets.setBulletTag(weapon.getWeaponTag());
            bullets.setAnimSet(weapon.getBullet().getAnimSet());
        }
        if(weapon.getBullet().getBulletTag() == "shotgun")
        {
            for(int i = 0; i < 2; i++)
            {
                int rand = PFRandom.randomRange(1, 20);
                bullets = new Bullet(
                        rotation + rand + (i * 5),
                        weapon.getBullet().getSpeed(),
                        "bullets" + weapon.getBullet().getPlayerNumber(),
                        weapon.getBullet().getWidth(),
                        weapon.getBullet().getHeight(),
                        weapon.getBullet().getBulletTag() + "_Bullet.png"
                        , weapon.getBullet().getTotalFrames(), 1, weapon.getBullet().getTotalFrames(), 0.1f);
                ObjectManager.addGameObject(bullets);
                bullets.setPosition(this.getPositionX() + offsetX, this.getPositionY() + offsetY);
                bullets.setRotation(rotation + rand + (i * 5));
                bullets.setCircleCollider(weapon.getBullet().getColliderSize());
                bullets.setBulletTag(weapon.getWeaponTag());
                bullets.setAnimSet(weapon.getBullet().getAnimSet());
                bullets.animationData.goToFrame(1);

                bullets = new Bullet(rotation + -rand - (i * -5),
                        weapon.getBullet().getSpeed(),
                        "bullets" + weapon.getBullet().getPlayerNumber(),
                        weapon.getBullet().getWidth(),
                        weapon.getBullet().getHeight(),
                        weapon.getBullet().getBulletTag() + "_Bullet.png"
                        , weapon.getBullet().getTotalFrames(), 1, weapon.getBullet().getTotalFrames(), 0.1f);
                ObjectManager.addGameObject(bullets);
                bullets.setPosition(this.getPositionX() + offsetX, this.getPositionY() + offsetY);
                bullets.setRotation(rotation + -rand + (i * -5));
                bullets.setCircleCollider(weapon.getBullet().getColliderSize());
                bullets.setBulletTag(weapon.getWeaponTag());
                bullets.setAnimSet(weapon.getBullet().getAnimSet());
                bullets.animationData.goToFrame(1);
            }
        }
        if(weapon.getBullet().getBulletTag() == "grenadeLauncher" || weapon.getBullet().getBulletTag() == "alienDuckHead")
        {
            bullets.animationData.goToAndPlay(bullets.animSet);
        }
        bullets.setScale(weapon.getBullet().getScale());

        if(this.getSpecies() == "Duck_")
        {
            if(weapon.getWeaponTag() == "rocketLauncher" || weapon.getWeaponTag() == "grenadeLauncher" || weapon.getWeaponTag() == "shotgun")
            {
                weapon.setAnimationSet(8);
            }
        }
        if(this.getSpecies() == "Goose_")
        {
            if(weapon.getWeaponTag() == "rocketLauncher" || weapon.getWeaponTag() == "grenadeLauncher" || weapon.getWeaponTag() == "shotgun")
            {
                weapon.setAnimationSet(24);
            }
        }
        if(weapon.getWeaponTag() == "rocketLauncher" || weapon.getWeaponTag() == "grenadeLauncher" || weapon.getWeaponTag() == "shotgun")
        {
            this.animationData.goToFrame(this.animationData.getCurrentFrame() + 8);
        }

        if(dashing)
        {
            dashing = false;
            this.setOpacity(1);
            dasher.kill();
        }
    }


    public void remoteDetonation()
    {
        if (weapon.getWeaponTag() == "rocketLauncher")
        {
            if (firstFire)
            {
                if (bullets.isInViewport() && !exploded)
                {
                    float posX;
                    float posY;

                    posX = bullets.getPositionX();
                    posY = bullets.getPositionY();
                    bullets.kill();

                    GameObject explosion = new GameObject("explosion" + weapon.getBullet().getPlayerNumber(), 135, 123, "explosion.png", 25, 1, 25, 0.025f);
                    ObjectManager.addGameObject(explosion);
                    explosion.setPosition(posX, posY);
                    explosion.setScale(2, 2);
                    explosion.setCircleCollider(150);
                    explosion.animationData.play();
                    exploded = true;
                    cooldown = 0;
                }
            }
        }
        if(weapon.getWeaponTag() == "grenadeLauncher" || weapon.getWeaponTag() == "cannon")
        {
            ArrayList<GameObject> allBullets = new ArrayList<GameObject>();
            if(this == players[1])
            {
                allBullets = ObjectManager.getGameObjectsByName("bullets1");
            }
            if(this == players[2])
            {
                allBullets = ObjectManager.getGameObjectsByName("bullets2");
            }
            for(int i = 0; i < allBullets.size(); i++)
            {
                if (firstFire)
                {
                    if (allBullets.get(i).isInViewport() && allBullets.get(i).isDead() == false)
                    {
                        allBullets.get(i).kill();
                        GameObject explosion = new GameObject("explosion" + weapon.getBullet().getPlayerNumber(), 135, 123, "explosion.png", 25, 1, 25, 0.025f);
                        ObjectManager.addGameObject(explosion);
                        explosion.setPosition(allBullets.get(i).getPosition());
                        explosion.setScale(1.5f, 1.5f);
                        explosion.setCircleCollider(100);
                        explosion.animationData.play();
                        exploded = true;
                        cooldown = 0;
                    }
                }
            }
        }
    }
    public void reflect()
    {
        if(!reflected)
        {
            if (this.getSpecies() == "Duck_")
            {
                reflector = new GameObject("reflector", 135, 177, "Duck_reflect.png", 20, 1, 20, 0.03f);
            }
            if (this.getSpecies() == "Goose_")
            {
                reflector = new GameObject("reflector", 138, 231, "Goose_reflect.png", 20, 1, 20, 0.03f);
            }
            reflector.setPosition(this.getPosition());
            this.setOpacity(0);
            reflector.animationData.play();
            reflected = true;
            if(dashing)
            {
                dashing = false;
                dasher.kill();
            }
            if(this == players[1])
            {
                players[2].bulletRef = false;
            }
            if(this == players[2])
            {
                players[1].bulletRef = false;
            }
        }
    }
    public void dash()
    {
        float rotation = 0;
        if (this.animationData.getCurrentFrame() == 0 + weapon.getAnimationSet()) {
            rotation = -90;
        }
        if (this.animationData.getCurrentFrame() == 1+ weapon.getAnimationSet()) {
            rotation = -45;
        }
        if (this.animationData.getCurrentFrame() == 2+ weapon.getAnimationSet())
        {
            rotation = 0;
        }
        if (this.animationData.getCurrentFrame() == 3+ weapon.getAnimationSet()) {
            rotation = 45;
        }
        if (this.animationData.getCurrentFrame() == 4+ weapon.getAnimationSet()) {
            rotation = 90;
        }
        if (this.animationData.getCurrentFrame() == 5+ weapon.getAnimationSet()) {
            rotation = 125;
        }
        if (this.animationData.getCurrentFrame() == 6+ weapon.getAnimationSet()) {
            rotation = 180;
        }
        if (this.animationData.getCurrentFrame() == 7+ weapon.getAnimationSet()) {
            rotation = 225;
        }

        dasher = new Dasher(rotation, "dasher", 243, 276, "dash.png", 16, 1, 16, 0.1f);
        if(this.getSpecies() == "Duck_")
        {
            if (this.animationData.getCurrentFrame() >= 0 && this.animationData.getCurrentFrame() <= 7 ) {
                dasher.animationData.goToFrame(this.animationData.getCurrentFrame());
            }
            if (this.animationData.getCurrentFrame() >= 8 && this.animationData.getCurrentFrame() <= 15) {
                dasher.animationData.goToFrame(this.animationData.getCurrentFrame() - 8);
            }
        }
        if(this.getSpecies() == "Goose_")
        {
            if (this.animationData.getCurrentFrame() >= 16 && this.animationData.getCurrentFrame() <= 23 ) {
                dasher.animationData.goToFrame(this.animationData.getCurrentFrame() - weapon.getAnimationSet() + 8);
            }
            if (this.animationData.getCurrentFrame() >= 24 && this.animationData.getCurrentFrame() <= 31) {
                dasher.animationData.goToFrame(this.animationData.getCurrentFrame());
            }
        }

        ObjectManager.addGameObject(dasher);
        dasher.setPosition(this.getPosition());
        this.setOpacity(0);
        dashing = true;
    }

    public void readyUp()
    {
        if(!ready)
        {
            readyUpper = new GameObject("readyUpper", 138, 177, "readyAnim.png", 10, 1, 10, 0.03f);

            readyUpper.setPosition(this.getPosition());
            this.setOpacity(0);
            readyUpper.animationData.play();
            ready = true;
            if(playerNum == 1)
            {
                newCharChoose.player1.ready = true;
            }
            if(playerNum == 2)
            {
                newCharChoose.player2.ready = true;
            }
        }
    }
    public void timeDeplete(int seconds)
    {
        if(this == players[1])
        {
            timer -= seconds;
            FightLevel.p1timer.animationData.goToFrame(FightLevel.p1timer.animationData.getCurrentFrame() + 2);
            FightLevel.p1bar.setPositionY(FightLevel.p1bar.getPositionY() - 2 * (Game.getWindowHeight() / 60));
            FightLevel.p1timer.setPositionY(FightLevel.p1timer.getPositionY() - 2 * (Game.getWindowHeight() / 60));
        }
        if(this == players[2])
        {
            timer -= seconds;
            FightLevel.p2timer.animationData.goToFrame(FightLevel.p2timer.animationData.getCurrentFrame() + 2);
            FightLevel.p2bar.setPositionY(FightLevel.p2bar.getPositionY() - 2 * (Game.getWindowHeight() / 60));
            FightLevel.p2timer.setPositionY(FightLevel.p2timer.getPositionY() - 2 * (Game.getWindowHeight() / 60));
        }
    }
    @Override
    public void collisionReaction(GameObject collidedWith)
    {
        if (!reflected && "bullets2".equals(collidedWith.getName()) || "explosion2".equals(collidedWith.getName()) && !hit)
        {
            if(dashing)
            {
                dashing = false;
                this.setOpacity(1);
                dasher.kill();
            }
                if (this.getSpecies() == "Duck_" && this != players[2]) {
                    hit = true;
                    players[2].hit = true;
                    this.setSpecies("Goose_");
                    players[2].setSpecies("Duck_");

                    players[2].remoteDetonation();
                    ObjectManager.removeAllObjectsByName("bullets1");
                    ObjectManager.removeAllObjectsByName("bullets2");
                    ObjectManager.removeAllObjectsByName("laserSegment");

                    weapon.setAnimationSet(16);
                    this.animationData.goToFrame(this.animationData.getCurrentFrame() + weapon.getAnimationSet());

                    players[2].weapon.setAnimationSet(0);
                    players[2].animationData.goToFrame(players[2].animationData.getCurrentFrame() + (players[2].weapon.getAnimationSet() - 16));
                    //FightLevel.players[2].setScale(FightLevel.players[2].getScale().getX() * 2,FightLevel.players[2].getScale().getY());
                }
                if (this.getSpecies() == "Goose_" && this != players[2] && !hit)
                {
                    if ("bullets2".equals(collidedWith.getName()))
                    {
                        players[2].remoteDetonation();
                        collidedWith.kill();
                        ObjectManager.removeAllObjectsByName("laserSegment");
                    }
                    hit = true;
                    timeDeplete(2);
                }
            }
            if(players[1] != null && players[1].bulletRef)
            {
                if (this.getSpecies() == "Goose_" && this == players[2] && !hit)
                {
                    collidedWith.kill();
                    ObjectManager.removeAllObjectsByName("laserSegment");
                    hit = true;
                    timeDeplete(2);
                    players[1].bulletRef = false;
                }
            }
                if (!reflected  && "bullets1".equals(collidedWith.getName()) || "explosion1".equals(collidedWith.getName()) && !hit)
                {
                    if(dashing)
                    {
                        dashing = false;
                        this.setOpacity(1);
                        dasher.kill();
                    }
                        if (this.getSpecies() == "Duck_" && this != players[1])
                        {
                            hit = true;
                            players[1].hit = true;
                            this.setSpecies("Goose_");
                            players[1].setSpecies("Duck_");

                            players[1].remoteDetonation();
                            ObjectManager.removeAllObjectsByName("bullets1");
                            ObjectManager.removeAllObjectsByName("bullets2");
                            ObjectManager.removeAllObjectsByName("laserSegment");

                            weapon.setAnimationSet(16);
                            this.animationData.goToFrame(this.animationData.getCurrentFrame() + weapon.getAnimationSet());

                            players[1].weapon.setAnimationSet(0);
                            players[1].animationData.goToFrame(players[1].animationData.getCurrentFrame() + (players[1].weapon.getAnimationSet() - 16));
                            //FightLevel.players[1].setScale(FightLevel.players[1].getScale().getX() * 2,FightLevel.players[1].getScale().getY());
                        }
                        if (this.getSpecies() == "Goose_" && this != players[1] && !hit)
                        {
                            if ("bullets1".equals(collidedWith.getName())) {
                                players[1].remoteDetonation();
                                collidedWith.kill();
                                ObjectManager.removeAllObjectsByName("laserSegment");
                            }
                            hit = true;
                            timeDeplete(2);
                        }
                    if(players[2] != null && players[2].bulletRef)
                    {
                        if (this.getSpecies() == "Goose_" && this == players[1] && !hit)
                        {
                            collidedWith.kill();
                            ObjectManager.removeAllObjectsByName("laserSegment");
                            hit = true;
                            timeDeplete(2);
                            players[2].bulletRef = false;
                        }
                    }
                }

                if("prototypeStand".equals(collidedWith.getName()))
                {
                    atProtoStand = true;
                    atExploStand = false;
                }
                if("explosiveStand".equals(collidedWith.getName()))
                {
                    atExploStand = true;
                    atProtoStand = false;
                }
    }
}