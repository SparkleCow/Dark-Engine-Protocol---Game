import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthRequestDto } from '../../models/auth-request-dto';
import { AuthResponseDto } from '../../models/auth-response-dto';
import { PlayerRequestDto } from '../../models/player-request-dto';

/*Auth service for login, register, save and retrieve jwt token and playerId from localStorage*/
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly api = 'http://localhost:8082/auth';
  private JWT_KEY = 'jwt';

  constructor(private http: HttpClient) {}

  $login(payload: AuthRequestDto): Observable<AuthResponseDto> {
    return this.http.post<AuthResponseDto>(`${this.api}/login`, payload);
  }

  $register(payload: PlayerRequestDto): Observable<void> {
    return this.http.post<void>(`${this.api}/register`, payload);
  }

  saveToken(token: string): void {
    localStorage.setItem(this.JWT_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.JWT_KEY);
  }

  // Cleanup Method

  clearAuthData(): void {
    localStorage.removeItem(this.JWT_KEY);
  }
}
