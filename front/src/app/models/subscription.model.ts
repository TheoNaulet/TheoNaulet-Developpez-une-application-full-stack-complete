import { Theme } from "./theme.model";

export interface Subscription {
    id: number;
    createdAt: string;
    theme: Theme;
}