package com.ubb.en.ArcaneProgramming.converter;

import com.ubb.en.ArcaneProgramming.dto.MessageDto;
import com.ubb.en.ArcaneProgramming.model.Message;

public class MessageConverter {
    public static MessageDto convertToDto(Message message){
        MessageDto messageDto = new MessageDto();
        messageDto.setID(message.getID());
        messageDto.setContent(message.getContent());
        messageDto.setDate(message.getDate());
        messageDto.setMessageStatus(message.getMessageStatus());
        messageDto.setSender(ArcaneUserConverter.convertToDto(message.getSender()));
        messageDto.setReceiver(ArcaneUserConverter.convertToDto(message.getReceiver()));
        return messageDto;
    }
}
