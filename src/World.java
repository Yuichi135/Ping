import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.io.Serializable;

public class World implements Serializable {
    private final PlayerBlock p1;
    private final PlayerBlock p2;
    private final Ball ball;
    private final Vector2D score;

    public World(PlayerBlock p1, PlayerBlock p2, Ball ball, Vector2D score) {
        this.p1 = p1;
        this.p2 = p2;
        this.ball = ball;
        this.score = score;
    }

    public void draw(FXGraphics2D graphics) {
        graphics.fill(p1.getShape());
        graphics.fill(p2.getShape());
        graphics.fill(ball.getShape());

        FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
        String string = (int) score.getX() + " - " + (int) score.getY();
        graphics.drawString(string, Server.WIDTH / 2 - metrics.stringWidth(string) / 2, graphics.getFont().getSize());
    }
}
