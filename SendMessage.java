import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * send message
 */
public class SendMessage {
    public static void send(String host, int serverPort, String message) {
        int retry = 0;
        boolean succeed = false;
        while (retry < 3 && !succeed) {
            try {
                Socket socket = new Socket(host, serverPort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
                dataOutputStream.close();
                socket.close();
                succeed = true;
            } catch (IOException e) {
                retry++;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }
}
