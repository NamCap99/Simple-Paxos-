public class ProposerHandler extends Councillor {
    private Proposer proposer;
    private ReceiveMessage receiveMessage;

    public ProposerHandler(Proposer proposer) {
        this.proposer = proposer;
        receiveMessage = new ReceiveMessage(this);
        Thread thread = new Thread(receiveMessage);
        thread.start();
    }

    @Override
    public void receive(String message) {
        proposer.receive(message);
    }

    @Override
    public String getName() {
        return proposer.getName();
    }

    @Override
    public String getValue() {
        return proposer.getValue();
    }

    public void propose() {
        proposer.propose();
    }
}
