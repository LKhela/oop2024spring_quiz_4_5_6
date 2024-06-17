package quiz5;

import java.util.List;

public class DummyCommunicationManager implements CommunicationManager {
    @Override
    public String sendMessage(List<String> chatHistory, String userMessage) {
        return "This is a dummy response.";
    }
}
