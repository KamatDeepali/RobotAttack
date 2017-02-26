import java.io.Serializable;

public class Grid implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x = 0;
	private int y = 0;

	public Grid() {
		this.x = 0;
		this.y = 0;
	}

	public Grid(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
