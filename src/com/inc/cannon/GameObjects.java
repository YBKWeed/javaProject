package com.inc.cannon;


public abstract class GameObjects {
	float x,y;
	float vx,vy;
	float weight;
	
	public GameObjects(float x,float y,float weight) {
		this.x = x;
		this.y = y;
		this.weight = weight;
	}
	
	public void setX(float x) { this.x = x;}
	public void setY(float y) { this.y = y;}
	
	public float getX() {return x;}
	public float getY() {return y;}
	
	public void setVx(float vx) { this.vx = vx;}
	public void setVy(float vy) { this.vy = vy;}
	
	public float getVx() { return vx;}
	public float getVy() { return vy;}
	
	public void setWeight(float weight) {this.weight = weight;}
	public float getWeight() {return weight;}
	
}
