package com.jboby93.jgl_test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.jboby93.jgl.*;

public class App extends GameWindow {

	public static void main(String[] args) throws Exception {
		LibInfo.showAboutDialog();
		
		//uncomment ONE of the following game definitions for a feature demo
		
		//classic Bouncing Ball simulation
		App game = new App();
		
		//image features - thread-based animation, rotation, collision checking
		//ImageTest game = new ImageTest();
		
		//tiled graphics test
		//TiledGraphics game = new TiledGraphics();
		
		//start the 'game'
		game.start();
		
		System.exit(0);
	}

	// === BOUNCING BALL SAMPLE === //
	
	GameObject ball;
	GameObject ground;
	ArrayList<GameObject> walls;
	
	@Override
	protected void Initialize() {
		ball = new GameObject(Sprite.SpriteType.circle, 100, 100, 30, 30, new ShapeStyle(Color.blue, Color.white));
		ball.velocity = new Vector(3, 0);
		ball.isVelocityOn = true;
		ball.acceleration = new Vector(0, 1);
		ball.isAccelerationOn = true;
		
		ground = new GameObject(Sprite.SpriteType.box, 
				new Point(0, 460), new Dimension(this.getWidth(), 20),
				new ShapeStyle(Color.black, Color.black));
		ground.isSolidObject = true;
		
		walls = new ArrayList<GameObject>();
		walls.add(new GameObject(Sprite.SpriteType.box,
				new Point(0, 0), new Dimension(20, this.getHeight()),
				new ShapeStyle(Color.black, Color.black)));
		walls.add(new GameObject(Sprite.SpriteType.box,
				new Point(this.getWidth() - 20, 0), new Dimension(20, this.getHeight()),
				new ShapeStyle(Color.black, Color.black)));
		
		this.setTitle("BouncingBall Demo - JavaGL");
	}

	@Override
	protected void ProcessInput(GameWindowInputHandler input) {
		if(input.isKeyDown(KeyEvent.VK_ESCAPE)) {
			//escape key is pressed
			this.showExitPrompt();
		}
	}

	@Override
	protected void GameLogic() {
		ball.update();
		
		if(ball.isCollidingWith(ground)) { ball.bounceOffOf(ground); }
		for(GameObject w : walls) {
			if(ball.isCollidingWith(w)) {
				ball.bounceOffOf(w);
			}
		}
	}

	@Override
	protected void Render(Buffer b) {
		b.clear(Color.white);
		
		ball.draw(b);
		ground.draw(b);
		for(GameObject w : walls) { w.draw(b); }
		
		b.render();
	}

	@Override
	protected void Shutdown() {
		
	}
} //end App
