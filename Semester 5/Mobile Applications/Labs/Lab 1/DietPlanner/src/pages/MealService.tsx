import axios from 'axios';
import {getLogger} from '../core';
import {MealProps} from "./MealCommon";

const log = getLogger('itemApi');

const baseUrl = 'localhost:3000';
// noinspection HttpUrlsUsage
const mealUrl = `http://${baseUrl}/meal`;

interface ResponseProps<T> {
    data: T;
}

function withLogs<T>(promise: Promise<ResponseProps<T>>, functionName: string): Promise<T> {
    log(`${functionName} - started`);
    return promise
        .then(res => {
            log(`${functionName} - succeeded`);
            return Promise.resolve(res.data);
        })
        .catch(err => {
            log(`${functionName} - failed`);
            return Promise.reject(err);
        });
}

const config = {
    headers: {
        'Content-Type': 'application/json'
    }
};

export const getMeals: () => Promise<MealProps[]> = () => {
    return withLogs(axios.get(mealUrl, config), 'getMeals');
}

export const createMeal: (meal: MealProps) => Promise<MealProps[]> = meal => {
    return withLogs(axios.post(mealUrl, meal, config), 'createMeal');
}

export const updateMeal: (item: MealProps) => Promise<MealProps[]> = meal => {
    return withLogs(axios.put(`${mealUrl}/${meal.id}`, meal, config), 'updateMeal');
}

interface MessageData {
    event: string;
    payload: {
        meal: MealProps;
    };
}

export const newWebSocket = (onMessage: (data: MessageData) => void) => {
    const webSocket = new WebSocket(`ws://${baseUrl}`)
    webSocket.onopen = () => {
        log('web socket onopen');
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
