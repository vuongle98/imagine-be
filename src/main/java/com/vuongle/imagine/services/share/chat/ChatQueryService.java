package com.vuongle.imagine.services.share.chat;

import com.vuongle.imagine.models.ChatMessage;
import com.vuongle.imagine.services.share.BaseService;
import com.vuongle.imagine.services.share.chat.query.ChatQuery;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatQueryService extends BaseService<ChatQuery> {

    List<ChatMessage> findAllByConversationId(ObjectId conversationId);

    List<ChatMessage> findAllByConversationId(ObjectId conversationId, Pageable pageable);

    List<ChatMessage> findAllPublicMessages();




}
