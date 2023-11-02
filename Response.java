/**
 * response sent by the acceptor
 */
public class Response {
    private Status status;
    private Acceptor acceptor;

    public Response(Status status, Acceptor acceptor) {
        this.status = status;
        this.acceptor = acceptor;
    }

    public Acceptor getCouncillor() {
        return acceptor;
    }

    public String toString() {
        String res = getType().name() + ";" +
                acceptor.getName() + ";" +
                status.name() + ";";

        res +=  acceptor.isDecided() + ";" +
                acceptor.getAcceptedProposerNumber() + ";" +
                acceptor.getValue() + ";";

        return res;
    }

    public Type getType() {
        return Type.RESPONSE;
    }

    public Status getStatus() {
        return status;
    }
}
