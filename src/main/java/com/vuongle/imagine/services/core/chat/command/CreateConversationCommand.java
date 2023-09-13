package com.vuongle.imagine.services.core.chat.command;

import com.vuongle.imagine.constants.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateConversationCommand implements Serializable {

    private ChatType type;

    private List<ObjectId> addMemberIds;

    private String name;
}
