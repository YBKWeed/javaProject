package com.inc.cannon;

import java.awt.Image;
import java.awt.Toolkit;

public class Gun {
	float x,y;
	Image img  = Toolkit.getDefaultToolkit().createImage("src/img/gun.png");
	public Gun(float x, float y) {
		
		this.x = x;
		this.y = y;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
}
