package com.zach.ddg3;

import com.zach.engine.AbstractGame;
import com.zach.engine.Main;
import com.zach.engine.Renderman;

/**
 * Created by Zach on 6/12/2018.
 */
public class GameLevelManager extends AbstractGame
{
    protected static GameState gameState;
    protected static GameLevel currLevel;
    private boolean switched;

    public enum GameState
    {
        TITLE_STATE,
        MAIN_STATE,
        SELECTION_STATE,
        ENDGAME_STATE
    }
    public GameLevelManager(GameManager gameManager)
    {
        gameState = (GameState.TITLE_STATE);
        currLevel = new titleLevel();
    }

    @Override
    public void init(Main main)
    {

    }

    @Override
    public void update(Main main, float dt)
    {
        /*switch (gameState)
        {
            case TITLE_STATE:
                currLevel = new titleLevel();
            case ENDGAME_STATE:
                currLevel = new endGameLevel();
            case MAIN_STATE:
                currLevel = new mainLevel();
            case SELECTION_STATE:
                currLevel = new selectionLevel();
        }*/

        //Checks for current level state and runs accordingly

        if(gameState == GameState.TITLE_STATE)
        {
            currLevel = new titleLevel();
        }
        if(gameState == GameState.MAIN_STATE)
        {
            currLevel = new mainLevel();
        }
        if(gameState == GameState.SELECTION_STATE)
        {
            currLevel = new selectionLevel();
        }
        if(gameState == GameState.ENDGAME_STATE)
        {
            currLevel = new endGameLevel();
        }
        if(!switched)
        {
            currLevel.init(main);
            switched = true;
        }
        currLevel.update(main, dt);
    }

    @Override
    public void render(Main main, Renderman r)
    {

    }

    public void setGameState(GameState state)
    {
        gameState = state;
        switched = false;
    }
}
