package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends InputAdapter implements ApplicationListener {
	public static final float
			SCENE_WIDTH = 19.2f,
			SCENE_HEIGHT = 10.8f,
			WORLD_TO_SCREEN = 1f/100f,
			CAMERA_MOVE_SPEED = 5f,
			CAMERA_ZOOM_SPEED = 5f,
			VIRTUAL_WIDTH = 1920f,
			VIRTUAL_HEIGHT = 1080f;

	private OrthographicCamera cam, camHUD;
	private Viewport viewport, viewportHUD;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch, batchHud;
	private Train train;
	private HUD hud;
	private String inputString = "0";
	boolean typing = false;

	@Override
	public void create () {
		AssetLoader.load();
		hud = new HUD();
		train = new Train();
		camHUD = new OrthographicCamera();
		batchHud = new SpriteBatch();
		viewportHUD = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		camHUD.position.set(VIRTUAL_WIDTH*0.5f, VIRTUAL_HEIGHT*0.5f,0f);
		cam = new OrthographicCamera();
		cam.position.set(SCENE_WIDTH * 0.5f, SCENE_HEIGHT * 0.5f, 0f);
		cam.zoom = 10;
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, cam);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void resize(int width, int height) {

		viewport.update(width, height);
		viewportHUD.update(width,height);
	//	viewport.apply();

	}

	@Override
	public boolean keyTyped(char character) {
		if(typing){
			inputString += character;
		}
		return super.keyTyped(character);
	}

	@Override
	public void render() {
		float delta = Gdx.graphics.getDeltaTime();

		if(!typing) {
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				cam.position.y += CAMERA_MOVE_SPEED * delta * cam.zoom;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				cam.position.y -= CAMERA_MOVE_SPEED * delta * cam.zoom;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				cam.position.x -= CAMERA_MOVE_SPEED * delta * cam.zoom;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				cam.position.x += CAMERA_MOVE_SPEED * delta * cam.zoom;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.A)) {
				cam.zoom -= CAMERA_MOVE_SPEED * delta * cam.zoom;
				if (cam.zoom < 0.5f) cam.zoom = 0.5f;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
				cam.zoom += CAMERA_MOVE_SPEED * delta * cam.zoom;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
				train.stop(0);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
				train.start(0);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
				train.zero(0);
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
			if(typing){

			}

			typing = !typing;
		}


		//physics
		train.physics(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();
		viewport.apply();
		///Draw World
		shapeRenderer.setProjectionMatrix(cam.combined);
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		train.draw(batch);
		batch.end();

		//Draw HUD
		camHUD.update();
		viewportHUD.apply();
		batchHud.setProjectionMatrix(camHUD.combined);
		batchHud.begin();
		hud.draw(batchHud, delta);
		batchHud.end();


	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 touch = new Vector3(screenX, screenY, 0);
		touch.set(cam.unproject(touch));
		Gdx.app.log("Touched Norm", "" + screenX + "," + screenY);
		Gdx.app.log("Touched Unproject", "" + touch.x +","+ touch.y);
		return super.touchDown(screenX, screenY, pointer, button);
	}


}
