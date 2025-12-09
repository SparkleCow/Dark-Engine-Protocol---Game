import { Injectable } from '@angular/core';
import { Client, IFrame, Stomp } from '@stomp/stompjs';
import { Subject } from 'rxjs';
import { Position } from '../../models/position';
import SockJS from 'sockjs-client';
import { AuthService } from './auth.service';

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

  constructor(private authService: AuthService) {}

  // src/app/core/services/websocket.service.ts (Método connect modificado)

  public connect(playerId: number): void {
    
    // 1. Obtener el Token JWT
    const jwtToken = this.authService.getToken(); 

    if (!jwtToken) {
        console.error("JWT Token not found. Cannot connect to WebSocket.");
        return; 
    }
    
    // 2. Crear el socket y el cliente STOMP
    const socket = new SockJS(this.socketUrl);
    this.stompClient = new Client({ // Usamos 'new Client' en lugar de Stomp.over()
        webSocketFactory: () => socket,
        debug: (str) => { 
          console.log(str); 
        },
        
        connectHeaders: {
             'Authorization': `Bearer ${jwtToken}`, 
        },

        onConnect: (frame: IFrame) => {
            console.log('STOMP Connected:', frame);
            
            this.stompClient.subscribe(this.SYNC_TOPIC, message => {
                const positions: Position[] = JSON.parse(message.body);
                this.syncSubject.next(positions);
            });
        },
        
        onWebSocketError: (error) => console.error('WS Error:', error),
        onStompError: (frame) => console.error('STOMP Error:', frame),

        reconnectDelay: 5000, 
    });

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
