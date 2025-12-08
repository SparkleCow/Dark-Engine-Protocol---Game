import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthRequestDto } from '../../models/auth-request-dto';
import { AuthResponseDto } from '../../models/auth-response-dto';
import { PlayerRequestDto } from '../../models/player-request-dto';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly api = 'http://localhost:8080/api/auth';
  private PLAYER_ID_KEY = 'player-id';
  private JWT_KEY = 'jwt';

  constructor(private http: HttpClient) {}

  $login(payload: AuthRequestDto): Observable<AuthResponseDto> {
    return this.http.post<AuthResponseDto>(`${this.api}/login`, payload);
  }

  $register(payload: PlayerRequestDto): Observable<void> {
    return this.http.post<void>(`${this.api}/register`, payload);
  }

  savePlayerId(playerId: string): void {
    localStorage.setItem(this.PLAYER_ID_KEY, playerId);
  }

  getPlayerId(): string | null {
    return localStorage.getItem(this.PLAYER_ID_KEY);
  }

  saveToken(token: string): void {
    localStorage.setItem(this.JWT_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.JWT_KEY);
  }

  // Cleanup Method
  
  clearAuthData(): void {
    localStorage.removeItem(this.PLAYER_ID_KEY);
    localStorage.removeItem(this.JWT_KEY);
  }
}