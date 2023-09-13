package com.vuongle.imagine.services.core.chat.impl;

import com.vuongle.imagine.constants.ChatType;
import com.vuongle.imagine.constants.UserRole;
import com.vuongle.imagine.models.Conversation;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.repositories.ConversationRepository;
import com.vuongle.imagine.services.core.chat.ConversationService;
import com.vuongle.imagine.services.core.chat.command.CreateConversationCommand;
import com.vuongle.imagine.services.core.chat.command.UpdateConversationCommand;
import com.vuongle.imagine.utils.Context;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationServiceImpl(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }


    @Override
    public Conversation create(CreateConversationCommand command) {
        Conversation conversation = new Conversation();

        User user = Context.getUser();

        if (Objects.isNull(user)) {
            throw new RuntimeException("User not found");
        }

        command.getAddMemberIds().add(user.getId());

        // previous conversation by check type private and 2 members user and friend id, if exist not add
        if (command.getType().equals(ChatType.PRIVATE) && command.getAddMemberIds().size() == 2) {

            Conversation existedConversation = conversationRepository.findByTypeAndMembers(ChatType.PRIVATE, command.getAddMemberIds());

            if (Objects.nonNull(existedConversation)) {
                return existedConversation;
            }
        }

        switch (command.getType()) {
            case GROUP -> {
                conversation.setType(ChatType.GROUP);
                conversation.addAllMembers(command.getAddMemberIds());
            }
            case PRIVATE -> {
                conversation.setType(ChatType.PRIVATE);

                // check not over 2 members
                if (command.getAddMemberIds().size() == 2) {
                    conversation.setMembers(command.getAddMemberIds());
                } else {
                    throw new RuntimeException("Not over 2 members on private chat");
                }
            }
            case PUBLIC -> {

                if (!user.isAdmin() || !user.isModerator()) {
                    throw new RuntimeException("Not admin or moderator");
                }

                conversation.setType(ChatType.PUBLIC);
                conversation.addAllMembers(command.getAddMemberIds());
            }
        }

        conversation.setName(command.getName());
        conversation = conversationRepository.save(conversation);
        return conversation;
    }

    @Override
    public Conversation update(UpdateConversationCommand command) {

        Conversation conversation = conversationRepository.findById(command.getId()).orElseThrow(() -> new RuntimeException("Conversation not found"));

        if (Objects.nonNull(command.getAddMemberIds())) {
            conversation.addAllMembers(command.getAddMemberIds());
        }

        if (Objects.nonNull(command.getRemovedMembers())) {
            conversation.removeMembers(command.getRemovedMembers());
        }

        if (Objects.nonNull(command.getName())) {
            conversation.setName(command.getName());
        }


        conversation = conversationRepository.save(conversation);
        return conversation;
    }

    @Override
    public void delete(ObjectId id) {

    }
}
