package com.jboby93.jgl;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * The Sprite class is used to store information about how to draw something.
 * It does not store location or size; this must be provided to the draw() methods from another source.
 * @author Jace
 */
public class Sprite {
	// ======== FOR IMAGE-BASED SPRITES
	private Image imgs[];
	/**
	 * Gets the array of image-based frames stored in this Sprite
	 * @return
	 */
	public Image[] getFrames() { return imgs; }
	/**
	 * Sets the array of image-based frames stored in this Sprite
	 * @param value
	 */
	public void setFrames(Image value[]) {
		imgs = value;
		if(curFrame > value.length - 1)
			curFrame = 0;
	}
	
	private int fps;
	/**
	 * Gets the FPS of this Sprite's animation
	 * @return
	 */
	public int getFPS() { return fps; }
	/**
	 * Sets the FPS of this Sprite's animation
	 * @param value
	 */
	public void setFPS(int value) {
		fps = value;
		if(fps <= 0) fps = 1;
	}
	
	private int curFrame;
	/**
	 * Gets the current frame index of animation, or -1 if the Sprite does not animate (is a primitive shape)
	 * @return
	 */
	public int getCurrentFrameIndex() { return curFrame; }
	/**
	 * Sets the current frame index of animation.  This has no effect if the Sprite is a primitive shape
	 * @param value
	 */
	public void setCurrentFrameIndex(int value) {
		if(curFrame == -1) {
			//current sprite is not animatable
		} else {
			if(value >= imgs.length) { 
				//out of bounds
				curFrame = 0;
			} else {
				curFrame = value;
			}
		}
	}
	
	/**
	 * Gets the current frame of animation as an image
	 * @return
	 */
	public Image getCurrentFrame() { return (curFrame == -1 ? null : imgs[curFrame]); }
	
	/**
	 * Returns true if this Sprite can animate; false otherwise
	 * @return
	 */
	public boolean canAnimate() { return (curFrame > -1); }
	
	private boolean isAnimating;
	/**
	 * Returns true if this Sprite is currently animating; false otherwise
	 * @return
	 */
	public boolean isAnimating() { return isAnimating; }
	
	/**
	 * If true, the animation will loop forever when the Sprite is animating.
	 */
	public boolean shouldLoopAnimation;
	
	// ======== FOR SHAPE-BASED SPRITES
	private ShapeStyle sStyle;
	/**
	 * Gets the ShapeStyle used to control drawing of this Sprite if a shape-type is used
	 * @return
	 */
	public ShapeStyle getShapeStyle() { return sStyle; }
	/**
	 * Sets the ShapeStyle used to control drawing of this Sprite if a shape-type is used
	 * 
	 * Changing this has no effect if this Sprite is an image-based sprite.
	 * @param value
	 */
	public void setShapeStyle(ShapeStyle value) {
		sStyle = value;
	}
	
	// ======== OTHER PROPERTIES
	private SpriteType sType;
	/**
	 * The possible types of sprite
	 * @author Jace
	 *
	 */
	public enum SpriteType {box, circle, image};
	public SpriteType getType() { return sType; }
	
	/**
	 * A value from 0.0 to 1.0 representing the opacity of this Sprite
	 */
	public double opacity;
	
	public static final int IMAGEMODE_STRETCHED = 0;
	public static final int IMAGEMODE_TILED = 1;
	public int imageMode = IMAGEMODE_STRETCHED;
	
	/**
	 * Creates a new Sprite object with the given type and initialization data
	 * @param type The type of sprite to create
	 * @param initData If shape-based: a ShapeStyle object that stores drawing controls; if image-based: an Image array of frames to load
	 */
	public Sprite(SpriteType type, Object initData) {
		imgs = null;
		sStyle = null;
		
		sType = type;
		switch(type) {
		case box:
		case circle:
			sStyle = (ShapeStyle)initData;
			curFrame = -1;
			break;
		case image:
			imgs = (Image[])initData;
			break;			
		}
		
		this.init();
	}
	
	/**
	 * Creates a new Sprite from the given image
	 * @param img
	 */
	public Sprite(Image img) {
		imgs = new Image[] {img};
		sStyle = null;
		sType = SpriteType.image;
	
		this.init();
	}
	
