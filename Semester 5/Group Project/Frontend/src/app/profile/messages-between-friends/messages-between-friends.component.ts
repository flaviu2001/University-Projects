import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";

import { User } from "../../common/models/user.model";
import { Message } from "../../common/models/message.model";
import { ProfileService } from "../../common/services/profile.service";
import { MessageService } from "../../common/services/message.service";
import {Utils} from "../../common/utils";

@Component({
  selector: 'app-messages-between-friends',
  templateUrl: './messages-between-friends.component.html',
  styleUrls: ['./messages-between-friends.component.css']
})
export class MessagesBetweenFriendsComponent implements OnInit {
  senderUsername: string = ''; // me
  receiverUsername: string = '';

  sender: User = {} as User;
  receiver: User = {} as User;
  messages: Message[] = [];

  constructor(private route: ActivatedRoute,
              private profileService: ProfileService, private messageService: MessageService) { }

  ngOnInit(): void {
    this.receiverUsername = this.route.snapshot.params.name;
    this.senderUsername = String(localStorage.getItem('username'));

    this.profileService.getUserByUsername(this.receiverUsername).subscribe((user) => {
      this.receiver = user;
    });
    this.profileService.getUserByUsername(this.senderUsername).subscribe((user) => {
      this.sender = user;
    })

    this.messageService.getConversation(this.senderUsername, this.receiverUsername)
      .subscribe((messages) => { this.messages = messages; });
  }

  sendMessage(content: string): void {
    let message = new Message(content, new Date(), "SENT", this.sender, this.receiver);
    this.messageService.sendMessage(message).subscribe(() => {
      window.location.reload();
    });
  }

  getPrettyMessageDate(message: Message): string {
    return new Utils().prettyDate(new Date(message.date))
  }
}
