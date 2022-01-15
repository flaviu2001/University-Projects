package com.ubb.en.ArcaneProgramming.controller;


import com.ubb.en.ArcaneProgramming.converter.MessageConverter;
import com.ubb.en.ArcaneProgramming.dto.MessageDto;
import com.ubb.en.ArcaneProgramming.model.ArcaneUser;
import com.ubb.en.ArcaneProgramming.model.Message;
import com.ubb.en.ArcaneProgramming.model.MessageStatus;
import com.ubb.en.ArcaneProgramming.service.MessageService;
import com.ubb.en.ArcaneProgramming.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @GetMapping("/get/{senderUserName}/{receiverUserName}")
    private ResponseEntity<List<MessageDto>> getConversation(@PathVariable String senderUserName, @PathVariable String receiverUserName){
        ArcaneUser sender = userService.findUserByUserName(senderUserName);
        ArcaneUser receiver = userService.findUserByUserName(receiverUserName);
        messageService.updateReadReceipts(sender, receiver);
        List<Message> messages = messageService.getConversation(sender, receiver);
        return new ResponseEntity<>(messages.stream().map(MessageConverter::convertToDto).collect(Collectors.toList()), HttpStatus.OK);

    }

    @PostMapping("/send")
    public ResponseEntity<MessageDto> sendMessage(@RequestBody MessageDto messageDto){
        Message message = new Message();
        message.setContent(messageDto.getContent());
        message.setDate(new Date());
        message.setMessageStatus(MessageStatus.SENT);
        message.setSender(userService.findUserByUserName(messageDto.getSender().getUserName()));
        message.setReceiver(userService.findUserByUserName(messageDto.getReceiver().getUserName()));
        message = messageService.addMessage(message);
        return new ResponseEntity<>(MessageConverter.convertToDto(message), HttpStatus.OK);
    }
}
