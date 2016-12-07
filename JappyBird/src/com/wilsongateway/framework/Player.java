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

public class Player {
	
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
	
	private boolean alive;
	private int points = 0;
	
	private static ArrayList<Player> players = new ArrayList<Player>();
	
	public Player(){
		resetPlayer();
		players.add(this);
	}
	
	public void resetPlayer(){
		y = Game.dayBackground.getHeight(null)/2;
		x = Game.boardPanel.getWidth()/2 - Game.getFlappyUp().getWidth(null)/2;
		velY = 0;
		accY = 0;
		theta = 0;
		
		alive = true;
		points = 0;
	}
	
	public void paintPlayer(Graphics2D g2d){
		if(alive){
			if(Board.current == Stage.PLAYING){
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
		
		g2d.rotate(theta, x + Game.getFlappyUp().getWidth(null)/2, (int) (Game.heightRatio()*y) + Game.getFlappyUp().getHeight(null)/2);
		g2d.drawImage(Game.getFlappy(), x, (int) (Game.heightRatio()*y), null);
		
		checkCollision();
		
		//Dev Render Border
		if(Board.devMode){
			g2d.draw(outline);
		}
	}
	
	public void flap(){
		if(Board.current == Stage.PLAYING){
			accY = 0;
			velY = -4;
			theta = -0.5;
		}
	}
	
	public void checkCollision(){
		//Check floor collision (y is inverted)
		if(Game.heightRatio()*y + Game.getFlappyUp().getHeight(null) > Game.getDayBackground().getHeight(null) - Game.getPlatform().getHeight(null)){
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
