import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;


public class Simulation extends JFrame implements Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2666673049691980199L;
	private int DELAY = 50;
	private int MAX_DURATION = 50;
	private int MIN_DURATION = 40;
	private double MAX_VELOCITY = 10;
	private double TRAIL_LENGTH = 8;
	

	private class Snake  {
		private double x,y;
		private final int radius = 5;
		private int duration;
		private double dx,dy;  
		private Color c,c_path;
		private LinkedList<Point> tocki;
		private int color = 0;
		
		public Snake( JFrame parent ) {
			this.x = parent.getWidth()/2;
			this.y = parent.getHeight()/2;
			moveRandom();
			
			c_path = Color.black;
			tocki = new LinkedList<Point>();
			this.color = (int)(Math.random()*4);
			//this.color = 3;
			if (color == 0)
				this.c = new Color(255,0,0);
			if (color == 1)
				this.c = new Color(0,255,0);
			if (color == 2)
					this.c = new Color(0,0,255);	
			if (color == 3)
				this.c = new Color(255,255,255);	
		}

		private void moveRandom() {
			duration = (int)(Math.random()*(MAX_DURATION-MIN_DURATION))+MIN_DURATION;
			double radius =  Math.random()*MAX_VELOCITY-(MAX_VELOCITY/2);
			double direction = Math.random()*6.29;
			dx = radius*Math.cos(direction);
			dy = radius*Math.sin(direction);
//			dx = Math.random()*MAX_VELOCITY-(MAX_VELOCITY/2);
//			dy = Math.random()*MAX_VELOCITY-(MAX_VELOCITY/2);
		}
		
		private void update() {
			--duration;
			if ( tocki.size() > TRAIL_LENGTH )
				tocki.removeLast();
			tocki.addFirst(new Point((int)x,(int)y));
			x += dx;
			y += dy;
			if ( duration == 0 ) moveRandom();
		}
		
		public void paint ( Graphics g ) {
			update();
			g.setColor(c_path);
			double stroke = radius*2-4;
			double dstroke = -stroke/TRAIL_LENGTH;
			double color = 150;
			double dcolor = (220-color)/TRAIL_LENGTH;
//			this.color = 3;
			for ( int i = 1 ; i < tocki.size() ; ++i ) {
				color += dcolor;
				if ( this.color == 0 )
					g.setColor(new Color(255,(int)color,(int)color));
				if ( this.color == 1 )
					g.setColor(new Color((int)color,255,(int)color));
				if ( this.color == 2 )
					g.setColor(new Color((int)color,(int)color,255));
				if ( this.color == 3 )
					g.setColor(new Color((int)color,(int)color,(int)color));
				Point p1 = tocki.get(i);
				Point p2 = tocki.get(i-1);
				stroke += dstroke;
				((Graphics2D)g).setStroke(new BasicStroke((int)stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
			g.setColor(c);
			g.fillOval((int)(x-radius), (int)(y-radius), radius*2, radius*2);
		}
		
	}
	
	private ArrayList<Snake> topki;
	private int n = 0;
	
	public Simulation ( int n ) {
		this.setTitle("Flying snakes");
		this.n = n;
		this.setBackground(Color.black);
		this.setSize(700,500);
		this.setResizable(false);
		this.setVisible(true);
		topki = new ArrayList<Snake>();
		for ( int i = 0 ; i < n ; ++i ) {
			topki.add(new Snake(this));
		}
		Thread trd = new Thread(this);
		trd.start();
		
		
	}

	@Override
	public void run() {
		while ( true ) {
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		if ( offscreen == null )  {
			offscreen = createImage(getWidth(), getHeight());
		}
		super.paint(offscreen.getGraphics());
		offscreen.getGraphics().setColor(Color.BLACK);
		offscreen.getGraphics().fillRect(0, 0, getWidth(), getHeight());
		for ( Snake t : topki ) {
			t.paint(offscreen.getGraphics());
		}
		g.drawImage(offscreen, 0, 0, getWidth(), getHeight(), null);
	}
	Image offscreen;
	
	public static void main(String[] args) {
		new Simulation(300);
	}
	
}