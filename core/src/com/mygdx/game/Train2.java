package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by Russell Elfenbein on 9/23/2015.
 */
public class Train2 {
    BitmapFont font = new BitmapFont();
    private static float posY;
    private float drawBarLength = 4f;
    private float minDist = 0.5f*drawBarLength, maxDist = drawBarLength;

    private int numWagons = 15;
    private ArrayList<Wagon2> wagons;

    public Train2(){
        wagons = new ArrayList<Wagon2>();
        for(int i = 0; i < numWagons; i++){
            wagons.add(new Wagon2());
            wagons.get(i).position.set(-(i*wagons.get(i).size.x +minDist*i*1.25f),15f);
        }
    }

    public void update(float delta){

        Gdx.app.log("FPS:",1/delta+"");
        if(delta > 0.02f)
            delta = 0.02f;

        calcCollisions(delta);

    }

    public int checkCollision(float delta){
        Wagon2 wagon, nextWagon, prevWagon;
        float maxOvershoot = -1;
        int index = -1;

        for(int i = 0; i < wagons.size()-1; i++){
            float spacing = -1;
            wagon = wagons.get(i);
            nextWagon = wagons.get(i+1);
            wagon.rearSpacing = wagon.position.x - nextWagon.position.x - wagon.size.x;

            if(wagon.rearSpacing < minDist && (minDist-wagon.rearSpacing) > maxOvershoot){
                maxOvershoot = minDist - wagon.rearSpacing;
                Gdx.app.log("Max Overshoot","Wagon "+i+" Max Overshoot: "+maxOvershoot);
                index = i;
            } else if(wagon.rearSpacing > maxDist && (wagon.rearSpacing - maxDist) > maxOvershoot){
                maxOvershoot = wagon.rearSpacing - maxOvershoot;
                Gdx.app.log("Max Overshoot","Wagon "+i+" Max Overshoot: "+maxOvershoot);
                index = i;
            }
        }

        if(index != -1){
            return index;
        }
        return -1;
    }

    public boolean projectCollision(int index){
        for(int i = 0; i < wagons.size(); i++){

        }

        return false;
    }

    public void calcCollisions(float delta){
        float collisionTime = 0, timeStep = 0, soonestCollisionDelta = -1;
        int j = 0;
        while(timeStep < delta) {
            j++;
            for (int i = 0; i < wagons.size() - 1; i++) {
                Wagon2 wagon, nextWagon;
                wagon = wagons.get(i);
                nextWagon = wagons.get(i + 1);
                float spacing = wagon.rearSpacing = wagon.position.x - nextWagon.position.x - wagon
                        .size.x;
                float relativeVel = wagon.velocity.x - nextWagon.velocity.x;
                //float relativeAcc = wagon.acceleration.dst(nextWagon.acceleration);
                if(relativeVel<0){
                    spacing = spacing-minDist;
                } else if(relativeVel > 0){
                    spacing = spacing - maxDist;
                }
                if(relativeVel != 0)
                    collisionTime = spacing/Math.abs(relativeVel);
                if(collisionTime < soonestCollisionDelta){
                    soonestCollisionDelta = collisionTime;
                }
            }
            if(soonestCollisionDelta != -1) {
                physics(soonestCollisionDelta);
                timeStep += soonestCollisionDelta;
                Gdx.app.log("soonestCollisionDelta "+soonestCollisionDelta,"timestep" + timeStep);
            } else {
                timeStep = delta;
                physics(delta);
                Gdx.app.log("Timesep and Delta", "T:"+timeStep+ " D:"+delta);
            }
            Gdx.app.log("Calcing collisions While loop","Iteration "+j);
        }
    }



    public void physics(float delta){
        Wagon2 wagon;
        for(int i = 0; i < wagons.size() ;i++){
            wagon = wagons.get(i);
            wagon.position.add(wagon.velocity.x*delta,0);
        }
        ensureSpacing();
        calcVelocity(delta);
    }

    public void ensureSpacing(){
        Wagon2 wagon, nextWagon;
        for(int i = 0; i < wagons.size()-1; i++){
            wagon = wagons.get(i);
            nextWagon = wagons.get(i+1);
            if(wagon.rearSpacing < minDist){
                wagon.position.x = nextWagon.position.x + minDist+wagon.size.x;
                wagon.rearSpacing = minDist;
                wagon.momentum = (wagon.momentum + nextWagon.momentum)*0.5f;
                nextWagon.momentum = wagon.momentum;
            }
            if(wagon.rearSpacing > maxDist){
                wagon.position.x = nextWagon.position.x + maxDist+wagon.size.x;
                wagon.rearSpacing = maxDist;
                wagon.momentum = (wagon.momentum + nextWagon.momentum)*0.5f;
                nextWagon.momentum = wagon.momentum;
            }
        }
    }

    public void calcVelocity(float delta){
        Wagon2 wagon;
        for(int i = 0; i < wagons.size(); i++){
            wagon = wagons.get(i);
            wagon.acceleration.x = wagon.forceResultant.x/wagon.mass;
            wagon.velocity.x = wagon.momentum/wagon.mass;
            wagon.velocity.x += wagon.acceleration.x*delta;
            wagon.momentum = wagon.velocity.x*wagon.mass;
            wagon.position.x += wagon.velocity.x*delta;

        }
    }

    public void draw(SpriteBatch batch){
        font.setColor(1,0,0,1);
        font.getData().setScale(0.1f,0.1f);
        for (Wagon2 wagon : wagons) {
            AssetLoader.wagonS.setPosition(wagon.position.x, wagon.position.y);

            float x = Math.abs(wagon.velocity.x);
            x = (x/3);

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
            wagons.get(i).forceResultant.x -= 10000;
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
            //wagons.get(i).velocity.set(0, 0);
            wagons.get(i).forceResultant.x = 0;
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
