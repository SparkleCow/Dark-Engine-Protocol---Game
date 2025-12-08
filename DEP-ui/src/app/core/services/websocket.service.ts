import { Injectable } from '@angular/core';
import { Client, Stomp } from '@stomp/stompjs';
import { Subject } from 'rxjs';
import { Position } from '../../models/position';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  public stompClient!: Client;
  private socketUrl: string = 'http://localhost:8080/ws';
  
  // Subject for emit data
  private syncSubject = new Subject<Position[]>();
  public sync$ = this.syncSubject.asObservable();

  // Websockets constants
  private SYNC_TOPIC = '/topic/sync/all-positions';
  private MOVEMENT_DESTINATION = '/app/move';

  public connect(playerId: number): void {
    const socket = new SockJS(this.socketUrl);
    this.stompClient = Stomp.over(socket);
    this.stompClient.debug = () => {};

    this.stompClient.onConnect = (frame) => {
      console.log('STOMP Connected:', frame);
      
      // Once we are already connect, we are going to suscribe at topic. 
      // Global suscription
      this.stompClient.subscribe(this.SYNC_TOPIC, message => {
        const positions: Position[] = JSON.parse(message.body);
        this.syncSubject.next(positions);
      });
    };
    
    this.stompClient.onWebSocketError = (error) => console.error('WS Error:', error);
    this.stompClient.onStompError = (frame) => console.error('STOMP Error:', frame);

    this.stompClient.activate();
  }
  
  public sendMovement(position: Position): void {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: this.MOVEMENT_DESTINATION,
        body: JSON.stringify(position)
      });
    }
  }

  public disconnect(): void {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }
}
