package com.ubb.en.ArcaneProgramming.dto;

import com.ubb.en.ArcaneProgramming.model.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long ID;
    private String content;
    private Date date;
    private MessageStatus messageStatus;
    private ArcaneUserDto sender;
    private ArcaneUserDto receiver;
}
