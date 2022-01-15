package com.ubb.en.ArcaneProgramming.service;

import com.ubb.en.ArcaneProgramming.model.ArcaneUser;
import com.ubb.en.ArcaneProgramming.model.Message;
import com.ubb.en.ArcaneProgramming.model.MessageStatus;
import com.ubb.en.ArcaneProgramming.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getConversation(ArcaneUser sender, ArcaneUser receiver) {
        List<Message> messages = messageRepository.findBySenderAndReceiverOrderByDate(sender, receiver);
        messages.addAll(messageRepository.findBySenderAndReceiverOrderByDate(receiver, sender));
        messages.sort(Comparator.comparing(Message::getDate).reversed());
        return messages;
    }

    @Transactional
    public void updateReadReceipts(ArcaneUser sender, ArcaneUser receiver) {
        List<Message> messages = messageRepository.findBySenderAndReceiverOrderByDate(receiver, sender);
        for (Message message : messages)
            message.setMessageStatus(MessageStatus.READ);
    }
}
