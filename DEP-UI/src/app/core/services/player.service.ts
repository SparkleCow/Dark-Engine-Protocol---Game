import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlayerResponseDto } from '../../models/player-response-dto';

@Injectable({
  providedIn: 'root',
})
export class PlayerService {
  private readonly apiUrl = 'http://localhost:8081/player';
  constructor(private http: HttpClient) {}

  /*Only is needed send the jwt since our interceptor to be able to access at player information since the API recieve an authentication object.*/
  $getPlayerInformation(): Observable<PlayerResponseDto> {
    return this.http.get<PlayerResponseDto>(`${this.apiUrl}/me`);
  }
}
