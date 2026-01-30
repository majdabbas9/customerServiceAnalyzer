
import java.io.IOException;
import call_analyzer.client.*;
import call_analyzer.staff.*;
import call_analyzer.interaction.*;

public class Main {
    public static void main(String[] args) {
        try {
            Client c = new Client("a", "a", "a");
            Agent agent = new Agent("b", "b", "b", "b");
            Interaction interaction = new Interaction.Builder("call_analyzer/audio/1.mp3", "call_analyzer/audio/2.mp3")
                    .setAgent(agent)
                    .transcribeAgent()
                    .setClient(c)
                    .transcribeClient()
                    .build();
            System.out.println("Interaction completed: " + interaction);
            System.out.println("Agent Side Call: " + interaction.getAgentSideCall());
            System.out.println("Client Side Call: " + interaction.getClientSideCall());
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
