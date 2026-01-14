import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms'
import { AuthService } from './core/services/auth.service';
import { AuthRequestDto } from './models/auth-request-dto';
import { AuthResponseDto } from './models/auth-response-dto';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  imports: [CommonModule, ReactiveFormsModule, RouterOutlet], 
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  
  mode: 'login'|'register' = 'login';
  fg: FormGroup; 
  
  showLanding: boolean = true; 

  constructor(
    private authService: AuthService,
    private formBuilder: FormBuilder,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object 
  ){
    this.fg = formBuilder.group({});
    this.updateFormControls();
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
        this.router.events.pipe(
            filter((event): event is NavigationEnd => event instanceof NavigationEnd)
        ).subscribe((event: NavigationEnd) => {
            this.showLanding = event.urlAfterRedirects === '/';
        });

        this.showLanding = this.router.url === '/';
    }
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
            this.authService.savePlayerId(authResponseDto.playerId.toString());
            this.authService.saveToken(authResponseDto.jwt);
            this.redirectAtGame(); // Éxito: Navegar al juego
          },
          error: (err) => {
            console.error("Login failed", err);
          }
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

  redirectAtGame(){
    this.router.navigate(["/dep"]);
  }
}