package kayakoAPI.pojos;

import common.annotaions.Excel;
import common.annotaions.JsonField;
import lombok.Data;
import kayakoAPI.pojos.conversationStatus.Status;

@Data
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
}
