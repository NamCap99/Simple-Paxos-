
public class AcceptorHandler extends Councillor{
    private Acceptor acceptor;
    private ReceiveMessage receiveMessage;

    public AcceptorHandler(Acceptor acceptor) {
        this.acceptor = acceptor;
        receiveMessage = new ReceiveMessage(this);
        Thread thread = new Thread(receiveMessage);
        thread.start();
    }

    @Override
    public void receive(String message) {
        acceptor.receive(message);
    }

    @Override
    public String getName() {
        return acceptor.getName();
    }

    @Override
    public String getValue() {
        return acceptor.getValue();
    }
}
