import { FormControl, Validators } from "@angular/forms";

export class Utils {
  constructor() {
  }

  setDate(): any {
    let date = new Date(Date.now());

    let year = date.getFullYear();
    let longMonth = date.toLocaleString('en-us', { month: 'long' });
    let day = (date.getDate() < 10 ? '0' : '') + date.getDate();
    let hour = (date.getHours() < 10 ? '0' : '') + date.getHours();
    let minutes = (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();

    return (day + "-" + longMonth + "-" + year + " " + hour + ":" + minutes);
  }

  /*__________________________________________________________________________________________________________________*/

  hidePassword: boolean = true;
  hideReenterPassword: boolean = true;
  hideNewPassword: boolean = true;

  firstName = new FormControl('',
    [Validators.required, Validators.pattern('^[a-zA-Z]+$')]);
  lastName = new FormControl('',
    [Validators.required, Validators.pattern('^[a-zA-Z]+$')]);
  username = new FormControl('',
    [Validators.required, Validators.pattern('^[0-9a-zA-Z ]+$')]);
  email = new FormControl('',
    [Validators.required, Validators.email]);
  password = new FormControl('',
    [Validators.required, Validators.minLength(8), Validators.pattern('^[0-9a-zA-Z]+$')]);
  reenterPassword = new FormControl('',
    [Validators.required, Validators.minLength(8), Validators.pattern('^[0-9a-zA-Z]+$')]);
  newPassword = new FormControl('',
    [Validators.required, Validators.minLength(8), Validators.pattern('^[0-9a-zA-Z]+$')]);

  getErrorMessageForFirstName() {
    if(this.firstName.hasError('required')) {
      return 'You must enter a value';
    }

    return this.firstName.hasError('pattern') ?
      'The first name should contain only capital and lowercase letters' : '';
  }

  getErrorMessageForLastName() {
    if(this.lastName.hasError('required')) {
      return 'You must enter a value';
    }

    return this.lastName.hasError('pattern') ?
      'The last name should contain only capital and lowercase letters' : '';
  }

  getErrorMessageForUsername() {
    if(this.username.hasError('required')) {
      return 'You must enter a value';
    }

    return this.username.hasError('pattern') ?
      'The username should contain only digits, spaces and capital and lowercase letters' : '';
  }

  getErrorMessageForPassword() {
    if(this.password.hasError('minlength')) {
      return 'The password should contain at least 8 characters';
    }
    if(this.password.hasError('required')) {
      return 'You must enter a value';
    }

    return this.password.hasError('pattern') ?
      'The password should contain only digits and capital and lowercase letters' : '';
  }

  getErrorMessageForRPassword() {
    if(this.reenterPassword.hasError('minlength')) {
      return 'The password should contain at least 8 characters';
    }
    if(this.reenterPassword.hasError('required')) {
      return 'You must enter a value';
    }

    return this.reenterPassword.hasError('pattern') ?
      'The password should contain only digits and capital and lowercase letters' : '';
  }

  getErrorMessageForNewPassword() {
    if(this.newPassword.hasError('minlength')) {
      return 'The password should contain at least 8 characters';
    }
    if(this.newPassword.hasError('required')) {
      return 'You must enter a value';
    }

    return this.newPassword.hasError('pattern') ?
      'The password should contain only digits and capital and lowercase letters' : '';
  }

  getErrorMessageForEmail() {
    if (this.email.hasError('required')) {
      return 'You must enter a value';
    }

    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  /*__________________________________________________________________________________________________________________*/
}
