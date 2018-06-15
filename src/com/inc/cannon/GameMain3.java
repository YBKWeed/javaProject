package com.inc.cannon;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Iterator;

public class GameMain3 extends Canvas implements Runnable {
	public static final int Width = 800, Height = 400;
	private static Frame fr;
	private Thread thread;
	private BufferStrategy bs;
	private Graphics g;
	private float xG = 0;
	private float yG = 0;
	private boolean _Runner = false;
	long startTime = System.currentTimeMillis();
	private double time;
	private double time2;
	private boolean tf = true;
	private Image dogImg = Toolkit.getDefaultToolkit().createImage("src/img/철댕이.png");
	private Image player = Toolkit.getDefaultToolkit().createImage("src/img/NTW.png");
	private Image gun = Toolkit.getDefaultToolkit().createImage("src/img/gun.png");
	private Image gun2 = Toolkit.getDefaultToolkit().createImage("src/img/gun2.png");
	private Image back = Toolkit.getDefaultToolkit().createImage("src/img/back.png");
	private Image boss = Toolkit.getDefaultToolkit().createImage("src/img/boss.png");
	private int ku = 10;
	private int t = 1000;
	private int bosshp = 80;
	private int playerhp = 4;
	private int bost = 5000;
	private int bosso;
	private int ox;
	private int oy;
	int count;
	private int bosx = 680, bosy = 150;
	private int[] bosb = new int[6];
	private ArrayList<GameObjects> objects;
	private ArrayList<Enemy> enemy;
	private ArrayList<Gun> gunA;
	private ArrayList<Gun> egun;
	long beforeTime = System.currentTimeMillis();
	private boolean bossb = true;
	Font ft;

	public GameMain3() {
		init();
		initEvent();

		Thread newThread = new Thread(() -> {

			while (bossb) {
				initThread();
				try {
					thread.sleep(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		newThread.start();

	}

	public void init() {
		thread = new Thread(this);
		objects = new ArrayList<GameObjects>();
		enemy = new ArrayList<Enemy>();
		gunA = new ArrayList<Gun>();
		egun = new ArrayList<Gun>();
		objects.add(new TestObject(50, 50, 0.3f));
	}

	void initEvent() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				for (GameObjects object : objects) {
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						if (tf) {
							yG -= 1.3f;
							break;
						}
					}
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						if (tf) {
							yG += 1.3f;
							break;
						}
					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						if (tf) {
							xG += 1f;
							break;
						}
					}
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						if (tf) {
							xG -= 1f;
							break;
						}
					}
					if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						if (tf) {
							if (System.currentTimeMillis() - beforeTime > 500L) {
								synchronized (gunA) {
									for (GameObjects obj : objects) {
										gunA.add(new Gun((int) obj.getX() + 45, (int) obj.getY() - 5));
									}
								}
								beforeTime = System.currentTimeMillis();
							}
						}
					}
					if (e.getKeyCode() == KeyEvent.VK_ENTER && tf == false) {
						fr.dispose();
						tf = true;
						break;
					}
					if (e.getKeyCode() == KeyEvent.VK_R && tf == false) {
						GameMain gm = new GameMain();
						_Runner = false;
						thread.stop();
						gm.main(null);
						fr.dispose();
						tf = true;
						break;
					}
					if (e.getKeyCode() == KeyEvent.VK_Z && tf == false) {
						startTime = System.currentTimeMillis();
						GameMain3 gm = new GameMain3();
						_Runner = false;
						fr.dispose();
						thread.stop();
						gm.main(null);
						tf = true;
						break;
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
				ob.setX(0);
				xG = 0.3f;
			} else if (ob.getX() > Width / 2 - 20) {
				ob.setX(Width / 2 - 30);
				xG = -0.3f;
			}
			if (ob.getY() < 0) {
				ob.setY(0);
			} else if (ob.getY() > Height - 40) {
				ob.setY(Height - 50);
			}
		}
	}

	private void obt() {
		for (GameObjects object : objects) {

			/*
			 * if (object.getX() >= q + 40 && object.getX() <= q + 45 && object.getY() >= w
			 * && object.getY() <= w + 60) { xG = -0.1f; object.setVy(+0.003f); tf = false;
			 * }
			 */
		}
	}

