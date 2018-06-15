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

public class GameMain2 extends Canvas implements Runnable {
	public static final int Width = 800, Height = 400;
	private static Frame fr;
	private Thread thread, thread1, thread2, thread3, thread4, thread5;
	private Thread moveThread;
	private BufferStrategy bs;
	private Graphics g;
	private float gravity;
	private float xG = 0;
	private float yG = 0;
	private boolean _Runner = false;
	private boolean tf = true;
	private boolean move = true, move1 = true, move2 = true, move3 = true, move4 = true, move5 = true;
	int q = 550;
	int w = 200;
	int e = 0, e1 = 0, e2 = 12, e3 = 24, e4 = 12, e5 = 0;
	int ttggg = 0;
	int[] p = { 0, 70, 140, 210, 280 };
	int[] a1;
	int[] b1;
	int[] a2;
	int[] b2;
	int[] a3;
	int[] b3;
	int[] a4;
	int[] b4;
	int[] a5;
	int[] b5;

	// 벽 1

	int r1 = 100;
	int t1 = 100;
	int r2 = 100;
	int t2 = 100;
	int r3 = 100;
	int t3 = 100;
	int r4 = 100;
	int t4 = 100;
	int r5 = 100;
	int t5 = 100;

	private ArrayList<GameObjects> objects;

	public GameMain2() {
		init();
		initEvent();
		moveThreads();
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
						yG += 0.21f;
						break;
					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						xG += 0.8f;
						break;
					}
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						xG -= 0.8f;
						break;
					}
					if (e.getKeyCode() == KeyEvent.VK_ENTER && tf == false) {
						GameMain3 gm = new GameMain3();
						_Runner = false;
						thread.stop();
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
				ob.setX(6f);
			} else if (ob.getX() > Width - 15) {
				ob.setX(Width - 20);
			}
			if (ob.getY() > Height || ob.getY() < 80) {
				ob.setY(120);
				ob.setVy(-1);
			}
		}
	}

	private void obt() {
		for (GameObjects object : objects) {
			if (object.getX() >= q - 10 && object.getX() <= q + 60 && object.getY() >= w + 55
					&& object.getY() <= w + 60) {
				object.setVx(0);
				object.setVy(-1);
			}
			if (object.getX() >= q - 5 && object.getX() <= q + 50 && object.getY() >= w + 40
					&& object.getY() <= w + 50) {
				object.setVx(0);
				object.setVy(+0.003f);
				tf = false;
			}
			if (object.getX() >= q - 25 && object.getX() <= q - 5 && object.getY() >= w && object.getY() <= w + 60) {
				xG = -1;
				object.setVy(-1);
			}
			if (object.getX() >= q + 50 && object.getX() <= q + 60 && object.getY() >= w && object.getY() <= w + 60) {
				xG = 1;
				object.setVy(-1);
			}
			if (object.getX() >= q - 5 && object.getX() <= q && object.getY() >= w && object.getY() <= w + 60) {
				xG = 0.1f;
				object.setVy(+0.003f);
			}
			if (object.getX() >= q + 40 && object.getX() <= q + 45 && object.getY() >= w && object.getY() <= w + 50) {
				xG = -0.1f;
				object.setVy(+0.003f);
			}
			// 골대 --------------------------------

			repak(r1, t1, 0);
			repak(r2, t2, 1);
			repak(r3, t3, 2);
			repak(r4, t4, 3);
			repak(r5, t5, 4);
			if (object.getX() >= 230 && object.getX() <= 270 && object.getY() > 270 - 10 && object.getY() < 310) {
				object.setX(20);
				object.setY(120);
				object.setVx(0);
				object.setVy(0);
			}
		}
	}

	private void repak(int ra, int ta, int pa) {
		for (GameObjects object : objects) {
			if (object.getX() >= ra - 10 + p[pa] && object.getX() <= ra + 30 + p[pa] && object.getY() > ta - 10
					&& object.getY() < ta + 150) {
				object.setX(20);
				object.setY(120);
				object.setVx(0);
				object.setVy(0);
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
			g.fillOval((int) obj.getX(), (int) obj.getY(), 10, 10);
		}
		g.drawString("Box's gravity Accel : " + objects.get(0).getVy(), Width / 2, 75);

		g.setColor(Color.BLACK);

		g.drawLine(0, 300, Width, 300);
		g.drawLine(0, 80, Width, 80);

		if (tf == false) {
			g.drawString("다음 스테이지 = 엔터", 350, 180);
		}
		int[] a = { q, q, q + 50, q + 50, q + 60, q + 60, q - 10, q - 10, q };
		int[] b = { w, w + 50, w + 50, w, w, w + 60, w + 60, w, w };
		repak2(r1, t1, 0);
		repak2(r2, t2, 1);
		repak2(r3, t3, 2);
		repak2(r4, t4, 3);
		repak2(r5, t5, 4);

		int[] c6 = { 240, 240, 270, 270, 240 };
		int[] d6 = { 300, 270, 270, 300, 300 };
		g.drawPolyline(c6, d6, 5);

		g.drawPolyline(a, b, 9);
		requestFocus();
		bs.show();
	}

	private void repak2(int ra, int ta, int pa) {
		int[] c1 = { ra + p[pa], ra + 30 + p[pa], ra + 30 + p[pa], ra + p[pa], ra + p[pa] };
		int[] d1 = { ta, ta, ta + 150, ta + 150, ta };
		g.drawPolyline(c1, d1, 5);
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

		GameMain2 gm = new GameMain2();
		fr.add(gm);
		fr.setVisible(true);
		gm.stater();
	}

	private void moveThreads() {
		moveThread = new Thread(() -> {
			while (true) {
				if (move) {
					q += 1;
					e++;
					if (e > 90) {
						move = false;
					}
				} else {
					q -= 1;
					e--;
					if (e < 0) {
						move = true;
					}
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread1 = new Thread(() -> {
			while (true) {
				if (move1) {
					t1 += 2;
					e1++;
					if (e1 > 30) {
						move1 = false;
					}
				} else {
					t1 -= 2;
					e1--;
					if (e1 < 0) {
						move1 = true;
					}
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread2 = new Thread(() -> {
			while (true) {
				if (move2) {
					t2 += 2;
					e2++;
					if (e2 > 30) {
						move2 = false;
					}
				} else {
					t2 -= 2;
					e2--;
					if (e2 < 0) {
						move2 = true;
					}
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread3 = new Thread(() -> {
			while (true) {
				if (move3) {
					t3 += 2;
					e3++;
					if (e3 > 30) {
						move3 = false;
					}
				} else {
					t3 -= 2;
					e3--;
					if (e3 < 0) {
						move3 = true;
					}
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread4 = new Thread(() -> {
			while (true) {
				if (move4) {
					t4 += 2;
					e4++;
					if (e4 > 30) {
						move4 = false;
					}
				} else {
					t4 -= 2;
					e4--;
					if (e4 < 0) {
						move4 = true;
					}
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread5 = new Thread(() -> {
			while (true) {
				if (move5) {
					t5 += 2;
					e5++;
					if (e5 > 30) {
						move5 = false;
					}
				} else {
					t5 -= 2;
					e5--;
					if (e5 < 0) {
						move5 = true;
					}
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		thread5.start();
		moveThread.start();

	}
}