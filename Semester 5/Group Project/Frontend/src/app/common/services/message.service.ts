import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

import { Message } from "../models/message.model";

@Injectable()
export class MessageService {
  private baseUrl = "http://localhost:8080/api/message/";

  constructor(private httpClient: HttpClient) {
  }

  getConversation(senderUsername: string, receiverUsername: string): Observable<Message[]> {
    return this.httpClient.get<Message[]>(this.baseUrl + `get/${senderUsername}/${receiverUsername}`);
  }

  sendMessage(message: Message): Observable<any> {
    return this.httpClient.post(this.baseUrl + `send`, message);
  }
}
