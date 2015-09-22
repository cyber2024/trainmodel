package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Russell Elfenbein on 9/21/2015.
 */
public class AssetLoader {
    public static Texture wagonT;
    public static Sprite wagonS;
    public static BitmapFont font;

    public static void load(){
        font = new BitmapFont();
        wagonT = new Texture("wagon.png");
        wagonT.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        wagonS = new Sprite(wagonT);
        wagonS.setScale(0.1f);
    }

    public static void dispose(){
        wagonT.dispose();
        font.dispose();
    }
}
