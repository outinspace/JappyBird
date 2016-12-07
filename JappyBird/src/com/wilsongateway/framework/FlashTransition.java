package com.wilsongateway.framework;

import java.awt.Color;
import java.awt.Graphics2D;

import com.wilsongateway.framework.Board.Stage;

public class FlashTransition extends Transition{

	double opacity = 0;
	double increment;
	
	FlashTransition(Stage destinationStage, int length) {
		super(destinationStage, length);
		increment = 255/length;
	}

	@Override
	protected void paintTransition(Graphics2D g2d) {
		if(transitionTick < length/2){
			opacity += increment;
		}else{
			opacity -= increment;
		}
		
		g2d.setColor(new Color(255, 255, 255, (int)opacity));
		g2d.fillRect(0, 0, Game.boardPanel.getWidth(), Game.boardPanel.getHeight());
		
		if(transitionTick == length/2){
			Board.current = destinationStage;
			System.out.println("Stage Changed");
		}
		System.out.println(transitionTick);
	}
}
