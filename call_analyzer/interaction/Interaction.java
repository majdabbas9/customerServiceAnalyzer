package call_analyzer.interaction;

import call_analyzer.client.Client;
import call_analyzer.staff.Agent;
import java.io.IOException;

public class Interaction {
    private final int interactionId;
    private final String clientSideCall;
    private final String agentSideCall;
    private final Client client;
    private final Agent agent;

    private Interaction(Builder builder) {
        this.interactionId = builder.interactionId;
        this.clientSideCall = builder.clientSideCall;
        this.agentSideCall = builder.agentSideCall;
        this.client = builder.client;
        this.agent = builder.agent;
    }

    public static class Builder {
        private static int nextInteractionId = 1;
        private final int interactionId;
        private CallTranscriber transcriber;
        private String clientSideCall;
        private String agentSideCall;
        private Client client;
        private Agent agent;

        public Builder(String clientFile, String agentFile) throws IOException, InterruptedException {
            this.interactionId = nextInteractionId++;
            this.transcriber = new CallTranscriber(clientFile, agentFile);
        }

        public Builder setClient(Client client) {
            this.client = client;
            return this;
        }

        public Builder setAgent(Agent agent) {
            this.agent = agent;
            return this;
        }

        public Builder transcribeClient() throws IOException, InterruptedException {
            this.clientSideCall = this.transcriber.transcribeClient();
            return this;
        }

        public Builder transcribeAgent() throws IOException, InterruptedException {
            this.clientSideCall = this.transcriber.transcribeAgent();
            return this;
        }

        public Interaction build() {
            return new Interaction(this);
        }
    }

    // Getters
    public int getInteractionId() {
        return interactionId;
    }

    public String getClientSideCall() {
        return clientSideCall;
    }

    public String getAgentSideCall() {
        return agentSideCall;
    }

    public Client getClient() {
        return client;
    }

    public Agent getAgent() {
        return agent;
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "id=" + interactionId +
                ", client=" + (client != null ? client.getName() : "null") +
                ", agent=" + (agent != null ? agent.getName() : "null") +
                ", hasClientCall=" + (clientSideCall != null && !clientSideCall.isEmpty()) +
                ", hasAgentCall=" + (agentSideCall != null && !agentSideCall.isEmpty()) +
                '}';
    }
}
