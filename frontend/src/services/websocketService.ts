import SockJS from 'sockjs-client';
import { Client, IMessage } from '@stomp/stompjs';

const WS_URL = process.env.REACT_APP_WS_URL || 'ws://localhost:8086/ws';

class WebSocketService {
  private stompClient: Client | null = null;
  private subscribers: Map<string, ((data: any) => void)[]> = new Map();

  connect() {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(WS_URL) as any,
      onConnect: () => {
        console.log('WebSocket connected');
      },
      onStompError: (frame) => {
        console.error('WebSocket connection error:', frame);
      },
    });
    this.stompClient.activate();
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
      this.stompClient = null;
    }
  }

  subscribe(topic: string, callback: (data: any) => void) {
    if (!this.stompClient) {
      this.connect();
      setTimeout(() => this.subscribe(topic, callback), 1000);
      return;
    }

    const trySubscribe = () => {
      if (this.stompClient?.connected) {
        if (!this.subscribers.has(topic)) {
          this.subscribers.set(topic, []);
          this.stompClient.subscribe(topic, (message: IMessage) => {
            const data = JSON.parse(message.body);
            const callbacks = this.subscribers.get(topic);
            callbacks?.forEach((cb) => cb(data));
          });
        }
        this.subscribers.get(topic)?.push(callback);
      } else {
        setTimeout(trySubscribe, 200);
      }
    };

    trySubscribe();
  }

  unsubscribe(topic: string, callback: (data: any) => void) {
    const callbacks = this.subscribers.get(topic);
    if (callbacks) {
      const index = callbacks.indexOf(callback);
      if (index > -1) {
        callbacks.splice(index, 1);
      }
    }
  }
}

export const websocketService = new WebSocketService();
