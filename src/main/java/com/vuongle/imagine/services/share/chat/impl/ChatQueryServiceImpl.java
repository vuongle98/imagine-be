package com.vuongle.imagine.services.share.chat.impl;

import com.vuongle.imagine.constants.ChatType;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.models.ChatMessage;
import com.vuongle.imagine.repositories.MessageRepository;
import com.vuongle.imagine.services.share.chat.ChatQueryService;
import com.vuongle.imagine.services.share.chat.query.ChatQuery;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ChatQueryServiceImpl implements ChatQueryService {

    private final MessageRepository messageRepository;

    private final MongoTemplate mongoTemplate;

    @Value("${imagine.app.recent-message-limit}")
    private int recentMessageLimit;

    public ChatQueryServiceImpl(MessageRepository messageRepository, MongoTemplate mongoTemplate) {
        this.messageRepository = messageRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ChatMessage> findAllByConversationId(ObjectId conversationId) {
        return findAllByConversationId(conversationId, Pageable.ofSize(recentMessageLimit));
    }

    @Override
    public List<ChatMessage> findAllByConversationId(ObjectId conversationId, Pageable pageable) {

        List<ChatMessage> chatMessages = messageRepository.findAllByConversationIdOrderByIdDesc(conversationId, pageable);
        chatMessages.sort(Comparator.comparing(ChatMessage::getTimeStamp));
        return chatMessages;
    }

    @Override
    public List<ChatMessage> findAllPublicMessages() {
        List<ChatMessage> chatMessages = messageRepository.findAllPublicMessages(Pageable.ofSize(recentMessageLimit));

        chatMessages.sort(Comparator.comparing(ChatMessage::getTimeStamp));
        return chatMessages;
    }

//    @Override
    public ChatMessage findById(ObjectId id) {
        return null;
    }

    @Override
    public <T> Optional<T> findById(ObjectId id, Class<T> classType) {
        return Optional.ofNullable(mongoTemplate.findById(id, classType));
    }

    @Override
    public <T> T getById(ObjectId id, Class<T> classType) {
        return findById(id, classType).orElseThrow(() -> new DataNotFoundException("Not found message has id " + id));
    }

    @Override
    public <T> Page<T> findPage(ChatQuery chatQuery, Pageable pageable, Class<T> returnType) {
        Query query = createQuery(chatQuery);
        List<T> data = mongoTemplate.find(query, returnType);
        return PageableExecutionUtils.getPage(
                data,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), returnType)
        );
    }

    @Override
    public <T> Page<T> findPage(ChatQuery chatQuery, Pageable pageable, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public <T> List<T> findList(ChatQuery chatQuery, Class<T> returnType) {
        Query query = createQuery(chatQuery);
        return mongoTemplate.find(query, returnType);
    }

    @Override
    public Query createQuery(ChatQuery chatQuery, Pageable pageable) {
        Query query = new Query();

        if (Objects.nonNull(pageable)) {
            query.with(pageable);
        }

        query.addCriteria(createCriteria(chatQuery));
        return query;
    }

    @Override
    public Query createQuery(ChatQuery chatQuery) {
        return createQuery(chatQuery, null);
    }

    @Override
    public long countByQuery(ChatQuery chatQuery) {
        Query query = createQuery(chatQuery);
        return mongoTemplate.count(query, ChatMessage.class);
    }

    @Override
    public long countByQuery(ChatQuery chatQuery, AggregationOperation... aggregationOperationInputs) {
        return 0;
    }

    @Override
    public Criteria createCriteria(ChatQuery query) {
        Criteria criteria = new Criteria();

        List<Criteria> listAndCriteria = new ArrayList<>();

        if (Objects.nonNull(query.getId())) {
            listAndCriteria.add(Criteria.where("_id").is(query.getId()));
        }

        if (Objects.nonNull(query.getConversationId())) {
            listAndCriteria.add(Criteria.where("conversationId").is(query.getConversationId()));
        }

        if (Objects.nonNull(query.getLikeContent())) {
            listAndCriteria.add(Criteria.where("content").regex(query.getLikeContent(), "i"));
        }

        if (Objects.nonNull(query.getSenderId())) {
            listAndCriteria.add(Criteria.where("sender.id").is(query.getSenderId()));
        }

        if (Objects.nonNull(query.getFrom())) {
            listAndCriteria.add(Criteria.where("timeStamp").gte(query.getFrom()));
        }

        if (Objects.nonNull(query.getTo())) {
            listAndCriteria.add(Criteria.where("timeStamp").lte(query.getTo()));
        }

        if (Objects.nonNull(query.getType())) {
            listAndCriteria.add(Criteria.where("type").is(query.getType()));
        }

        if (!listAndCriteria.isEmpty()) {
            criteria.andOperator(listAndCriteria.toArray(new Criteria[0]));
        }

        return criteria;
    }
}
