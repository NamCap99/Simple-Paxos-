import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Proposer role
 */
public class Proposer extends Councillor{
    private int id;
    private int receiveCount;
    private boolean offline;
    private Status status;
    private String value;
    private String name;
    private Map<Status, Integer> statusCountMap;

    public Proposer(String name) {
        this(GenerateId.getId(), name);
    }

    /**
     *
     * @param id proposal ID
     * @param name proposer's name
     */
    public Proposer(int id, String name) {
        this.offline = false;
        this.status = Status.PREPARE;
        this.id = id;
        this.name = name;
        this.statusCountMap = new ConcurrentHashMap<>();
        this.value = getName() + " Becomes President!";
    }

    @Override
    public void receive(String message) {
        // ignore the message if offline
        if (offline) {
            return;
        }
        receiveCount++;

        String name = message.split(";")[1];
        Status status = Status.valueOf(message.split(";")[2]);
        boolean accepted = Boolean.parseBoolean(message.split(";")[3]);
        int acceptedPropNo = Integer.parseInt(message.split(";")[4]);
        String acceptedValue = message.split(";")[5];

        Response response = new Response(status, new Acceptor(accepted, acceptedPropNo, acceptedValue, name));

        Status rcvStatus = response.getStatus();

        // check receive status
        if (rcvStatus == Status.PREPARE_OK) {
            receivePrepareOK();
        } else if (rcvStatus == Status.NACK) {
            receiveNACK();
        } else if (rcvStatus == Status.ACCEPT_OK) {
            receiveAcceptOK();
        } else if (rcvStatus == Status.ACCEPT_REJECT) {
            receiveAcceptReject();
        } else if (rcvStatus == Status.DECIDE_OK) {
            this.value = response.getCouncillor().getValue();
            System.out.println("Present is " + this.value);
        }
    }

    private void receivePrepareOK() {
        int count = statusCountMap.getOrDefault(Status.PREPARE_OK, 0) + 1;
        // if received PREPARE_OK from the majority
        // 1. change status to ACCEPT_REQUEST and send proposal to acceptors again
        // 2. clear the status count since new proposal is sent
        // 3. Proposal ID should not change, and also the proposal value for this situation
        if (count > Constants.NUMBER_ACCEPTORS / 2 && receiveCount == Constants.NUMBER_ACCEPTORS) {
            status = Status.ACCEPT_REQUEST;
            propose();
        } else {
            statusCountMap.put(Status.PREPARE_OK, count);
        }
    }

    // when received NACK
    private void receiveNACK() {
        int count = statusCountMap.getOrDefault(Status.NACK, 0) + 1;
        // if received NACK from the majority
        // 1. need to update the proposal ID and send again
        // 2. update the status
        if (count > Constants.NUMBER_ACCEPTORS / 2 && receiveCount == Constants.NUMBER_ACCEPTORS) {
            status = Status.PREPARE;
            id = GenerateId.getId();
            propose();
        } else {
            statusCountMap.put(Status.NACK, count);
        }
    }

    // when received ACCEPT_OK
    private void receiveAcceptOK() {
        int count = statusCountMap.getOrDefault(Status.ACCEPT_OK, 0) + 1;
        // if received ACCEPT_OK from the majority
        // 1. need to send DECIDE request to the acceptors
        // 2. need to update the status
        if (count > Constants.NUMBER_ACCEPTORS / 2 && receiveCount == Constants.NUMBER_ACCEPTORS) {
            status = Status.DECIDE;
            propose();
        } else {
            statusCountMap.put(Status.ACCEPT_OK, count);
        }
    }

    // when received ACCEPT_REJECT
    private void receiveAcceptReject() {
        int count = statusCountMap.getOrDefault(Status.ACCEPT_REJECT, 0) + 1;
        // if received ACCEPT_REJECT from the majority
        // 1. need to update the proposal ID and send again
        // 2. update the status
        if (count > Constants.NUMBER_ACCEPTORS / 2 && receiveCount == Constants.NUMBER_ACCEPTORS) {
            status = Status.PREPARE;
            id = GenerateId.getId();
            propose();
        } else {
            statusCountMap.put(Status.ACCEPT_REJECT, count);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public int getId() {
        return id;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
        int port = Constants.getPortByName(getName());
        SendMessage.send(Constants.LOCAL_HOST, port, Type.OFFLINE.name());
    }

    public void propose() {
        this.receiveCount = 0;
        this.statusCountMap.clear();
        Request request = new Request(value, status, this);
        for (int i = 0; i < Constants.NUMBER_ACCEPTORS; ++i) {
            SendMessage.send(Constants.LOCAL_HOST, Constants.PORTS[i + Constants.NUMBER_PROPOSER], request.toString());
            System.out.printf(getName() +
                            " sending proposal to %s: {Proposal Number = %d, Proposal Value = %s, Status = %s}\n",
                    Constants.NAMES[i + Constants.NUMBER_PROPOSER], id, value, status.name());
        }
    }
}
