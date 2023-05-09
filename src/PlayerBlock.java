import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class PlayerBlock implements Serializable, Drawable {
    public static final int STEPSIZE = 20;
    public static final int WIDTH = 10;
    private static final int HEIGHT = 50;
    private Vector2D position;

    public PlayerBlock(Direction direction) {
        position = new Vector2D(0, Server.HEIGHT / 2);
        if (direction == Direction.LEFT)
            position.setX(WIDTH);
        else
            position.setX(Server.WIDTH - WIDTH);
    }

    public Vector2D getPosition() {
        return position;
    }

    public void move(int amount) {
        position.addY(amount);

        if (position.getY() < HEIGHT / 2.0)
            position.setY(HEIGHT / 2.0);
        else if (position.getY() > Server.HEIGHT - (HEIGHT / 2.0))
            position.setY(Server.HEIGHT - (HEIGHT / 2.0));
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(position.getX() - WIDTH / 2.0, position.getY() - HEIGHT / 2.0, WIDTH, HEIGHT);
    }

    @Override
    public String toString() {
        return "Position: " + position.toString();
    }
}
