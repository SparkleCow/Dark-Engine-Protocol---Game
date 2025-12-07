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

  constructor(private http: HttpClient) {}

  $login(payload: AuthRequestDto): Observable<AuthResponseDto> {
    return this.http.post<AuthResponseDto>(`${this.api}/login`, payload);
  }

  $register(payload: PlayerRequestDto): Observable<void> {
    return this.http.post<void>(`${this.api}/register`, payload);
  }

  savePlayerId(playerId: string){
    localStorage.setItem("player-id", playerId);
  }

  getPlayerId(){
    return localStorage.getItem("player-id");
  }

  saveToken(token: string){
    localStorage.setItem("jwt", token)
  }

  getToken(){
    return localStorage.getItem("jwt")
  }
}