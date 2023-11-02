public class Main {
    private static void testOneProposer() {
        System.out.println("Test 1 Proposer and 6 Acceptors");
        System.out.println("Acceptor Response Delay: 0 s");
        for (int i = 0; i < Constants.NUMBER_ACCEPTORS; ++i) {
            Acceptor acceptor = new Acceptor(Constants.NAMES[i + Constants.NUMBER_PROPOSER]);
            new AcceptorHandler(acceptor);
        }
        Proposer proposer = new Proposer(Constants.NAMES[0]);
        ProposerHandler proposerHandler = new ProposerHandler(proposer);
        proposerHandler.propose();
    }

    private static void testOneProposerWithDelay(int delay) {
        System.out.println("Test 1 Proposer and 6 Acceptors");
        System.out.println("Acceptor Response Delay: " + delay + " s");
        for (int i = 0; i < Constants.NUMBER_ACCEPTORS; ++i) {
            Acceptor acceptor = new Acceptor(Constants.NAMES[i + Constants.NUMBER_PROPOSER]);
            acceptor.setTimeDelay(delay);
            new AcceptorHandler(acceptor);
        }
        Proposer proposer = new Proposer(Constants.NAMES[0]);
        ProposerHandler proposerHandler = new ProposerHandler(proposer);
        proposerHandler.propose();
    }

    private static void testMultipleProposers(int p, int delay, int offline) {
        System.out.println("Test " + p + " Proposers and 6 Acceptors");
        System.out.println("Acceptor Response Delay: " + delay + " s");
        System.out.print("Offline Member: ");
        Proposer[] proposers = new Proposer[p];
        ProposerHandler[] proposerHandler = new ProposerHandler[p];
        for (int i = 0; i < p; ++i) {
            proposers[i] = new Proposer(Constants.NAMES[i]);
            proposerHandler[i] = new ProposerHandler(proposers[i]);
        }
        if (offline > 0) {
            for (int i = 0; i < offline; ++i) {
                System.out.print(Constants.NAMES[i + 1]);
                proposers[i + 1].setOffline(true);
            }
            System.out.println();
        } else {
            System.out.println(" Empty");
        }

        for (int i = 0; i < Constants.NUMBER_ACCEPTORS; ++i) {
            Acceptor acceptor = new Acceptor(Constants.NAMES[i + Constants.NUMBER_PROPOSER]);
            acceptor.setTimeDelay(delay);
            new AcceptorHandler(acceptor);
        }

        for (int i = 0; i < p; ++i) {
            proposerHandler[i].propose();
        }
    }
    public static void main(String[] args) {
        int p = 1;
        int t = 0;
        int o = 0;
        if (args.length == 0) {
            testOneProposer();
        } else if (args.length == 2 || args.length == 4 || args.length == 6) {
            for (int i = 0; i < args.length; i += 2) {
                if (args[i].equalsIgnoreCase("-p")) {
                    p = Integer.parseInt(args[i + 1]);
                    if (p < 1 || p > Constants.NUMBER_PROPOSER) {
                        System.out.println("Invalid number of proposers: " + p);
                        break;
                    }
                } else if (args[i].equalsIgnoreCase("-t")) {
                    t = Integer.parseInt(args[i + 1]);
                    if (t < 1 || t > Constants.MAX_DELAY) {
                        System.out.println("Invalid time delay: " + t);
                        break;
                    }
                } else if (args[i].equalsIgnoreCase("-o")) {
                    o = Integer.parseInt(args[i + 1]);
                    if (o < 0 || o >= Constants.NUMBER_PROPOSER) {
                        System.out.println("Invalid number of offlines: " + o);
                        break;
                    }
                }
            }
            if (o >= p) {
                System.out.println("Invalid number of offlines: offlines must be less than proposers");
            } else {
                if (p == 1 && t > 0) {
                    testOneProposerWithDelay(t);
                } else {
                    testMultipleProposers(p, t, o);
                }
            }
        } else {
            System.out.println("Usage java Main [-p number] [-t number] [-o number]");
        }
    }
}