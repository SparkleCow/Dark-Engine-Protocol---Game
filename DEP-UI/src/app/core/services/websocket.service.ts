import { Injectable } from '@angular/core';
import { Client, IFrame } from '@stomp/stompjs';
import { Subject } from 'rxjs';
import SockJS from 'sockjs-client';
import { AuthService } from './auth.service';
import { WorldSnapshot } from '../../models/world-snapshot';
import { Position } from '../../models/position';

@Injectable({
  providedIn: 'root',
})
export class WebsocketService {
  public stompClient!: Client;
  private socketUrl: string = 'http://localhost:8080/ws';

  private worldSubject = new Subject<WorldSnapshot>();
  public world$ = this.worldSubject.asObservable();

  private WORLD_TOPIC = '/topic/sync/world';
  private MOVEMENT_DESTINATION = '/app/move';
  private ATTACK_DESTINATION = '/app/attack';

  constructor(private authService: AuthService) {}

  public connect(playerId: number): void {
    const jwtToken = this.authService.getToken();
    if (!jwtToken) return;

    const socket = new SockJS(this.socketUrl);

    this.stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: `Bearer ${jwtToken}`,
      },
      reconnectDelay: 5000,

      onConnect: (frame: IFrame) => {
        console.log('STOMP Connected');

        this.stompClient.subscribe(this.WORLD_TOPIC, (message) => {
          const snapshot: WorldSnapshot = JSON.parse(message.body);
          this.worldSubject.next(snapshot);
        });
      },

      onWebSocketError: (err) => console.error(err),
      onStompError: (frame) => console.error(frame),
    });

    this.stompClient.activate();
  }

  public sendMovement(position: Position): void {
    if (this.stompClient?.connected) {
      this.stompClient.publish({
        destination: this.MOVEMENT_DESTINATION,
        body: JSON.stringify(position),
      });
    }
  }

  public sendAttack(monsterId: string): void {
    if (!this.stompClient?.connected) return;

    this.stompClient.publish({
      destination: this.ATTACK_DESTINATION,
      body: JSON.stringify({ monsterId }),
    });
  }

  public disconnect(): void {
    this.stompClient?.deactivate();
  }
}
