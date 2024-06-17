package quiz5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserInteractionManager {
    private CommunicationManager communicationManager;
    private List<String> chatHistory;

    public UserInteractionManager(CommunicationManager communicationManager) {
        this.communicationManager = communicationManager;
        this.chatHistory = new ArrayList<>();
    }

    public void startChat() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("User: ");
                String userInput = reader.readLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }
                chatHistory.add("User: " + userInput);
                String chatbotResponse = communicationManager.sendMessage(chatHistory, userInput);
                chatHistory.add("Chatbot: " + chatbotResponse);
                System.out.println("Chatbot: " + chatbotResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CommunicationManager communicationManager = new DummyCommunicationManager();
        UserInteractionManager userInteractionManager = new UserInteractionManager(communicationManager);
        userInteractionManager.startChat();
    }
}
