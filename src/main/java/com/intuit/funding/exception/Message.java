package com.intuit.funding.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Message {
    private String content;
    private String type;
    private String errorCode;

    public Message(String content, String type){
        this.content = content;
        this.type=type;
    }
}
