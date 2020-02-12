
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author karma
 */
public class DistanceField2D {
	
	BufferedImage input;
	float[][] distanceField;
	float shortDist;

	public DistanceField2D() {
		
		shortDist = Float.MAX_VALUE;
	}
	
	public BufferedImage getInputImage() {
		
		return input;
	}
	
	public boolean checkPixel(int x, int y) {
		
		int i = input.getRGB(x, y);
		
		return (i == 0xffffffff);
	}
	
	private boolean checkBounds(int x, int y) {
		
		return (x >= 0 && x < input.getWidth() && y >= 0 && y < input.getHeight());
	}
	
	public float checkBox(int x, int y, int spread, boolean col) {
		
		int size = spread * 2 + 1; //length and height of  the box
		float shortD = Float.MAX_VALUE;
		
		for (int i = 0; i < size; i++) {
			
			for (int j = 0; j < size; j++) {
			
				int nx = (x - spread) + j;
				int ny = (y - spread) + i;
				
				if (checkBounds(nx, ny)) {
					
					if (checkPixel(nx, ny) == col) {
						
						int dx = x - nx;
						int dy = y - ny;
						float d = (float) Math.sqrt((dx * dx) + (dy * dy));
						shortD = (d < shortD) ? d : shortD;
					}		
				}
			}
		}
		
		return (shortD < Float.MAX_VALUE) ? shortD : (float) Math.sqrt((spread * spread) * 2);
	}
		
	public float normalise(float val, float dist) {
		
		float max = dist * 2;
		
		return (val + dist) / max;
	}
	
	public void calculateDistance(int spread) {
		
		int cx = input.getWidth() / 2;
		int cy = input.getHeight() / 2;
		float maxDist = (float) Math.sqrt((spread * spread) * 2);
		
		int numPixels = input.getWidth() * input.getHeight();
		
		for (int y = 0; y < input.getHeight(); y++) {
			
			for (int x = 0; x < input.getWidth(); x++) {
				
				boolean col = checkPixel(x, y);
				
				//white
				if (col) {
					
					float d = checkBox(x, y, spread, false);
					distanceField[x][y] = normalise(d, maxDist);
					
				//black
				} else {
					
					float d = -checkBox(x, y, spread, true);
					distanceField[x][y] = normalise(d, maxDist);
				}
			}
		}
	}
	
	public void printOutputData () {
		
		System.out.println("\nOutput Array");
		
		for (int y = 0; y < distanceField.length; y++) {
			
			for (int x = 0; x < distanceField[0].length; x++) {
				
				System.out.print(String.format("%.2f ",distanceField[x][y]));
			}
			
			System.out.println("");
		}
	}
	
	public void printInputData() {
		
		System.out.println("\nInput Image");
		
		for (int y = 0; y < input.getHeight(); y++) {
			
			for (int x = 0; x < input.getWidth(); x++) {
			
				int i = input.getRGB(x, y);
				
				if (i == 0xffffffff) {
					
					System.out.print("1");
					
				} else {
					
					System.out.print("0");
				}
			}
			
			System.out.println("");
		}
	}
	
	public void setInputImage(String path) {
			
		File f = new File(path);
		
		try {
			
			input = ImageIO.read(f);
			distanceField = new float[input.getWidth()][input.getHeight()];
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
		}
	}
	
	public BufferedImage generateOuputImage(int w, int h) {
		
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		for (int y = 0; y < h; y++) {
			
			for (int x = 0; x < w; x++) {
				
				//uv of new image
				float u = (float) x / w;
				float v = (float) y / h;
				
				//width and height of distance field array
				int width = distanceField[0].length;
				int height = distanceField.length;
				
				//x an y values to look up in distance array
				int nx = (int) (u * width);
				int ny = (int) (v * height);
				
				float d = distanceField[nx][ny];
				
				int val = (int) (d * 255);
				
				int col = 0;
				
				col = col | 0xff; //a
				col = col << 8;
				col = col | val; //r
				col = col << 8;
				col = col | val; //g
				col = col << 8;
				col = col | val; //b
				
				img.setRGB(x, y, col);	
			}
			
			System.out.println("");
		}
		
		return img;
	}
}
