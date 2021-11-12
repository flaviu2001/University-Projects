export class User {
  id: number;
  firstName: string;
  lastName: string;
  userName: string;
  email: string;
  bio: string;
  password: string;
  emailVerified: string;

  constructor(firstName: string, lastName: string, userName: string, email: string, bio: string, password: string, emailVerified: string) {
    this.id = -1;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userName = userName;
    this.email = email;
    this.bio = bio;
    this.password = password;
    this.emailVerified = emailVerified;
  }
}
