package com.wilsongateway.framework;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.wilsongateway.framework.Board.Stage;

/**
 * Name	 	: Nicholas Lane Wilson
 * Class 	    : 1620 - 002
 * Program # 	: 7
 * Due Date  	: 12/7/2016
 *
 * Honor Pledge:  On my honor as a student of the University
 *                of Nebraska at Omaha, I have neither given nor received
 *                unauthorized help on this homework assignment.
 *
 * NAME: Nicholas Lane Wilson
 * NUID: 350
 * EMAIL: nlwilson@unomaha.edu
 * 
 * Partners:   NONE
 *	
 * Description: Represents a player which is user controled and bound to a key.
 */
public class Player {
	
	//Position variables
	private int x;
	private double y;
	private double velY;
	private double accY;
	private double theta;
	public final static double accCutOff = 0.3;
	public final static double thetaCutOff = 1;
	private int binding = KeyEvent.VK_SPACE;
	
	//Collision Border
	private Shape outline;
	
	//Status
	private boolean alive;
	private int points = 0;
	
	private static ArrayList<Player> players = new ArrayList<Player>();
	
	/**
	 * 
	 * Method Name   : [Constructor]
	 * Parameters    : none
	 * Description   : Resets the players position and adds this to players.
	 */
	Player(){
		resetPlayer();
		players.add(this);
	}
	
	/**
	 * 
	 * Method Name   : resetPlayer
	 * Parameters    : none
	 * Return Values : void
	 * Description   : Resets all position variables and sets alive to true.
	 */
	public void resetPlayer(){
		y = Game.dayBackground.getHeight(null)/2;
		x = Game.boardPanel.getWidth()/2 - Game.getFlappyUp().getWidth(null)/2;
		velY = 0;
		accY = 0;
		theta = 0;
		
		alive = true;
		points = 0;
	}
	
	/**
	 * 
	 * Method Name   : paintPlayer
	 * Parameters    : g2d : Graphics
	 * Return Values : void
	 * Description   : Renders the current player and adjusts position variables.
	 */
	public void paintPlayer(Graphics2D g2d){
		//Status check
		if(alive){
			if(Board.current == Stage.PLAYING){
				//Adjust position variables
				velY += accY;
				y += velY;
				if(y < 0){
					y = 0;
				}
				
				//Deceleration
				if(accY < accCutOff){
					accY += .05;
				}
				
				//Falling rotation
				if(theta < thetaCutOff){
					theta += 0.02;
				}
			}
		}else{
			Game.settingsFrame.refreshMaxScoreLabel();
			Board.resetGame();
		}
		
		//Render images
		g2d.rotate(theta, x + Game.getFlappyUp().getWidth(null)/2, (int) (Game.heightRatio()*y) + Game.getFlappyUp().getHeight(null)/2);
		g2d.drawImage(Game.getFlappy(), x, (int) (Game.heightRatio()*y), null);
		
		checkCollision();
		
		//Dev Render Border
		if(Board.devMode){
			g2d.draw(outline);
		}
	}
	
	/**
	 * 
	 * Method Name   : flap
	 * Parameters    : none
	 * Return Values : void
	 * Description   : Adjusts position variables to represent an upwards acceleration.
	 */
	public void flap(){
		if(Board.current == Stage.PLAYING){
			accY = 0;
			velY = -4;
			theta = -0.5;
		}
	}
	
	/**
	 * 
	 * Method Name   : checkCollision
	 * Parameters    : none
	 * Return Values : void
	 * Description   : Compares an ellipse surrounding the player to collision boxes of nearby pipes to determine a collision.
	 */
	public void checkCollision(){
		//Check floor collision (y is inverted)
		if(Game.heightRatio()*y + Game.getFlappyUp().getHeight(null) > Game.getBackground().getHeight(null) - Game.getPlatform().getHeight(null)){
			alive = false;
		}
		
		//Refresh outline
		outline = new Ellipse2D.Double(x, (int) (Game.heightRatio()*y), Game.getFlappyUp().getWidth(null), Game.getFlappyUp().getHeight(null));
		
		for(Pipe pipe : Pipe.getPipes()){
			for(Rectangle2D s : pipe.getOutlines()){
				if(outline.intersects(s)){
					alive = false;
				}
			}
		}
	}
	
	/**
	 * 
	 * Method Name   : addPoint
	 * Parameters    : none
	 * Return Values : void
	 * Description   : Increments points.
	 */
	public void addPoint(){
		points++;
	}

	//Boilerplate
	public static ArrayList<Player> getPlayers() {return players;}
	public int getKeyBind(){return binding;}
	public void setKeyBind(int binding){this.binding = binding;}
	public boolean isAlive(){return alive;}
	public Shape getOutline(){return outline;}
	public int getX(){return x;}
	public int getPoints(){return points;}
}
