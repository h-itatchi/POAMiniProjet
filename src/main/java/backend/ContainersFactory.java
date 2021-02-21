package backend;

import java.util.ArrayList;

import backend.Agents.MyAgent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import static java.lang.Thread.sleep;

public class ContainersFactory {
    private ArrayList<AgentContainer> containers;
    private AgentContainer defaultContainer;
    private Runtime runtime;
    public final String buyer = "backend.Agents.Buyer";
    public final String seller = "backend.Agents.Seller";
    private int counter = 0;

    public ContainersFactory() {
        containers = new ArrayList<>();
        runtime = Runtime.instance();
        defaultContainer = this.createContainer("default");
    }

    public AgentContainer createContainer(String name) {
        AgentContainer agentContainer = null;
        try {
            runtime = Runtime.instance();
            ProfileImpl profileImpl = new ProfileImpl(false);
            profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
            profileImpl.setParameter(ProfileImpl.CONTAINER_NAME, name);
            agentContainer = runtime.createAgentContainer(profileImpl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        containers.add(agentContainer);
        return agentContainer;
    }

    private AgentController createAgent(AgentContainer container, String name, String type, Object parameter, WarperAgent ref) {
        AgentController agentController = null;
        if (name.isEmpty()) {
            if (type.equals(this.buyer)) {
                name = "Buyer-" + counter;
            } else if (type.equals(this.seller)) {
                name = "seller-" + counter;
            }
            counter++;
        }
        try {
            agentController = container.createNewAgent(name, type, new Object[]{parameter, ref});
            agentController.start();
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agentController;
    }

    public synchronized MyAgent createBuyer(AgentContainer container, String name, String book) {
        WarperAgent warp = new WarperAgent();
            AgentController temp = createAgent(container, name, this.buyer, book, warp);
            if (warp.getRef() != null) {
                warp.getRef().setController(temp);
            }
        return warp.getRef();
    }

    public MyAgent createSeller(AgentContainer container, String name, ArrayList<Book> books) {
        WarperAgent warp = new WarperAgent();
        AgentController temp = createAgent(container, name, this.seller, books, warp);
        if (warp.getRef() != null) {
            warp.getRef().setController(temp);
        }
        return warp.getRef();
    }

    public  AgentContainer getContainer(String name) {
        final AgentContainer[] container = {null};
        containers.forEach(c -> {
            if (c.getName().equals(name)) {
                container[0] = c;
            }
        });
        return container[0];
    }

    public AgentContainer getDefaultContainer() {
        return defaultContainer;
    }

    public ArrayList<AgentContainer> getContainers() {
        return containers;
    }

    public static class WarperAgent {
        private MyAgent ref;
        public String s = "123";

        public MyAgent getRef() {
            return ref;
        }

        public void setRef(MyAgent ref) {
            this.ref = ref;
        }
    }
}
