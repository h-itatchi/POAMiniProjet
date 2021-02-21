package backend.Agents;

import backend.Behaviours.SellerBehaviour;
import backend.Book;
import backend.ContainersFactory;
import frontend.Logger;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Random;

public class Seller extends MyAgent {
    public static ArrayList<AID> sellers = new ArrayList<>();
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private ParallelBehaviour parallelBehaviour;
    private Logger logger = new Logger();

    @Override
    protected void setup() {
        logger.println("setup Agent Seller :" + this.getAID().getName());
        Random r = new Random();
        Object[] args = getArguments();
        if (args.length >= 1) {
            if (args[0] != null) {
                books = (ObservableList<Book>) args[0];
            }
            ContainersFactory.WarperAgent warp =((ContainersFactory.WarperAgent) args[1]);
            warp.setRef(this);
        } else {
            books.add(new Book("XML", 230 + Math.random() * 200, r.nextInt(10)));
            books.add(new Book("JAVA", 460 + Math.random() * 200, r.nextInt(10)));
            books.add(new Book("IOT", 540 + Math.random() * 200, r.nextInt(10)));
            sellers.add(this.getAID());
        }
        // Service
        DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(this.getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("book-selling");
        serviceDescription.setName("book-trading");
        agentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, agentDescription);
        } catch (FIPAException e1) {
            e1.printStackTrace();
        }

        // Behaviour
        parallelBehaviour = new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                try {
                    MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
                    ACLMessage aclMessage = receive(messageTemplate);
                    if (aclMessage != null) {
                        logger.println("Receiving MSG :" + myAgent.getAID().getName());
                        logger.println("Conversation ID:" + aclMessage.getConversationId());
                        String book = aclMessage.getContent();
                        Double price = 0.0;
                        for (Book b : books) {
                            if (b.getName().equals(book)) {
                                price = b.getPrice();
                                break;
                            }
                        }
                        ACLMessage reply = aclMessage.createReply();
                        reply.setPerformative(ACLMessage.PROPOSE);
                        reply.setContent(price.toString());
                        logger.println("...... En cours");
                        Thread.sleep(5000);
                        send(reply);
                        parallelBehaviour.addSubBehaviour(new SellerBehaviour(myAgent, aclMessage.getConversationId()));
                    } else {
                        logger.println("Blocking:" + myAgent.getAID().getName());
                        block();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addBehaviour(parallelBehaviour);
    }

    public ObservableList<Book> getBooks() {
        return books;
    }

    public void setBooks(ObservableList<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        for (Book b : books) {
            if (b.getName().equals(book)) {
                b.setQuantity(b.getQuantity() + book.getQuantity());
                return;
            }
        }
        books.add(book);
    }
}
