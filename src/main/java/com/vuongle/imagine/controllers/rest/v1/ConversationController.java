package com.vuongle.imagine.controllers.rest.v1;

import com.vuongle.imagine.models.Conversation;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.repositories.ConversationRepository;
import com.vuongle.imagine.services.core.chat.command.CreateConversationCommand;
import com.vuongle.imagine.services.core.chat.command.UpdateConversationCommand;
import com.vuongle.imagine.services.core.chat.impl.ConversationServiceImpl;
import com.vuongle.imagine.utils.Context;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationRepository conversationRepository;

    private final ConversationServiceImpl conversationService;

    public ConversationController(ConversationRepository conversationRepository, ConversationServiceImpl conversationService) {
        this.conversationRepository = conversationRepository;
        this.conversationService = conversationService;
    }

    @PostMapping()
    public ResponseEntity<Conversation> create(
            @RequestBody CreateConversationCommand command
    ) {

        Conversation conversation = conversationService.create(command);
        return ResponseEntity.ok(conversation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conversation> update(
            @PathVariable(value = "id") ObjectId id,
            @RequestBody UpdateConversationCommand command
    ) {
        command.setId(id);
        Conversation conversation = conversationService.update(command);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping
    public ResponseEntity<Page<Conversation>> findAll(Pageable pageable) {
        User user = Context.getUser();
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(conversationRepository.findAllByMember(user.getId(), pageable));
    }
}
