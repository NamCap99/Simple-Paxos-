import java.util.concurrent.TimeUnit;

/**
 * Acceptor role
 */
public class Acceptor extends Councillor{
    private boolean accepted;
    private boolean isDecided;
    private int acceptedProposerNumber;
    private int timeDelay;
    private String value;
    private String name;

    /**
     *
     * @param accepted accepted proposal previously
     * @param acceptedProposerNumber accepted proposal number previously
     * @param value accepted proposal value previousl
     * @param name acceptor's name
     */
    public Acceptor(boolean accepted, int acceptedProposerNumber, String value, String name) {
        timeDelay = -1;
        this.accepted = accepted;
        this.acceptedProposerNumber = acceptedProposerNumber;
        this.value = value;
        this.name = name;
        isDecided = false;
    }

    public Acceptor(String name) {
        this(false, -1, "", name);
    }

    public void setTimeDelay(int timeDelay) {
        if (timeDelay > 0) {
            this.timeDelay = timeDelay;
        }
    }

    @Override
    public void receive(String message) {
        Response response;

        String name = message.split(";")[1];
        Status status = Status.valueOf(message.split(";")[2]);
        int propNo = Integer.parseInt(message.split(";")[3]);
        String propVal = message.split(";")[4];

        Request request = new Request(propVal, status, new Proposer(propNo, name));

        Proposer proposer = request.getCouncillor();
        StringBuilder sb = new StringBuilder();
        int port = Constants.getPortByName(proposer.getName());
        sb.append(String.format("%s reply to %s: ",
                this.name,  proposer.getName()));
        if (status == Status.PREPARE) {
            response = receivePrepare(request, sb);
        } else if (status == Status.ACCEPT_REQUEST) {
            response = receiveAcceptRequest(request, sb);
        } else if (status == Status.DECIDE) {
            response = receiveDecide(request, sb);
        } else {
            throw new RuntimeException("Unknown message format: " + message);
        }

        if (timeDelay > 0) {
            int rand = (int) (Math.random() * timeDelay);
            try {
                TimeUnit.SECONDS.sleep(rand);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(sb);
        SendMessage.send(Constants.LOCAL_HOST, port, response.toString());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public boolean isDecided() {
        return isDecided;
    }
    public boolean isAccepted() {
        return accepted;
    }

    public int getAcceptedProposerNumber() {
        return acceptedProposerNumber;
    }

    private Response receivePrepare(Request request, StringBuilder sb) {
        // if it's not accepted before.
        // 1. update the proposal ID
        // 2. reply with PREPARE_OK
        Proposer proposer = request.getCouncillor();
        String propValue = proposer.getValue();
        int propID = proposer.getId();

        // If n > any previous proposal number received from any proposer by the acceptor
        // must return a promise to ignore all future proposals having number < n
        if (!isAccepted() || propID > acceptedProposerNumber) {
            accepted = true;
            acceptedProposerNumber = propID;
            value = propValue;
            sb.append(String.format(
                    "{Proposal Number = %d, Proposal Value = %s, Status = %s, Accepted Before = False}\n",
                    acceptedProposerNumber, value, Status.PREPARE_OK.name()));
            return new Response(Status.PREPARE_OK, this);
        } else {
            sb.append(String.format(
                    "{Proposal Number = %d, Proposal Value = %s, Status = %s, Accepted Before = True}\n",
                    acceptedProposerNumber, value, Status.PREPARE_OK.name()));
            return new Response(Status.NACK, this);
        }
    }

    // when receiving ACCEPT_REQUEST
    private Response receiveAcceptRequest(Request request, StringBuilder sb) {
        Proposer proposer = request.getCouncillor();
        int propID = proposer.getId();
        if (propID >= acceptedProposerNumber) {
            sb.append(String.format(
                    "{Proposal Number = %d, Proposal Value = %s, Status = %s}\n",
                    acceptedProposerNumber, value, Status.ACCEPT_OK.name()));
            return new Response(Status.ACCEPT_OK, this);
        } else {
            sb.append(String.format(
                    "{Proposal Number = %d, Proposal Value = %s, Status = %s}\n",
                    acceptedProposerNumber, value, Status.ACCEPT_REJECT.name()));
            return new Response(Status.ACCEPT_REJECT, this);
        }
    }

    // when receiving DECIDE
    private Response receiveDecide(Request request, StringBuilder sb) {
        Proposer proposer = request.getCouncillor();
        if (!isDecided) {
            isDecided = true;
            value = proposer.getValue();
        }
        sb.append(String.format(
                "{Proposal Number = %d, Proposal Value = %s, Status = %s}\n",
                acceptedProposerNumber, value, Status.DECIDE_OK.name()));
        return new Response(Status.DECIDE_OK, this);
    }
}
