import com.sun.webkit.graphics.WCImageDecoder;

import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

public class Ball implements Serializable, Drawable {
    public static final int radius = 10;
    private Vector2D position;
    private Vector2D speed;
    private Vector2D max;
    private transient PointCallback callback;

    public Ball(Vector2D position, Vector2D max, PointCallback callback) {
        this.position = position;
        this.speed = new Vector2D(1, 0);
        this.max = max;
        this.callback = callback;

        this.speed.rotate(Math.random() * 90 - 45);
    }

    public Vector2D getPosition() {
        return position;
    }

    public Direction getDirection() {
        return (speed.getX() > 0) ? Direction.RIGHT : Direction.LEFT;
    }

    public void reset() {
        position.setLocation(VectorMath.scale(max, 0.5));
        speed.setLocation((Math.random() > 0.5) ? 1 : -1, 0);
        speed.rotate(Math.random() * 90 - 45);
    }

    public void update(double dt) {
        if (getDirection() == Direction.RIGHT && position.getX() > Server.WIDTH ||
            getDirection() == Direction.LEFT && position.getX() < 0)
            callback.pointScored(getDirection());
        if (position.getY() < radius || position.getY() > max.getY() - radius)
            speed.setY(-speed.getY());

        position.add(VectorMath.scale(speed, dt / 10));
    }

    public void bounceBack(PlayerBlock playerBlock) {
        double offset = playerBlock.getPosition().getY() - position.getY();
        speed.setX(-speed.getX());
        speed.addY(-offset / 25);
        speed.scale(1.05);
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Double(position.getX() - radius, position.getY() - radius, radius * 2, radius * 2);
    }
}
