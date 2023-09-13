package com.vuongle.imagine.services.core.chat;

import com.vuongle.imagine.models.Conversation;
import com.vuongle.imagine.services.core.BaseService;
import com.vuongle.imagine.services.core.chat.command.CreateConversationCommand;
import com.vuongle.imagine.services.core.chat.command.UpdateConversationCommand;

public interface ConversationService extends BaseService<CreateConversationCommand, UpdateConversationCommand, Conversation> {

}
