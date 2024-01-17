export interface ITrackType {
  id: number;
  name?: string | null;
  creditsNeeded?: number | null;
  vipCreditsNeeded?: number | null;
}

export type NewTrackType = Omit<ITrackType, 'id'> & { id: null };
