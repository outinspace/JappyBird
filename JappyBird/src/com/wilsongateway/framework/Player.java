package com.wilsongateway.framework;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.wilsongateway.framework.Board.Stage;

public class Player {
	
	private int x;
	private double y;
	private double velY = 0;
	private double accY = 0;
	private double theta = 0;
	public final static double accCutOff = 0.3;
	public final static double thetaCutOff = 1;
	private int binding = KeyEvent.VK_SPACE;
	
	private boolean alive;
	
	private static ArrayList<Player> players = new ArrayList<Player>();
	
	public Player(){
		resetPlayer();
		players.add(this);
		alive = true;
	}
	
	public void resetPlayer(){
		y = Game.getDayBackground().getHeight(null)/3;
		x = Game.getDayBackground().getWidth(null)/2;
		alive = true;
	}
	
	public void paintPlayer(Graphics2D g2d){
		if(alive){
			if(Board.current == Stage.PLAYING){
				velY += accY;
				y += velY;
				
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
			x -= Game.heightRatio()*Board.speedScaler;
		}
		g2d.rotate(theta, x + Game.getFlappyUp().getWidth(null)/2, (int) (Game.heightRatio()*y) + Game.getFlappyUp().getHeight(null)/2);
		g2d.drawImage(Game.getFlappy(), x, (int) (Game.heightRatio()*y), null);
		
		checkCollision();
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
		//System.out.println(y + "    " + (Game.getDayBackground().getHeight(null) - Game.getPlatform().getHeight(null)));
		//TODO
		if(Game.heightRatio()*y + Game.getFlappyUp().getHeight(null) > Game.getDayBackground().getHeight(null) - Game.getPlatform().getHeight(null)){
			alive = false;
		}
	}

	//Boilerplate
	public static ArrayList<Player> getPlayers() {return players;}
	public int getKeyBind(){return binding;}
	public void setKeyBind(int binding){this.binding = binding;}
	public boolean isAlive(){return alive;}
}