	public void gravity(ArrayList<GameObjects> objects) {
		if (objects.size() < 1) {
			return;
		}

		for (GameObjects object : objects) {
			obt();

			if (xG < 0) {
				xG += 0.01;
				object.setVx(object.getVx() + xG);
			} else if (xG > 0) {
				xG -= 0.01;
				object.setVx(object.getVx() - xG);
			}
			if (yG < 0) {
				yG += 0.03;
				object.setVy(object.getVy() + yG);
			} else if (yG > 0) {
				yG -= 0.03;
				object.setVy(object.getVy() - yG);
			}

			if (yG < 0) {
				object.setY(object.getY() + object.getVy());
			} else if (yG > 0) {
				object.setY(object.getY() - object.getVy());
			}
			if (xG < 0) {
				object.setX(object.getX() + object.getVx());

			} else if (xG > 0) {
				object.setX(object.getX() - object.getVx());
			}
			object.setVx(0);
			object.setVy(0);

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
		g.drawImage(back, 0, 0, this);
		g.setColor(Color.blue);
		synchronized (enemy) {
			for (Enemy en : enemy) {
				g.drawImage(dogImg, (int) en.getX() - 50, (int) en.getY() - 30, this);
				en.setX(en.getX() - 1);
			}
		}

		for (GameObjects obj : objects) {
			g.drawImage(player, (int) obj.getX() - 25, (int) obj.getY() - 25, this);
			ox = (int) obj.getX();
			oy = (int) obj.getY();
			
		}
		synchronized (gunA) {
			for (Gun gu : gunA) {
				g.drawImage(gun, (int) gu.getX() - 13, (int) gu.getY() - 5, this);
				gu.setX(gu.getX() + 10);
			}
		}
		Iterator<Gun> itergunA = gunA.iterator();
		while (itergunA.hasNext()) {
			Gun gu = itergunA.next();
			if (gu.getX() > Width) {
				itergunA.remove();
			}
		}
		synchronized (egun) {
			for (Gun gu : egun) {
				g.drawImage(gun2, (int) gu.getX() - 13, (int) gu.getY() - 5, this);
				gu.setX(gu.getX() - 2);
			}
		}
		Iterator<Gun> iteregun = egun.iterator();
		while (iteregun.hasNext()) {
			Gun gu = iteregun.next();
			if (gu.getX() < 0) {
				iteregun.remove();
			}
			if (gu.getX() < ox + 15 && gu.getX() > ox - 15 && gu.getY() < oy + 15 && gu.getY() > oy - 15) {
				playerhp--;
				iteregun.remove();
			}
		}
		System.out.println(playerhp);
		if (playerhp <= 0) {
			tf = false;
		}
		g.setColor(Color.BLACK);
		if (bosso == 0) {
			initBoss();
			bosso++;
		}
		time = System.currentTimeMillis() - startTime;
		if (tf) {
			time2 = time;
		}
		g.drawString("시간 : " + time2 / 1000, 410, 10);

		g.drawLine(Width / 2 - 20, 0, Width / 2 - 20, Height);
		if (tf == false) {
			g.drawString("종료 = 엔터, 처음부터 = R, 이 스테이지 다시 시작 : Z", 350, 180);
		}

		if (bossb == false) {
			g.drawImage(boss, 680, 150, this);
			g.setColor(Color.red);
			g.drawString("체력" + bosshp, 700, 130);
		}
		bossb = false;
		g.setColor(Color.red);
		ft = new Font("궁서", Font.BOLD, 50);
		g.drawString("남은 HP" + playerhp + ft.isBold(), 410, 30);

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

		GameMain3 gm = new GameMain3();
		fr.add(gm);
		fr.setVisible(true);
		gm.stater();
	}

	public void initThread() {
		int gy = (int) (Math.random() * getSize().height);
		synchronized (enemy) {
			if (tf) {
				enemy.add(new Enemy(Width, gy));
			}
		}

		new Thread(() -> {
			while (tf) {
				synchronized (gunA) {
					for (Gun gu : gunA) {
						Iterator<Enemy> iteren = enemy.iterator();

						while (iteren.hasNext()) {
							Enemy en = iteren.next();
							if (en.getX() - 30 < gu.getX() && en.getX() + 30 > gu.getX() && en.getY() - 40 < gu.getY()
									&& en.getY() + 40 > gu.getY()) {
								iteren.remove();
							}
						}
					}
				}
				if (bossb == false) {
					synchronized (gunA) {
						for (Gun gu : gunA) {
							if (gu.getX() > bosx && gu.getX() < bosx + 28 && gu.getY() > bosy
									&& gu.getY() < bosy + 80) {
								bosshp--;
								if (bosshp <= 40) {
									bost = 2500;
								}
								if (bosshp <= 0) {
									tf = false;
								}
								continue;
							}
						}
					}
				}

				try {
					Thread.sleep(ku);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(() -> {
			synchronized (enemy) {
				for (Enemy en : enemy) {
					if (en.getX() - 20 < 0) {
						tf = false;
						enemy.remove(en);
						return;
					}
				}
			}
		}).start();
	}

	private void initBoss() {
		new Thread(() -> {
			count = (int) (Math.random() * 8);
			while (tf) {
				if (time2 / 1000 >= 5) {
					bossb = false;
				}
				if (bossb == false) {
					for (int i = 0; i < bosb.length; i++) {
						bosb[i] = (int) (Math.random() * 8);

						for (int j = 0; j < i; j++) {
							if (bosb[i] == bosb[j]) {
								i--;
							} else if (bosb[i] == count) {
								i--;
							}

						}
						synchronized (egun) {
							egun.add(new Gun(Width, 50 * bosb[i]));
						}
					}

				}
				try {
					Thread.sleep(bost);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}