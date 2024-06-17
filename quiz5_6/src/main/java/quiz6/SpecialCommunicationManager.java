package quiz6;

import quiz5.CommunicationManager;
import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.List;

public class SpecialCommunicationManager implements CommunicationManager {
    private static final String COMMON_SERVICE_URL = "http://common-chatbot-service-url";
    private static final String SPECIAL_SERVICE_URL = "http://special-chatbot-service-url";
    private CloseableHttpClient httpClient;

    public SpecialCommunicationManager() {
        this.httpClient = HttpClients.createDefault();
    }

    @Override
    public String sendMessage(List<String> chatHistory, String userMessage) throws Exception {
        String serviceUrl = shouldUseSpecialService(chatHistory, userMessage) ? SPECIAL_SERVICE_URL : COMMON_SERVICE_URL;
        HttpPost postRequest = new HttpPost(serviceUrl);

        String requestBody = new Gson().toJson(new ChatRequest(chatHistory, userMessage));
        StringEntity entity = new StringEntity(requestBody);
        postRequest.setEntity(entity);
        postRequest.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                ChatResponse chatResponse = new Gson().fromJson(responseBody, ChatResponse.class);
                return chatResponse.getResponse();
            } else {
                throw new RuntimeException("Failed: HTTP error code: " + response.getStatusLine().getStatusCode());
            }
        }
    }

    private boolean shouldUseSpecialService(List<String> chatHistory, String userMessage) {
        return userMessage.toLowerCase().contains("help") || chatHistory.stream().anyMatch(message -> message.toLowerCase().contains("help"));
    }

    static class ChatRequest {
        private List<String> chatHistory;
        private String userMessage;

        public ChatRequest(List<String> chatHistory, String userMessage) {
            this.chatHistory = chatHistory;
            this.userMessage = userMessage;
        }

        public List<String> getChatHistory() {
            return chatHistory;
        }

        public void setChatHistory(List<String> chatHistory) {
            this.chatHistory = chatHistory;
        }

        public String getUserMessage() {
            return userMessage;
        }

        public void setUserMessage(String userMessage) {
            this.userMessage = userMessage;
        }
    }

    static class ChatResponse {
        private String response;

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
}
