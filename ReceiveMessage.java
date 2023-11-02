import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * receive message
 */
public class ReceiveMessage implements Runnable{
    private Councillor councillor;
    private ServerSocket serverSocket;

    public ReceiveMessage(Councillor councillor) {
        this.councillor = councillor;
        int port = Constants.getPortByName(councillor.getName());
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(councillor.getName() + " Listens On: " + Constants.LOCAL_HOST + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket connSocket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(connSocket.getInputStream());

                String message = dataInputStream.readUTF();

                Type type = Type.getTypeFromData(message);

                switch (type) {
                    case OFFLINE:
                        System.out.println(councillor.getName() + " Gets Offline.");
                        return;
                    case REQUEST:
                    case RESPONSE:
                        councillor.receive(message);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
