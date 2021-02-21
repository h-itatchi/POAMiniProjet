package backend.Agents;

import jade.core.Agent;
import jade.wrapper.AgentController;

public class MyAgent extends Agent {
    private AgentController controller;

    public AgentController getController() {
        return controller;
    }

    public void setController(AgentController controller) {
        this.controller = controller;
    }
}
