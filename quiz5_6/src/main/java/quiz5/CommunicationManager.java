package quiz5;

import java.util.List;

public interface CommunicationManager {
    String sendMessage(List<String> chatHistory, String userMessage) throws Exception;
}
