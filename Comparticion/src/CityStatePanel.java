import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.HashSet;
import java.util.List;

import javax.swing.JPanel;

import IA.Comparticion.Usuarios;

public class CityStatePanel extends JPanel implements Runnable{
	
	private static final int FRAME_DURATION_MS = 16;
	private static final float CAR_SPEED = 0.5f;
	private static final int N_BLOCKS = 100;
	//private static final int BLOCK_MARGIN_SIZE = 1;
	//private static final int BLOCK_CORNER_SIZE = 1;
	private static final boolean DRAW_TRAILS = true;
	private static final boolean DRAW_TRAGET_PATH = true;
	
	private int width;
	private int height;
	private Usuarios usuarios;
	private ComparticionState state;
	private int block_h = height/N_BLOCKS;
	private int block_w = width/N_BLOCKS;
	private Thread animator;

	private int t;
	
	public CityStatePanel(Usuarios usuarios, ComparticionState state, int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.usuarios = usuarios;
		this.state = state;
		this.block_h = height/N_BLOCKS;
		this.block_w = width/N_BLOCKS;
		
		setBackground(Color.decode("#fcfcfc"));
	}
	
	@Override
	public void addNotify() {
	    super.addNotify();
	    t = 0;
	    animator = new Thread(this);
	    animator.start();
	}
	
