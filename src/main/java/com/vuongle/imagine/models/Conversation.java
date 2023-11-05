package com.vuongle.imagine.models;

import com.vuongle.imagine.constants.ChatType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("conversation")
public class Conversation {

    @Id
    @NotNull
    private ObjectId id;

    @NotNull
    private String name;

    @NotNull
    private List<ObjectId> members = new ArrayList<>();

    @NotNull
    private ChatType type = ChatType.PRIVATE;

    private boolean deleted;

    @LastModifiedDate
    private Instant timeStamp;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;

    public void addMember(ObjectId memberId) {

        if (type.equals(ChatType.PRIVATE) && members.size() == 2) {
            return;
        }

        if (members.contains(memberId)) {
            return;
        }

        members.add(memberId);
    }

    public void addAllMembers(List<ObjectId> members) {
        members.forEach(this::addMember);
    }

    public void removeMembers(List<ObjectId> members) {
        this.members.removeAll(members);
    }
}
