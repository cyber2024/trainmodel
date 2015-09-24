package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by Russell Elfenbein on 9/24/2015.
 */
public class Train3 {

    BitmapFont font = new BitmapFont();
    private static float posY, springyness = 0.1f;
    private float drawBarLength = 1f,
            minDist = 0.5f*drawBarLength,
            maxDist = drawBarLength;

    private int numWagons = 250;
    private ArrayList<Wagon2> wagons;

    public Train3(){
        wagons = new ArrayList<Wagon2>();
        for(int i = 0; i < numWagons; i++){
            wagons.add(new Wagon2());
            wagons.get(i).position.set(-(i * wagons.get(i).size.x + minDist * i * 1.25f), 15f);
        }
    }

    public void update(float delta){
        Gdx.app.log("FPS:", 1 / delta + "");
        for(int i = 0; i < wagons.size(); i++) {
            Wagon2 wagon = wagons.get(i);
            updateAcceleration(delta, i, wagon);
            updateMomentum(delta, i, wagon);
            updatePosition(delta, i, wagon);
            collisionUpdate(i);
        }
    }
    public void updateForces(float delta, int index, Wagon2 wagon){
        wagon = wagons.get(index);


    }
    public void updateAcceleration(float delta, int index, Wagon2 wagon){
        wagon = wagons.get(index);
        wagon.acceleration.x = wagon.forceResultant.x/wagon.mass;

    }
    /*public void updateVelocity(float delta, int index, Wagon2 wagon){

    }*/
    public void updateMomentum(float delta, int index, Wagon2 wagon){
        wagon.momentum = wagon.momentum + wagon.acceleration.x*delta*wagon.mass;
        wagon.velocity.x = wagon.momentum/wagon.mass;
    }
    public void updatePosition(float delta, int index, Wagon2 wagon){
        wagon.position.x += wagon.velocity.x*delta;
    }

    public void collisionUpdate(int index){
        if(index < wagons.size()-1) {
            Wagon2 wagon, nextWagon;
            wagon = wagons.get(index);
            nextWagon = wagons.get(index + 1);
            if (wagon.position.x < nextWagon.position.x + wagon.size.x + minDist) {
                wagon.rearSpacing = wagon.position.x - nextWagon.position.x - wagon.size.x;
                float springX = wagon.rearSpacing/minDist;
                float tmpMomentum = (wagon.momentum + nextWagon.momentum) * 0.5f;
                wagon.momentum = tmpMomentum;
                nextWagon.momentum = tmpMomentum;
               // wagon.forceResultant.x += wagon.rearSpacing*100;
                //recalculateAheadCollisions(index);
            } else if (wagon.position.x > nextWagon.position.x + wagon.size.x + maxDist) {
                wagon.rearSpacing = wagon.position.x - nextWagon.position.x - wagon.size.x;
                float springX = (wagon.rearSpacing - maxDist);
                //wagon.rearSpacing = maxDist;
                //wagon.position.x = nextWagon.position.x + wagon.size.x + maxDist;
                float tmpMomentum = (wagon.momentum + nextWagon.momentum) * 0.5f;
                wagon.momentum = tmpMomentum;
                nextWagon.momentum = tmpMomentum;
                //wagon.forceResultant.x += wagon.rearSpacing*100;
            } else {
                wagon.forceResultant.x = 0;
            }
        }
    }

    public void recalculateAheadCollisions(int index){
        Wagon2 wagon = wagons.get(index);
        while (index > 0){
            index --;
            Wagon2 prevWagon = wagons.get(index);
            prevWagon.rearSpacing = prevWagon.position.x - wagon.position.x;
            if(prevWagon.rearSpacing < minDist){
                wagon.position.x = prevWagon.position.x - wagon.size.x-minDist;
                wagon.momentum = (wagon.momentum + prevWagon.momentum)*0.5f;
                prevWagon.momentum = wagon.momentum;
            }
            /*if(prevWagon.rearSpacing > maxDist){
                wagon.position.x = prevWagon.position.x - wagon.size.x-maxDist;
                wagon.momentum = (wagon.momentum + prevWagon.momentum)*0.5f;
                prevWagon.momentum = wagon.momentum;
            }*/
        }
    }


    public void draw(SpriteBatch batch){
        font.setColor(1,0,0,1);
        font.getData().setScale(0.1f,0.1f);
        for (Wagon2 wagon : wagons) {
            AssetLoader.wagonS.setPosition(wagon.position.x, wagon.position.y);

            float x = Math.abs(wagon.velocity.x);
            x = 1+x*0.01f;
            if(x<1)
                x=1f;

            if(wagon.velocity.x >= 0)
                AssetLoader.wagonS.setColor(1f/x, 1f, 1f/x, 1f);
            if(wagon.velocity.x < 0)
                AssetLoader.wagonS.setColor(1f, 1f/x, 1f/x, 1f);
            AssetLoader.wagonS.draw(batch);

            font.draw(batch,"Vel: "+Math.round(wagon.velocity.x), wagon.position.x, wagon.position
                    .y+2f);
        }
    }

    public boolean stop(int i){
        if(i<wagons.size()) {
            //wagons.get(i).velocity.set(0, 0);
            wagons.get(i).forceResultant.x -= 1000000;
            return true;
        }
        return false;
    }
    public boolean start(int i){
        if(i<wagons.size()) {
            //wagons.get(i).velocity.set(0, 0);
            wagons.get(i).forceResultant.x += 1000000;
            return true;
        }
        return false;
    }
    public boolean zero(int i){
        if(i<wagons.size()) {
            wagons.get(i).forceResultant.x = 0;
            wagons.get(i).momentum = 0;
            return true;
        }
        return false;
    }

    public void reset(){
        wagons.clear();
        wagons = new ArrayList<Wagon2>();
        for(int i = 0; i < numWagons; i++){
            wagons.add(new Wagon2());
            wagons.get(i).position.set(-(i*wagons.get(i).size.x +minDist*i),5f);
        }
    }
}
