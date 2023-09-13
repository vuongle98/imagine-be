package com.vuongle.imagine.services.core.chat.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateConversationCommand extends CreateConversationCommand {

    private ObjectId id;

    private List<ObjectId> removedMembers;
}
