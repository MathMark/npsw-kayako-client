package kayakoAPI.pojos;

import java.util.List;

public class ExtendedConversation {

    private Conversation conversation;

    private List<Rating> ratings;

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
