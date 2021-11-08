import axios from 'axios';
import {authConfig, baseUrl, getLogger, withLogs} from './index';
import {Plugins} from "@capacitor/core";
import {MealProps} from "../components/meal/Meal";

// noinspection HttpUrlsUsage
const mealUrl = `http://${baseUrl}/api/meal`;

export const getMeals: (token: string, searchText: string) => Promise<MealProps[]> = (token, searchText) => {
    return withLogs(axios.get(`${mealUrl}/filter/${searchText}`, authConfig(token)), 'getMeals');
}

// @ts-ignore
export const createMeal: (token: string, meal: MealProps, networkStatus: any, present: any) => Promise<MealProps[]> = (token, meal, networkStatus, present) => {
    function offlineActionGenerator() {
        return new Promise<MealProps[]>(async (resolve) => {
            const {Storage} = Plugins;
            present("Couldn't send data to the server, caching it locally", 3000)
            await Storage.set({
                key: `sav-${meal.name}`,
                value: JSON.stringify({
                    token,
                    meal
                })
            })
            // @ts-ignore
            resolve(meal)
        })
    }
    if (networkStatus.connected) {
        return withLogs(axios.post(mealUrl, meal, authConfig(token)), 'createMeal')
            .catch(() => {
            return offlineActionGenerator()
        });
    }
    return offlineActionGenerator()
}

export const uploadPhoto: (token: string, photo: string) => void = (token, photo) => {
    axios.post(`${mealUrl}/photo`, photo, authConfig(token)).then(_ => {})
}

// @ts-ignore
export const updateMeal: (token: string, meal: MealProps, networkStatus: any, present: any) => Promise<MealProps[]> = (token, meal, networkStatus, present) => {
    function offlineActionGenerator() {
        return new Promise<MealProps[]>(async (resolve) => {
            const {Storage} = Plugins;
            present("Couldn't send data to the server, caching it locally", 3000)
            await Storage.set({
                key: `upd-${meal.name}`,
                value: JSON.stringify({
                    token,
                    meal
                })
            })
            // @ts-ignore
            resolve(meal)
        })
    }
    if (networkStatus.connected)
        return withLogs(axios.put(`${mealUrl}/${meal._id}`, meal, authConfig(token)), 'updateMeal').catch(() => {
            return offlineActionGenerator()
        });
    return offlineActionGenerator()
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
