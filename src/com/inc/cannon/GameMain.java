package com.inc.cannon;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

public class GameMain extends Canvas implements Runnable {
	public static final int Width = 800, Height = 400;
	private static Frame fr;
	private Thread thread;
	private BufferStrategy bs;
	private Graphics g;
	private float gravity;
	private float xG = 0;
	private float yG = 0;
	private boolean _Runner = false;
	private boolean tf = true;
	int q = 500;
	int w = 200;
	int a[] = { q, q, q+50, q+50, q+60, q+60, q-10, q-10, q };
	int b[] = { w, w+50, w+50, w, w, w+60, w+60, w, w };

	private ArrayList<GameObjects> objects;

	public GameMain() {
		init();
		initEvent();
	}

	public void init() {
		thread = new Thread(this);
		objects = new ArrayList<GameObjects>();

		objects.add(new TestObject(50, 50, 0.3f));

	}

	void initEvent() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				for (GameObjects object : objects) {
					if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						object.setY(object.getY() - 1);
						yG += 0.2f;
						break;
					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						xG += 1f;
						break;

					}
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						xG -= 1f;
						break;
					}
					if (e.getKeyCode() == KeyEvent.VK_ENTER && tf == false) {
						GameMain2 gm = new GameMain2();
						thread.stop();
						_Runner = false;
						gm.main(null);
						fr.dispose();
						tf = true;
					}
				}

			}
		});
	}

	public void outOfScene(ArrayList<GameObjects> object) {
		if (object.size() < 1) {
			return;
		}
		for (GameObjects ob : object) {
			if (ob.getX() < -8f) {
				ob.setX(Width - 15);
			} else if (ob.getX() > Width - 15) {
				ob.setX(-8f);
			}
			if (ob.getY() > Height || ob.getY() < 0) {
				ob.setY(0);
			}
		}
	}

	private void obt() {
		for (GameObjects object : objects) {
			if (object.getX() >= q-10 && object.getX() <= q+60 && object.getY() >= w+55 && object.getY() <= w+60) {
				object.setVx(0);
				object.setVy(-1);
			}
			if (object.getX() >= q && object.getX() <= q+50 && object.getY() >= w+40 && object.getY() <= w+50) {
				object.setVx(0);
				object.setVy(+0.003f);
				tf = false;
			}
			if (object.getX() >= q-25 && object.getX() <= q-5 && object.getY() >= w && object.getY() <= w+60) {
				xG = -1;
				object.setVy(-1);
			}
			if (object.getX() >= q+50 && object.getX() <= q+60 && object.getY() >= w && object.getY() <= w+60) {
				xG = 1;
				object.setVy(-1);
			}
			if (object.getX() >= q-5 && object.getX() <= q && object.getY() >= w && object.getY() <= w+60) {
				xG = 0.1f;
				object.setVy(+0.003f);
			}
			if (object.getX() >= q+40 && object.getX() <= q+45 && object.getY() >= w && object.getY() <= w+60) {
				xG = -0.1f;
				object.setVy(+0.003f);
			}
		}
	}

	public void gravity(ArrayList<GameObjects> objects) {
		if (objects.size() < 1) {
			return;
		}

		for (GameObjects object : objects) {
			if (object.getY() > 290) {
				object.setVy(0);
			} else {
				gravity = object.getWeight() * (-9.8f / 1000f);
				object.setVy(object.getVy() + gravity);
				object.setY(object.getY() - object.getVy());
			}
			obt();

			if (xG < 0) {
				xG += 0.01;
				object.setVx(object.getVx() + xG);
			} else if (xG > 0) {
				xG -= 0.01;
				object.setVx(object.getVx() - xG);
			}
			if (yG > 0) {
				yG -= 0.03;
				object.setVy(object.getVy() + yG);
			}

			if (xG < 0) {
				object.setX(object.getX() + object.getVx());

			} else {
				object.setX(object.getX() - object.getVx());
			}
			object.setVx(0);
		}
	}

	public void render() {

		if (this.getBufferStrategy() == null) {
			this.createBufferStrategy(3);
			return;
		}

		bs = this.getBufferStrategy();
		g = bs.getDrawGraphics();

		g.clearRect(0, 0, Width, Height);

		g.setColor(Color.blue);

		for (GameObjects obj : objects) {
			g.drawRect((int) obj.getX(), (int) obj.getY(), 10, 10);
		}
		g.drawString("Box's gravity Accel : " + objects.get(0).getVy(), Width / 2, 75);

		g.setColor(Color.BLACK);

		g.drawLine(0, 300, Width, 300);

		if (tf == false) {
			g.drawString("다음 스테이지 = 엔터", 350, 180);
		}
		g.drawPolyline(a, b, 9);
		requestFocus();
		bs.show();
	}

	public void update() {
		outOfScene(objects);
		gravity(objects);
	}

	@Override
	public void run() {
		while (_Runner) {
			update();
			render();
		}
	}

	public void stater() {
		_Runner = true;
		thread.start();
	}

	public static void main(String[] args) {
		fr = new Frame();
		fr.setResizable(false);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		fr.setBounds((d.width - Width) / 2, (d.height - Height) / 2, Width, Height);
		fr.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		fr.requestFocus();

		GameMain gm = new GameMain();
		fr.add(gm);
		fr.setVisible(true);
		gm.stater();
	}

}