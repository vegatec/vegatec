export interface StreamState {
  playing: boolean;
  duration: number | undefined;
  currentTime: number | undefined;
  progress: number | undefined;
  canPlay: boolean;
  ended: boolean;
  error: boolean;
}
