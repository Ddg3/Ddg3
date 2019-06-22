package com.zach.ddg3;

import com.zach.ddg3.components.AABBComponent;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

public class HoleTile extends Wall {
    public HoleTile(String name, int width, int height, String path, int totalFrames, float frameLife, boolean isZUpdater)
    {
        super(name, width, height, path, totalFrames, frameLife, isZUpdater);
    }

    private float zUpdatePointHigh;
    private float zUpdatePointLow;

    public HoleTile()
    {
        super("Hole", 70, 70, "/holeTiles.png", 16, 0.0f, false);
        this.tag = "Hole";
        this.addComponent(new AABBComponent(this, "wall"));
        this.paddingTop = 0;
        this.paddingSide = 0;
    }

    public enum directions
    {
        SINGLE(0),
        LEFTof(1),
        RIGHTof(3),
        DOWNof(12),
        UPof(4),
        BETWEEN_HORIZof(2),
        BETWEEN_VERTof(8),
        ULCORNERof(5),
        LLCORNERof(13),
        URCORNERof(7),
        LRCORNERof(15);

        private int frame;

        public int getFrame() {
            return frame;
        }

        private directions(int frame)
        {
            this.frame = frame;
        }
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        super.update(main, gameManager, dt);
        this.updateComponents(main, gameManager, dt);
        this.animate(dt);
    }

    @Override
    public void render(Main main, Renderer r)
    {
        super.render(main, r);
        //r.drawFillRectangle((int)offsetPos.x, (int)offsetPos.y, width, height, color);
        //this.renderComponents(main, r);
    }

    @Override
    public void collision(Object other, Main main)
    {
        super.collision(other, main);
    }

    public void setWallFrom(WallTile.directions dir)
    {
        float posX = this.getPositionX();
        float posY = this.getPositionY();
        int zInd = this.zIndex;

        switch (dir) {
            case LEFTof:
                posX -= this.width;
                break;
            case RIGHTof:
                posX += this.width;
                break;
            case UPof:
                posY -= this.height;
                zInd -= 1;
                break;
            case DOWNof:
                posY += this.height / 2;
                zInd += 1;
                break;
        }

        /*WallTile wall = new WallTile(dir);
        wall.position = new Vector(posX, posY);
        wall.zIndex = zInd;
        wall.setFrame(dir.getFrame());
        GameManager.objects.add(wall);*/
    }

    public void setHoleFrom(HoleTile.directions dir)
    {
        float posX = this.getPositionX();
        float posY = this.getPositionY();

        switch (dir) {
            case LEFTof:
                posX -= this.width;
                break;
            case RIGHTof:
                posX += this.width;
                break;
            case UPof:
                posY -= this.height;
                break;
            case DOWNof:
                posY += this.height / 2;
                break;
            case URCORNERof:
                posX += this.width;
                break;
            case LLCORNERof:
                posY += this.height;
                break;
        }

        HoleTile hole = new HoleTile();
        hole.position = new Vector(posX, posY);
        hole.zIndex = this.zIndex;
        hole.setFrame(dir.getFrame());
        GameManager.objects.add(hole);
    }

    public void setHoleRowFrom(int size, boolean right, boolean ended)
    {
        float posX = this.position.x;
        float posY = this.position.y;

        for(int i = 1; i <= size; i++)
        {
            if(i < size)
            {
                HoleTile holeR = new HoleTile();
                if(right)
                {
                    holeR.position = new Vector(posX + (i * this.width), posY);
                }
                else
                    {
                        holeR.position = new Vector(posX - (i * this.width), posY);
                    }
                holeR.zIndex = this.zIndex;
                holeR.setFrame(2);
                GameManager.objects.add(holeR);
            }
            else if(ended)
                {
                    HoleTile holeR = new HoleTile();
                    if(right)
                    {
                        holeR.position = new Vector(posX + (i * this.width), posY);
                        holeR.setFrame(3);
                    }
                    else
                        {
                            holeR.position = new Vector(posX - (i * this.width), posY);
                            holeR.setFrame(1);
                        }
                    holeR.zIndex = this.zIndex;
                    GameManager.objects.add(holeR);
                }
        }
    }
    public void setHoleColumnFrom(int size, boolean down, boolean ended)
    {
        float posX = this.position.x;
        float posY = this.position.y;

        for(int i = 1; i <= size; i++)
        {
            if(i < size)
            {
                HoleTile holeR = new HoleTile();
                if(down)
                {
                    holeR.position = new Vector(posX, posY + (i * this.height));
                }
                else
                    {
                        holeR.position = new Vector(posX, posY - (i * this.height));
                    }
                holeR.zIndex = this.zIndex;
                holeR.setFrame(8);
                GameManager.objects.add(holeR);
            }
            else if(ended)
            {
                HoleTile holeR = new HoleTile();
                if(down)
                {
                    holeR.position = new Vector(posX, posY + (i * this.width));
                    holeR.setFrame(12);
                }
                else
                    {
                        holeR.position = new Vector(posX, posY - (i * this.width));
                        holeR.setFrame(4);
                    }
                holeR.zIndex = this.zIndex;
                GameManager.objects.add(holeR);
            }
        }
    }
}
