export class User {
  id: number;
  name: string;
  username: string;
  password: string;
  age: number;
  role: string;
  email: string;
  webpage: string;


  constructor(id: number, name: string, username: string, password: string, age: number, role: string, email: string, webpage: string) {
    this.id = id;
    this.name = name;
    this.username = username;
    this.password = password;
    this.age = age;
    this.role = role;
    this.email = email;
    this.webpage = webpage;
  }
}
