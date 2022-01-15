import { User } from "./user.model";

export class FriendInvitation {
  id: number;
  inviterDto: User;
  invitedDto: User;
  status: string;

  constructor(inviter: User, invited: User, status: string) {
    this.id = -1;
    this.inviterDto = inviter;
    this.invitedDto = invited;
    this.status = status;
  }
}
