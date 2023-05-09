import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Application {
    private Canvas canvas;
    private FXGraphics2D graphics;
    private Socket client;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(new Group(canvas)));
        stage.setTitle("Client");
        stage.show();
        stage.setResizable(false);
        canvas.requestFocus();
    }

    @Override
    public void init() throws Exception {
        canvas = new Canvas(Server.WIDTH, Server.HEIGHT);
        graphics = new FXGraphics2D(canvas.getGraphicsContext2D());
        graphics.setBackground(Color.BLACK);
        graphics.setColor(Color.WHITE);

        client = new Socket("127.0.0.1", Server.PORT);
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        output.flush();
        canvas.setOnKeyPressed(e -> {
            try {
                switch (e.getCode()) {
                    case UP:
                        output.writeInt(-1);
                        break;
                    case DOWN:
                        output.writeInt(1);
                        break;
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
        new Thread(() -> {
            while (true) {
                try {
                    World world = (World) objectInputStream.readObject();
                    graphics.clearRect(0, 0, Server.WIDTH, Server.HEIGHT);
                    world.draw(graphics);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
