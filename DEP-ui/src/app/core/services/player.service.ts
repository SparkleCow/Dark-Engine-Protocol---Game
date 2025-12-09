import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlayerResponseDto } from '../../models/player-response-dto';


@Injectable({
  providedIn: 'root'
})
export class PlayerService {

  private readonly apiUrl = 'http://localhost:8080/api/player'; 
  constructor(private http: HttpClient) { }


  $getPlayerInformation(): Observable<PlayerResponseDto> {
    return this.http.get<PlayerResponseDto>(this.apiUrl);
  }
}
