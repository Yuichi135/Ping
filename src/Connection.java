import java.io.*;
import java.net.Socket;

public class Connection {
    private Socket client;
    private ObjectOutputStream out;
    private DataInputStream in;
    private PlayerBlock playerBlock;

    public Connection(Socket client, Direction side) {
        try {
            this.client = client;
            this.out = new ObjectOutputStream(client.getOutputStream());
            this.in = new DataInputStream(client.getInputStream());
            this.playerBlock = new PlayerBlock(side);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return client;
    }

    public PlayerBlock getPlayerBlock() {
        return playerBlock;
    }

    public DataInputStream getDataInputStream() {
        return in;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return out;
    }

    public void writeObject(Object o) {
        try {
            out.writeObject(o);
            out.reset();
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
