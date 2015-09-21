package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Russell Elfenbein on 9/21/2015.
 */
public class AssetLoader {
    public static Texture wagonT;

    public static void load(){
        wagonT = new Texture("wagon.png");
    }
}
