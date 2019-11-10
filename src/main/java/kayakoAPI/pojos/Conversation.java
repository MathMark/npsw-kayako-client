package kayakoAPI.pojos;

import common.annotaions.Excel;
import common.annotaions.JsonField;
import pojos.conversationStatus.Status;

public class Conversation {

    @Excel(columnName = "Link")
    private String conversationLink;

    @JsonField(path = "/subject")
    @Excel(columnName = "Subject")
    private String subject;

    @Excel(columnName = "Status")
    private Status status;

    @Excel(isCustomObject = true)
    private User requester;

    @Excel(columnName = "Agent")
    private Agent assignedAgent;

    @Excel(columnName = "State")
    @JsonField(path = "/state")
    private String state;

    @Excel(columnName = "Rating")
    @JsonField(path = "/rating")
    private String rating;

    @JsonField(path = "/created_at")
    @Excel(columnName = "Created at")
    private String createdAt;

    @JsonField(path = "/updated_at")
    @Excel(columnName = "Updated at")
    private String updatedAt;

    //#ids
    @JsonField(path = "/id")
    private Integer conversationId;

    @JsonField(path = "/requester/id")
    private Integer requesterId;

    @JsonField(path = "/status/id")
    private Integer statusId;

    @JsonField(path = "/assigned_agent/id")
    private Integer assignee_id;

    @Override
    public String toString() {
        return "Conversation{" +
                "createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", subject='" + subject + '\'' +
                ", requester=" + requester +
                ", status=" + status +
                '}';
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Integer requesterId) {
        this.requesterId = requesterId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Agent getAssignedAgent() {
        return assignedAgent;
    }

    public void setAssignedAgent(Agent assignedAgent) {
        this.assignedAgent = assignedAgent;
    }

    public Integer getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(Integer assignee_id) {
        this.assignee_id = assignee_id;
    }

    public String getConversationLink() {
        return conversationLink;
    }

    public void setConversationLink(String conversationLink) {
        this.conversationLink = conversationLink;
    }

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
