import java.io.*;
import java.net.ServerSocket;

public class Server implements PointCallback {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    public static final int PORT = 3287;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.acceptClient();
        server.acceptClient();
        server.run();
    }

    private ServerSocket serverSocket;
    private Connection player1;
    private Connection player2;
    private boolean isRunning;
    private Ball ball;
    private Vector2D score;
    private World world;

    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
        isRunning = false;
        score = new Vector2D(0, 0);
        System.out.println("Started server");
    }


    public void run() {
        ball = new Ball(new Vector2D(WIDTH / 2.0, HEIGHT / 2.0), new Vector2D(WIDTH, HEIGHT), this);
        isRunning = true;
        System.out.println("Game started");

        world = new World(player1.getPlayerBlock(), player2.getPlayerBlock(), ball, score);

        new Thread(() -> {
            while (isRunning) {
                try {
                    int input = player1.getDataInputStream().readInt();
                    player1.getPlayerBlock().move(input * PlayerBlock.STEPSIZE);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        new Thread(() -> {
            while (isRunning) {
                try {
                    int input = player2.getDataInputStream().readInt();
                    player2.getPlayerBlock().move(input * PlayerBlock.STEPSIZE);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        new Thread(() -> {
            long now;
            long last = System.currentTimeMillis();
            while (true) {
                try {
                    now = System.currentTimeMillis();
                    ball.update((now - last));
                    handleBounces();
                    updateClients();
                    last = now;
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void handleBounces() {
        if (ball.getDirection() == Direction.RIGHT &&
                ball.getPosition().getX() > WIDTH - PlayerBlock.WIDTH * 1.5 - Ball.radius &&
                ball.getShape().intersects(player1.getPlayerBlock().getShape().getBounds2D())) {
            ball.bounceBack(player1.getPlayerBlock());
        } else if (ball.getDirection() == Direction.LEFT &&
                ball.getPosition().getX() < PlayerBlock.WIDTH * 1.5 + Ball.radius &&
                ball.getShape().intersects(player2.getPlayerBlock().getShape().getBounds2D())) {
            ball.bounceBack(player2.getPlayerBlock());
        }
    }

    public void acceptClient() throws IOException {
        if (player1 == null) {
            player1 = new Connection(serverSocket.accept(), Direction.RIGHT);
            System.out.println("Player 1 connected");
        }
        else if (player2 == null) {
            player2 = new Connection(serverSocket.accept(), Direction.LEFT);
            System.out.println("Player 2 connected");
        }
        else
            System.out.println("Pong only supports 2 players");
    }


    private void updateClients() {
        player1.writeObject(world);
        player2.writeObject(world);
    }

    @Override
    public void pointScored(Direction side) {
        if (side == Direction.RIGHT)
            score.addX(1);
        else
            score.addY(1);

        ball.reset();
        System.out.println("Score: " + score.getX() + " - " + score.getY());
    }
}