	public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g); 
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(new Font("default", Font.BOLD, 12));
        
        drawSimpleCityBlocks(g);
        
        g.setColor(new Color(32, 32, 160, 80));
        drawPassengers(g);
        
        g.setColor(new Color(160, 32, 32, 80));
        drawPassengersOut(g);
        
        drawCars(g);
    }
	
	private void drawSimpleCityBlocks(Graphics g) {
		g.setColor(Color.decode("#eeeeee"));
		((Graphics2D) g).setStroke(new BasicStroke(2));
		for(int y = 0; y <= N_BLOCKS; y++) {
			g.drawLine(0, y*block_h-block_h/2, width, y*block_h-block_h/2);
		}
		for(int x = 0; x <= N_BLOCKS; x++) {
			g.drawLine(x*block_w-block_w/2, 0, x*block_w-block_w/2, height);
		}
	}
	
	/** Very costly for large grid sizes (e.g. 100). But produces better visual results for smaller grid sizes. 
	 * private void drawCityBlocks(Graphics g) {
		g.setColor(Color.decode("#eeeeee"));
		for(int y = 0; y <= N_BLOCKS; y++) {
			for(int x = 0; x <= N_BLOCKS; x++) {
				g.fillPolygon(getCityBlock(x*block_w-block_w/2, y*block_h-block_h/2, block_w, block_h, BLOCK_MARGIN_SIZE, BLOCK_CORNER_SIZE));
			}
		}
	}**/
	
	private static Polygon getCityBlock(int x, int y, int w, int h, int m, int c) {
		Polygon block = new Polygon();
		block.addPoint(x+m+c, y+m);
		block.addPoint(x+w-m-c, y+m);
		block.addPoint(x+w-m, y+m+c);
		block.addPoint(x+w-m, y+h-m-c);
		block.addPoint(x+w-m-c, y+h-m);
		block.addPoint(x+m+c, y+h-m);
		block.addPoint(x+m, y+h-m-c);
		block.addPoint(x+m, y+m+c);
		return block;
	}
	
	private void drawCars(Graphics g) {
		
		for(int c = 0; c < state.getassignments().size(); c++) {
			List<Integer> car = state.getassignments().get(c);
			int d = 0;
			HashSet<Integer> aux = new HashSet<Integer>();
	        int previous_x = usuarios.get(car.get(0)).getCoordOrigenX();
	        int previous_y = usuarios.get(car.get(0)).getCoordOrigenY();
	        aux.add(car.get(0));
	        int target_x, target_y;
	        for(int i = 1; i < car.size(); ++i){
	        	int x, y;
	            if(aux.contains(car.get(i))){
	                target_x = usuarios.get(car.get(i)).getCoordDestinoX();
	                target_y = usuarios.get(car.get(i)).getCoordDestinoY();
	            } else{
	                target_x = usuarios.get(car.get(i)).getCoordOrigenX();
	                target_y=  usuarios.get(car.get(i)).getCoordOrigenY();
	                aux.add(car.get(i));
	            }
	            int x_dir = previous_x < target_x ? 1 : -1;
	            int y_dir = previous_y < target_y ? 1 : -1;
	            float d_x = (Math.abs(previous_x-target_x)) / CAR_SPEED;
	            float d_y = (Math.abs(previous_y-target_y)) / CAR_SPEED;
	            float d_tt = d_x + d_y;
	            
	            if(d <= t && d + d_tt > t) {
	            	
	            	float d_t = (t - d);
	            	
	            	if(d_t <= d_x) {
	            		x = mapXToDisplay(previous_x + d_t*x_dir*CAR_SPEED);
	            		y = mapYToDisplay(previous_y);
	            		if(DRAW_TRAILS) {
	            			((Graphics2D) g).setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	            			g.setColor(new Color(200, 0, 0, 60));
	            			g.drawLine(mapXToDisplay(previous_x), mapYToDisplay(previous_y), x, y);
	            		}
	            		if(DRAW_TRAGET_PATH) {
	            			((Graphics2D) g).setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{9}, 0));
	            			g.setColor(new Color(255, 0, 0, 40));
	            			g.drawLine(mapXToDisplay(target_x), y, x, y);
	            			g.drawLine(mapXToDisplay(target_x), mapYToDisplay(target_y), mapXToDisplay(target_x), y);
	            		}
	            	}else {
	            		x = mapXToDisplay(target_x);
		            	y = mapYToDisplay(previous_y + (d_t - d_x)*y_dir*CAR_SPEED);
	            		if(DRAW_TRAILS) {
	            			((Graphics2D) g).setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	            			g.setColor(new Color(200, 0, 0, 60));
	            			g.drawLine(mapXToDisplay(previous_x), mapYToDisplay(previous_y), x, mapYToDisplay(previous_y));
	            			g.drawLine(x, mapYToDisplay(previous_y), x, y);
	            		}
	            		if(DRAW_TRAGET_PATH) {
	            			((Graphics2D) g).setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{9}, 0));
	            			g.setColor(new Color(255, 0, 0, 40));
	            			g.drawLine(mapXToDisplay(target_x), mapYToDisplay(target_y), x, y);
	            		}
	            	}
	            	g.setColor(Color.decode("#202020"));
	            	drawCenteredCircle(g, x, y, 16);
	            	g.setColor(Color.WHITE);
	            	//g.drawString(String.format("%02d %02f / %02f", c, (t*CAR_SPEED), ((d_tt+d)*CAR_SPEED)), x-8, y+4);
	            	g.drawString(String.format("%02d", c), x-8, y+4);
	            	break;
	            }
	            d += d_tt;
	            previous_x = target_x;
	            previous_y = target_y;
	        }
			
		}
	}
	
	private int mapXToDisplay(float x) {
		return (int) (x*block_w+block_w/2);
	}
	
	private int mapYToDisplay(float y) {
		return (int) (y*block_h+block_h/2);
	}
	
	private void drawPassengers(Graphics g) {
		for(int i = 0; i < usuarios.size(); i++) {
			if(!isActualDriver(i)) {
				int x = usuarios.get(i).getCoordOrigenX() * block_w + block_w/2;
				int y = usuarios.get(i).getCoordOrigenY() * block_h + block_h/2;
				drawCenteredCircle(g, x, y, 8);
			}
		}
	}
	
	private void drawPassengersOut(Graphics g) {
		for(int i = 0; i < usuarios.size(); i++) {
			//if(!isActualDriver(i)) {
				int x = usuarios.get(i).getCoordDestinoX() * block_w + block_w/2;
				int y = usuarios.get(i).getCoordDestinoY() * block_h + block_h/2;
				drawCenteredCircle(g, x, y, 8);
			//}
		}
	}
	
	private boolean isActualDriver(int i) {
		for(List<Integer> l : state.getassignments()) {
			if(l.get(0) == i) {
				return true;
			}
		}
		return false;
	}
	
	private static void drawCenteredCircle(Graphics g, int x, int y, int r) {
		x = x-(r/2);
		y = y-(r/2);
		g.fillOval(x,y,r,r);
	}
	
	@Override
	public void run() {
		
		long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();
        
		while (true) {
			t++;
			repaint();
			
			timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = FRAME_DURATION_MS - timeDiff;
            if(sleep < 0) sleep = 2;
            
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            beforeTime = System.currentTimeMillis();
		}
		
	}

}
