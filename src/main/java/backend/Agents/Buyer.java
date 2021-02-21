package backend.Agents;

import backend.Behaviours.RequestBehaviour;
import backend.ContainersFactory;
import frontend.Logger;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Buyer extends MyAgent {
    private ParallelBehaviour parallelBehaviour;
    private String book;
    private int requesterCount;
    private Logger logger = new Logger();

    @Override
    protected void setup() {
        logger.println("setup Agent Buyer :" + this.getAID().getName());
        Object[] args = getArguments();
        if (args.length == 2) {
            if (args[0] != null) {
                book = (String) args[0];
            }
            ContainersFactory.WarperAgent warp =((ContainersFactory.WarperAgent) args[1]);
            warp.s = "963";
            warp.setRef(this);
            logger.println("I'm waiting to buy the Book : " + book);
        } else {
            doDelete();
        }
        parallelBehaviour = new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate
                        template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage aclMessage = receive(template);
                if (aclMessage != null) {
                    logger.println("Receiving MSG :" + myAgent.getAID().getName());
                    String livre = aclMessage.getContent();
                    AID requester = aclMessage.getSender();
                    ++requesterCount;
                    String conversationID = "transaction_" + livre + "_" + requesterCount;
                    parallelBehaviour.addSubBehaviour(
                            new RequestBehaviour(myAgent, livre, requester, conversationID));
                } else {
                    block();
                }
            }
        });
        addBehaviour(parallelBehaviour);
    }

    public void buyBook(String name){
        ++requesterCount;
        String conversationID = "transaction_" + name + "_" + requesterCount;
        parallelBehaviour.addSubBehaviour(
                new RequestBehaviour(this, name, this.getAID(), conversationID));
    }
}