	/**
	 * Creates a new animatable Sprite from the given array of images
	 * @param frames
	 */
	public Sprite(Image frames[]) {
		imgs = frames;
		sStyle = null;
		sType = SpriteType.image;
		
		this.init();
	}
	
	private void init() {
		curFrame = 0;
		opacity = 1.0;
		fps = 30;
		isAnimating = false;
		shouldLoopAnimation = true;
	}
	
	/**
	 * Draws the Sprite to the specified buffer using the specified bounds
	 * @param b
	 * @param bounds
	 */
	public void draw(Buffer b, Rectangle bounds) {
		//bounds check for opacity
		if(opacity < 0.0) opacity = 0.0;
		if(opacity > 1.0) opacity = 1.0;
		
		switch(sType) {
		case box:
		case circle:
			
			//box and circle have the same initialization code to prepare for drawing
			
			//make sure we actually have a drawing style defined
			if(sStyle == null) {
				//throw exception
			}
			
			int outlineAlpha = 0;
			int fillAlpha = 0;
			
			Color f = null;
			Color i = null;
			Color o = null;
			
			Pen p = null;
			
			if(sStyle.drawFill) {
				fillAlpha = (int)(opacity * (double)sStyle.fill.getAlpha());
				f = new Color(sStyle.fill.getRed(), sStyle.fill.getGreen(), sStyle.fill.getBlue(), fillAlpha);
			}
			if(sStyle.drawOutline) {
				outlineAlpha = (int)(opacity * (double)sStyle.outline.getColor().getAlpha());
				i = sStyle.outline.getColor();
				o = new Color(i.getRed(), i.getGreen(), i.getBlue(), outlineAlpha);
				
				p = new Pen(o, sStyle.outline.getWidth(), sStyle.outline.getCaps());
			}
			
			switch(sType) {
			case box:
				if(sStyle.drawFill) {
					b.fillRect(f, bounds);
				}
				if(sStyle.drawOutline) {
					b.drawRect(p, bounds);
				} 
				break; //end case box
			case circle:
				//remember: bounds = the bounding rectangle
				//  center point = (x + (width / 2), y + (height / 2))
				//      x-radius = width / 2
				//      y-radius = height / 2
			
				//int xRadius = (int)(bounds.width / 2);
				//int yRadius = (int)(bounds.height / 2);
				//Point center = new Point(bounds.x + xRadius, bounds.y + yRadius);

				if(sStyle.drawFill) {
					b.fillOval(f, bounds);
				}
				if(sStyle.drawOutline) {
					b.drawOval(p, bounds);
				}
				
				break; //end case circle
			} //end switch
			
			break; //end case box, case circle
		case image:
			//need AlphaComposite for handling transparent images
			//see http://www.informit.com/articles/article.aspx?p=26349&seqNum=5
			
			b.setDrawOpacity((float)opacity);
			
			if(this.imageMode == IMAGEMODE_TILED) {
				int tilesX = bounds.width / imgs[curFrame].getWidth(null);
				int tilesY = bounds.height / imgs[curFrame].getHeight(null);
				
				int w = 0; int tX = 0;
				int h = 0; int tY = 0;
				for(Integer x = 0 ; x < tilesX; x++) {
					tY = 0;
					h = 0;
					
					for(Integer y = 0; y < tilesY; y++) {
						b.drawImage(imgs[curFrame], 
								new Point(bounds.getLocation().x + (x * imgs[curFrame].getWidth(null)), 
										bounds.getLocation().y + (y * imgs[curFrame].getHeight(null))));
						h++;
						tY++;
					}
					
					if((h * imgs[curFrame].getHeight(null)) < bounds.height) {
						//didn't reach the full height of the bounds so we need a partial tile drawn
						int dH = bounds.height - (tilesY * imgs[curFrame].getHeight(null));
						
						//so we need to draw the tile (without stretching it) at size of (tileX, dH)
						//and it is tile tY in this direction
						//location: (bounds.getLocation().x + (x * imgs[curFrame].getWidth(null)),
						//			 bounds.getLocation().y + (tY * imgs[curFrame].getHeight(null)))
						b.drawImage(Functions.getPartOfImage(imgs[curFrame], new Rectangle(0, 0, imgs[curFrame].getWidth(null), dH)),
								new Point(bounds.getLocation().x + (x * imgs[curFrame].getWidth(null)),
											bounds.getLocation().y + (tY * imgs[curFrame].getHeight(null))));
					}
					
					w++;
					tX++;
				} //end for (tiles in x direction)
				
				//one more loop for the blank space (if any) where tiles aren't drawn in x-direction
				if(w * imgs[curFrame].getWidth(null) < bounds.width) {
					int dW = bounds.width - (tilesX * imgs[curFrame].getWidth(null));
					
					tY = 0;
					h = 0;
					
					for(Integer y = 0; y < tilesY; y++) {
						b.drawImage(Functions.getPartOfImage(imgs[curFrame], new Rectangle(0, 0, dW, imgs[curFrame].getHeight(null))), 
								new Point(bounds.getLocation().x + (tX * imgs[curFrame].getWidth(null)), 
										bounds.getLocation().y + (y * imgs[curFrame].getHeight(null))));
						h++;
						tY++;
					}
					
					if((h * imgs[curFrame].getHeight(null)) < bounds.height) {
						//didn't reach the full height of the bounds so we need a partial tile drawn
						int dH = bounds.height - (tilesY * imgs[curFrame].getHeight(null));
						
						//so we need to draw the tile (without stretching it) at size of (tileX, dH)
						//and it is tile tY in this direction
						//location: (bounds.getLocation().x + (x * imgs[curFrame].getWidth(null)),
						//			 bounds.getLocation().y + (tY * imgs[curFrame].getHeight(null)))
						b.drawImage(Functions.getPartOfImage(imgs[curFrame], new Rectangle(0, 0, dW, dH)),
								new Point(bounds.getLocation().x + (tX * imgs[curFrame].getWidth(null)),
											bounds.getLocation().y + (tY * imgs[curFrame].getHeight(null))));
					}
				} //end if (need partial tiles in x direction?)
				//end tiled graphics handling
			} else {
				b.drawImage(imgs[curFrame], bounds.getLocation(), bounds.getSize());
			} //end if (draw mode)
				
			b.resetDrawOpacity();
			break;		
		} //end switch
	} //end draw()
	
