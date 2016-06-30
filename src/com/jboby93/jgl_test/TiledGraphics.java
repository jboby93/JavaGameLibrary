package com.jboby93.jgl_test;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JOptionPane;

import com.jboby93.jgl.Buffer;
import com.jboby93.jgl.Functions;
import com.jboby93.jgl.GameObject;
import com.jboby93.jgl.GameWindow;
import com.jboby93.jgl.GameWindowInputHandler;
import com.jboby93.jgl.LibInfo;
import com.jboby93.jgl.Sprite;

public class TiledGraphics extends GameWindow {

	//GameObject log;
	Sprite log;
	Sprite stoneBrick;
	Sprite quartz;
	
	@Override
	protected void Initialize() throws Exception {
		log = new Sprite(Functions.loadImageFromFile("resources/images/log_oak.png"));
		log.imageMode = Sprite.IMAGEMODE_TILED;
		
		stoneBrick = new Sprite(Functions.loadImageFromFile("resources/images/stonebrick.png"));
		stoneBrick.imageMode = Sprite.IMAGEMODE_TILED;
		
		quartz = new Sprite(Functions.loadImageFromFile("resources/images/quartzblock_lines_top.png"));
		quartz.imageMode = Sprite.IMAGEMODE_TILED;
		
		this.setTitle("JavaGL - TiledGraphics [BuildDate: " + LibInfo.buildDate + "]");
		JOptionPane.showMessageDialog(this, "This is a test of JavaGL's handling of drawing Sprites with tiling.", "JavaGL - TiledGraphics", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	protected void ProcessInput(GameWindowInputHandler input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void GameLogic() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void Render(Buffer b) throws Exception {
		b.clear(Color.white);
		
		log.draw(b, new Rectangle(0, 0, b.getWidth(), b.getHeight()));
		stoneBrick.draw(b, new Rectangle(128, 128, 96, 160));
		quartz.draw(b, new Rectangle(384, 96, 160, 96));
		
		b.render();
	}

	@Override
	protected void Shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
