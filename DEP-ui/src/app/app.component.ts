import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormBuilder, FormGroup, FormGroupDirective, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms'
import { AuthService } from './core/services/auth.service';
import { AuthRequestDto } from './models/auth-request-dto';
import { AuthResponseDto } from './models/auth-response-dto';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  mode: 'login'|'register' = 'login';
  fg: FormGroup; 

  constructor(
    private authService: AuthService,
    private formBuilder: FormBuilder
  ){
    this.fg = formBuilder.group({});
    this.updateFormControls();
  }

updateFormControls() {

    Object.keys(this.fg.controls).forEach(key => this.fg.removeControl(key));

    if (this.mode === 'login') {
      this.fg.addControl('username', this.formBuilder.control('', [
        Validators.required, 
        Validators.maxLength(20)
      ]));
      
      this.fg.addControl('password', this.formBuilder.control('', [
        Validators.required, 
        Validators.minLength(6) 
      ]));
      
    } else if (this.mode === 'register') {
      this.fg.addControl('username', this.formBuilder.control('', [
        Validators.required
      ]));
      
      this.fg.addControl('email', this.formBuilder.control('', [
        Validators.required, 
        Validators.email
      ]));
      
      this.fg.addControl('password', this.formBuilder.control('', [
        Validators.required, 
        Validators.minLength(6)
      ])); 
    }
  }

  toggleMode(newMode: 'login'|'register') {
      if (this.mode !== newMode) {
        this.mode = newMode;
        this.updateFormControls();
        this.fg.reset();
      }
  }

  onSubmit() {
    if (this.fg.valid) {
      if (this.mode === 'login') {
        const authRequestDto: AuthRequestDto = {
          username: this.fg.value.username,
          password: this.fg.value.password
        }

        this.authService.$login(authRequestDto).subscribe({
          next: (authResponseDto: AuthResponseDto) => {
            this.authService.savePlayerId(authResponseDto.playerId.toString())
          },
          error: () => {}
        });

      } else {
        const playerRequestDto= this.fg.value; 

        this.authService.$register(playerRequestDto).subscribe({

          next: () => {
            this.toggleMode('login'); 
          },
          error: (err) => {
            console.error('Registration failed:', err);
          },
          complete: () => {
          }
        });
      }
    } else {
      console.log('Form is invalid!');
    }
  }
}