	/**
	 * Draws the Sprite to the given buffer with the specified bounds and rotation.  The Sprite will be rotated around the centerpoint of the provided bounding rectangle.
	 * @param b
	 * @param bounds
	 * @param radians The rotation of the object in radians.
	 */
	public void drawRotated(Buffer b, Rectangle bounds, double radians) {
		if(radians == 0) {this.draw(b, bounds); } else {
			this.drawRotated(b, bounds, radians, new Point(bounds.width / 2, bounds.height / 2));
		} //end if (if no rotation, don't do the big block)		
	} //end drawRotated()
	
	/**
	 * Draws the Sprite to the given buffer with the specified bounds and rotation.  The Sprite will be rotated around the given anchoring point.
	 * @param b
	 * @param bounds
	 * @param radians
	 * @param anchor
	 */
	public void drawRotated(Buffer b, Rectangle bounds, double radians, Point anchor) {
		if(radians == 0) { this.draw(b, bounds); } else {
			Image rotated = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D)rotated.getGraphics();
			
			//rotation is in radians
			g.rotate(radians, bounds.x + anchor.x, bounds.y + anchor.y);
			
			//bounds check for opacity
			if(opacity < 0.0) opacity = 0.0;
			if(opacity > 1.0) opacity = 1.0;
			
			//now draw the thing
			switch(sType) {
			case box:
			case circle:
				//box and circle have the same initialization code to prepare for drawing
				
				//make sure we actually have a drawing style defined
				if(sStyle == null) {
					//throw exception
				}
				
				int outlineAlpha = (int)(opacity * (double)sStyle.outline.getColor().getAlpha());
				int fillAlpha = (int)(opacity * (double)sStyle.fill.getAlpha());
				
				Color f = new Color(sStyle.fill.getRed(), sStyle.fill.getGreen(), sStyle.fill.getBlue(), fillAlpha);
				Color i = sStyle.outline.getColor();
				Color o = new Color(i.getRed(), i.getGreen(), i.getBlue(), outlineAlpha);
				
				switch(sType) {
				case box:
					if(sStyle.drawFill) {
						g.setColor(f);
						g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
					}
					if(sStyle.drawOutline) {
						g.setColor(o);
						g.setStroke(Buffer.createStroke(sStyle.outline.getWidth(), sStyle.outline.getCaps()));
						g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
					} 
					break;
				case circle:
					//remember: bounds = the bounding rectangle
					//  center point = (x + (width / 2), y + (height / 2))
					//      x-radius = width / 2
					//      y-radius = height / 2
				
					//int xRadius = (int)(bounds.width / 2);
					//int yRadius = (int)(bounds.height / 2);
					//Point center = new Point(bounds.x + xRadius, bounds.y + yRadius);

					if(sStyle.drawFill) {
						g.setColor(f);
						g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
					}
					if(sStyle.drawOutline) {
						g.setColor(o);
						g.setStroke(Buffer.createStroke(sStyle.outline.getWidth(), sStyle.outline.getCaps()));
						g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
					} 
					break;
				} //end switch
				break; //end case (box, circle)
			case image:
				//need AlphaComposite for handling transparent images
				//see http://www.informit.com/articles/article.aspx?p=26349&seqNum=5
				
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)opacity));
				
				//if tiles mode: do something else
				//else
				g.drawImage(imgs[curFrame], bounds.x, bounds.y, bounds.width, bounds.height, null);
				
				b.resetDrawOpacity();
				break; //end case (image)
			} //end switch
			
			//now, draw the resulting image onto the buffer
			b.drawImage(rotated, new Point(0, 0), new Dimension(b.getWidth(), b.getHeight()));
			
			//finally release the memory resources used here
			g.dispose();
			rotated.flush();
		} //end if (if radians == 0)
	} //end drawRotated()
	
	//============= ANIMATION STUFF
	
	/**
	 * Advances to the next frame of animation.  Has no effect if the Sprite is not animatable, or only has a single frame
	 */
	public void nextFrame() {
		if(curFrame == -1) return;
		
		if(curFrame == imgs.length - 1) {
			curFrame = 0;
		} else {
			curFrame++;
		}
	}
	
	/**
	 * Returns to the previous frame of animation.  Has no effect if the Sprite is not animatable, or only has a single frame
	 */
	public void previousFrame() {
		if(curFrame == -1) return;
		
		if(curFrame == 0) {
			curFrame = imgs.length - 1;
		} else {
			curFrame--;
		}
	}
	
	//the animation thread
	Thread animateThread;
	
	/**
	 * Begins animation of the Sprite on a separate thread.  Has no effect if the Sprite is not animatable
	 * 
	 * Be sure you call stopAnimating(), or dispose() on your GameObject, to stop the animation thread when your program is exiting.
	 */
	public void startAnimating() {
		if(curFrame == -1) return;
		
		isAnimating = true;
		animateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				//animate();
				
				while(isAnimating) {
					//if there's only one frame there's no point calling this
					if(imgs.length > 1) nextFrame();
					
					//bug catch?
					//if(curFrame >= imgs.length) curFrame = 0;
					
					if(!shouldLoopAnimation && (curFrame == imgs.length - 1)) {
						//reached the end of the animation
						isAnimating = false;
						break;
					}
					
					try {
						Thread.sleep(1000 / fps);
					} catch(Exception e) {
						isAnimating = false;
					}
				} //end while
			} //end run()
		}); //end creation of animation thread
		animateThread.start();
	}
	
	/**
	 * Stops animation of this Sprite.  Has no effect if the Sprite is not animatable
	 */
	public void stopAnimating() {
		if(curFrame == -1) return;
		
		isAnimating = false;
	}

	private void animate() {
		while(isAnimating) {
			nextFrame();
			
			if(!shouldLoopAnimation && (curFrame == imgs.length - 1)) {
				//reached the end of the animation
				isAnimating = false;
			}
			
			try {
				Thread.sleep(1000 / fps);
			} catch(Exception e) {
				isAnimating = false;
			}
		} //end while
	} //end animate()
	
	/**
	 * Disposes of all resources used by this object, and stops the animation thread if it is running
	 */
	public void dispose() {
		isAnimating = false;
	} //end dispose()
} //end class Sprite
