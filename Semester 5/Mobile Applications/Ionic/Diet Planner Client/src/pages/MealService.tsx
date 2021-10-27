import axios from 'axios';
import {authConfig, baseUrl, getLogger, withLogs} from '../core';
import {MealProps} from "./MealCommon";

// noinspection HttpUrlsUsage
const mealUrl = `http://${baseUrl}/api/meal`;

export const getMeals: (token: string) => Promise<MealProps[]> = token => {
    return withLogs(axios.get(mealUrl, authConfig(token)), 'getMeals');
}

export const createMeal: (token: string, meal: MealProps) => Promise<MealProps[]> = (token, meal) => {
    return withLogs(axios.post(mealUrl, meal, authConfig(token)), 'createMeal');
}

export const updateMeal: (token: string, meal: MealProps) => Promise<MealProps[]> = (token, meal) => {
    return withLogs(axios.put(`${mealUrl}/${meal._id}`, meal, authConfig(token)), 'updateMeal');
}

interface MessageData {
    type: string;
    payload: {
        meal: MealProps;
    };
}

const log = getLogger('ws');

export const newWebSocket = (token: string, onMessage: (data: MessageData) => void) => {
    const webSocket = new WebSocket(`ws://${baseUrl}`)
    webSocket.onopen = () => {
        log('web socket onopen');
        webSocket.send(JSON.stringify({ type: 'authorization', payload: { token } }));
    };
    webSocket.onclose = () => {
        log('web socket onclose');
    };
    webSocket.onerror = error => {
        log('web socket onerror', error);
    };
    webSocket.onmessage = messageEvent => {
        log('web socket onmessage');
        onMessage(JSON.parse(messageEvent.data));
    };
    return () => {
        webSocket.close();
    }
}
