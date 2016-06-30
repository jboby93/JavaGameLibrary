package com.jboby93.jgl_test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import com.jboby93.jgl.*;

public class ImageTest extends GameWindow {

	Image sword;
	Image swords[];
	
	GameObject swordSmall;
	GameObject swordBig;
	GameObject sword45;
	GameObject swordOffset;
	GameObject ground;
	
	boolean rotatingCollision = false;
	Rectangle r;
	
	Pen p = new Pen(Color.black, 2);
	
	@Override
	protected void Initialize() {
		sword = Functions.loadImageFromFile("resources/images/swordIron.png");
		swords = new Image[] {sword, 
							  Buffer.flipImage(sword, true, false),
							  Buffer.flipImage(sword, true, true),
							  Buffer.flipImage(sword, false, true)}; 
		
		swordSmall = new GameObject(swords, new Point(12, 36), 5);
		swordSmall.sprite.startAnimating();
		
		swordBig = new GameObject(swords, new Point(swordSmall.getRight() + 12, 36), new Dimension(64, 64), 10);
		swordBig.acceleration = new Vector(0, 1);
		swordBig.isAccelerationOn = true;
		swordBig.isVelocityOn = true;
		
		sword45 = new GameObject(sword, new Point(swordBig.getRight() + 12, 36), new Dimension(32, 32));
		sword45.rotation = new Angle(0);
		
		swordOffset = new GameObject(sword, new Point(sword45.getRight() + 24, 36), new Dimension(96, 96));
		swordOffset.rotationAnchor = new Point(72, 72); //off-centered rotation anchor
		
		ground = new GameObject(Functions.loadImageFromFile("resources/images/ground.png"), new Point(0, 426));
		
		this.setTitle("JavaGL - ImageTest [BuildDate: " + LibInfo.buildDate + "]");
		JOptionPane.showMessageDialog(this, "This is a test of JavaGL's image and sprite functions.\nDemos: rotation, computing rotated bounds, thread-based animation, animation functions, scaling, text\n" +
									"OS Name: " + LibInfo.getOSName(), "JavaGL - ImageTest", JOptionPane.INFORMATION_MESSAGE);
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
		swordBig.update();
		
		if(swordBig.isCollidingWith(ground)) {
			swordBig.sprite.nextFrame();
			swordBig.bounceOffOf(ground);
		}
		
		if(sword45.rotation.getDegrees() >= 360) {
			sword45.rotation.setDegrees(0);
		} else {
			sword45.rotation.addDegrees(1);
		}
		
		if(swordOffset.rotation.getDegrees() <= 0) {
			swordOffset.rotation.setDegrees(360);
		} else {
			swordOffset.rotation.subtractDegrees(1);
		}
		
		r = Functions.Math.getRotatedBoundingBox(swordOffset);
		rotatingCollision = swordOffset.isCollidingWith(sword45);
	}
	
	@Override
	protected void Render(Buffer b) {
		b.clear(new Color(100, 149, 237));
		
		swordSmall.draw(b);
		swordBig.draw(b);
		sword45.draw(b);
		swordOffset.draw(b);
		
		//draw some rotating boxes!
		b.drawRect(new Pen(Color.red), Functions.Math.getRotatedBoundingBox(sword45.getBounds(), sword45.rotation.getRadians()));
		b.drawRect(new Pen(Color.green), r);
		b.drawCircle(new Pen(), new Point(swordOffset.x + swordOffset.rotationAnchor.x, swordOffset.y + swordOffset.rotationAnchor.y), 3);
		
		ground.draw(b);
		
		b.drawText("swordSmall.sprite.curFrame = " + swordSmall.sprite.getCurrentFrameIndex(), new Point(12, 12));
		b.drawText("swordSmall.sprite.isAnimating = " + swordSmall.sprite.isAnimating(), new Point(12, 24));
		
		if(rotatingCollision) {
			b.drawText("Collision!", new Point(300, 150));
		}
		
		b.drawText("swordOffset.rotationAnchor = " + swordOffset.rotationAnchor.toString(), new Point(250, 200));
		b.drawText("swordOffset.rotation = " + swordOffset.rotation.toString(), new Point(250, 212));
		b.drawText("swordOffset.getLocation() = " + swordOffset.getLocation().toString(), new Point(250, 224));
		b.drawText("getRotatedBoundingBox(swordOffset) = " + r.toString(), new Point(250, 236));
		
		/*
		b.drawImage(sword, new Point(50, 50));
		//b.drawImage(Buffer.flipImage(sword, true, false), new Point(50, 50));
		//b.drawImage(sword, new Point(50, 50), new Dimension(-16, 16));
		//b.drawImage(sword, new Point(50, 50), new Dimension(16, -16));
		b.drawRect(new Pen(), new Rectangle(50, 50, 16, 16));
		*/
		
		b.render();
	}

	@Override
	protected void Shutdown() {
		
		swordSmall.dispose();
		swordBig.dispose();
		sword45.dispose();
		swordOffset.dispose();
	}

}
