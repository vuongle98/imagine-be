package com.vuongle.imagine.models;

import com.vuongle.imagine.constants.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("conversation")
public class Conversation {

    @Id
    private ObjectId id;

    private String name;

    private List<ObjectId> members;

    private ChatType type = ChatType.PRIVATE;

    private boolean deleted;
    @LastModifiedDate
    private Instant timeStamp;

    public void addMember(ObjectId memberId) {

        if (Objects.isNull(members)) {
            members = new ArrayList<>();
        }

        if (type.equals(ChatType.PRIVATE) && members.size() == 2) {
            return;
        }

        if (members.contains(memberId)) {
            return;
        }

        members.add(memberId);
    }

    public void addAllMembers(List<ObjectId> members) {
        if (Objects.isNull(this.members)) {
            this.members = new ArrayList<>();
        }

        members.forEach(this::addMember);
    }

    public void removeMembers(List<ObjectId> members) {
        this.members.removeAll(members);
    }
}
