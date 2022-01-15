import { User } from "./user.model";

export class Message {
  id: number;
  content: string;
  date: Date;
  messageStatus: string;
  sender: User;
  receiver: User;

  constructor(_content: string, _date: Date, _messageStatus: string, _sender: User, _receiver: User) {
    this.id = -1;
    this.content = _content;
    this.date = _date;
    this.messageStatus = _messageStatus;
    this.sender = _sender;
    this.receiver = _receiver;
  }
}
