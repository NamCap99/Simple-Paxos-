/**
 * request sent by the proposer
 */
public class Request {
    private String value;
    private Status status;
    private Proposer proposer;
    public Request(String value, Status status, Proposer proposer) {
        this.value = value;
        this.status = status;
        this.proposer = proposer;
    }
    public Proposer getCouncillor() {
        return proposer;
    }
    public Type getType() {
        return Type.REQUEST;
    }

    public Status getStatus() {
        return status;
    }

    public String toString() {
        return getType().name() + ";" +
                proposer.getName() + ";" +
                status.name() + ";" +
                proposer.getId() + ";" +
                value + ";";
    }
}
