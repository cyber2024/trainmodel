package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Russell Elfenbein on 9/21/2015.
 */
public class HUD {

    private BitmapFont font;

    public HUD(){
        font = new BitmapFont();

    }

    public void draw(SpriteBatch batch, float delta){
        font.getData().setScale(10f,10f);
        font.setColor(Color.WHITE);
        font.draw(batch, "Testasdfsdfg", 0, 0);
    }
}
