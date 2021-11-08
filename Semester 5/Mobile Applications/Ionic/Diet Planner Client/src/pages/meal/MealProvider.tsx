import React, {useCallback, useContext, useEffect, useReducer} from "react";
import PropTypes from "prop-types";
import {createMeal, getMeals, newWebSocket, updateMeal} from "../../core/ApiService";
import {useNetwork} from "../network/useNetwork";
import {useIonToast} from "@ionic/react";
import {Plugins} from "@capacitor/core";
import {MealProps} from "./Meal";
import {AuthContext} from "../auth/AuthProvider";

interface ActionProps {
    type: string,
    payload?: any,
}

export interface MealsState {
    meals?: MealProps[],
    fetching: boolean,
    fetchingError?: Error | null,
    saving: boolean,
    savingError?: Error | null,
    saveMeal?: SaveMealFunction,
    searchText: string,
    setSearchText?: SetSearchTextFunction
}

export type SaveMealFunction = (meal: MealProps) => Promise<any>;
export type SetSearchTextFunction = (text: string) => void;

export const initialState: MealsState = {
    fetching: false,
    saving: false,
    searchText: ''
};

export const MealContext = React.createContext<MealsState>(initialState);

const FETCH_MEALS_STARTED = 'FETCH_MEALS_STARTED';
const FETCH_MEALS_SUCCEEDED = 'FETCH_MEALS_SUCCEEDED';
const FETCH_MEALS_FAILED = 'FETCH_MEALS_FAILED';
const SAVE_MEAL_STARTED = 'SAVE_MEAL_STARTED';
const SAVE_MEAL_SUCCEEDED = 'SAVE_MEAL_SUCCEEDED';
const SAVE_MEAL_FAILED = 'SAVE_MEAL_FAILED';
const SET_SEARCH_TEXT = 'SET_SEARCH_TEXT';

const reducer: (state: MealsState, action: ActionProps) => MealsState =
    (state, {type, payload}) => {
        switch (type) {
            case FETCH_MEALS_STARTED:
                return { ...state, fetching: true, fetchingError: null };
            case FETCH_MEALS_SUCCEEDED:
                return { ...state, meals: payload.meals, fetching: false };
            case FETCH_MEALS_FAILED:
                return { ...state, fetchingError: payload.error, fetching: false };
            case SAVE_MEAL_STARTED:
                return { ...state, savingError: null, saving: true };
            case SAVE_MEAL_SUCCEEDED:
                const meals = [...(state.meals || [])];
                const meal = payload.meal;
                let index = meals.findIndex(it => it._id === meal._id);
                if (index === -1)
                    index = meals.findIndex(it => it.name === meal.name);
                if (index === -1) {
                    meals.splice(0, 0, meal);
                } else {
                    meals[index] = meal;
                }
                return { ...state, meals: meals, saving: false };
            case SAVE_MEAL_FAILED:
                return { ...state, savingError: payload.error, saving: false };
            case SET_SEARCH_TEXT:
                return {...state, searchText: payload.text}
            default:
                return state;
        }
    };

interface MealProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const MealProvider: React.FC<MealProviderProps> = ({children}) => {
    const {token} = useContext(AuthContext);
    const [state, dispatch] = useReducer(reducer, initialState);
    const {meals, fetching, fetchingError, saving, savingError, searchText} = state;
    const {networkStatus} = useNetwork()
    const [present] = useIonToast();
    useEffect(executePendingOperations, [networkStatus.connected, token])
    useEffect(getMealsEffect, [token, searchText]);
    useEffect(webSocketsEffect, [token]);
    const saveMeal = useCallback<SaveMealFunction>(saveMealCallback, [networkStatus, token, present]);
    const setSearchText = useCallback<SetSearchTextFunction>(setSearchTextCallback, [])
    const value = {meals, fetching, fetchingError, saving, savingError, saveMeal: saveMeal, searchText, setSearchText};
    return (
        <MealContext.Provider value={value}>
            {children}
        </MealContext.Provider>
    );

    async function fetchMeals() {
        if (!token?.trim()) {
            return;
        }
        try {
            dispatch({type: FETCH_MEALS_STARTED});
            const meals = await getMeals(token, searchText);
            dispatch({type: FETCH_MEALS_SUCCEEDED, payload: {meals: meals}});
        } catch (error) {
            dispatch({type: FETCH_MEALS_FAILED, payload: {error}});
        }
    }

    function getMealsEffect() {
        fetchMeals().then(_ => {
        });
    }

    async function setSearchTextCallback(text: string) {
        dispatch({type: SET_SEARCH_TEXT, payload: {text: text}})
        await fetchMeals()
    }

    async function saveMealCallback(meal: MealProps) {
        try {
            dispatch({type: SAVE_MEAL_STARTED});
            const savedMeal = await (meal._id ? updateMeal(token, meal, networkStatus, present) : createMeal(token, meal, networkStatus, present));
            dispatch({type: SAVE_MEAL_SUCCEEDED, payload: {meal: savedMeal}});
        } catch (error) {
            dispatch({type: SAVE_MEAL_FAILED, payload: {error}});
        }
    }

    function executePendingOperations() {
        async function helperMethod() {
            if (networkStatus.connected && token?.trim()) {
                console.log("executing pending operations")
                const {Storage} = Plugins;
                const {keys} = await Storage.keys();
                for (const key of keys) {
                    if (key.startsWith("sav-")) {
                        const value = JSON.parse((await Storage.get({key: key})).value!!)
                        await createMeal(value.token, value.meal, networkStatus, present)
                        await Storage.remove({key: key})
                    } else if (key.startsWith("upd-")) {
                        const value = JSON.parse((await Storage.get({key: key})).value!!)
                        await updateMeal(value.token, value.meal, networkStatus, present)
                        await Storage.remove({key: key})
                    }
                }
            }
        }
        // noinspection JSIgnoredPromiseFromCall
        helperMethod()
    }

    function webSocketsEffect() {
        let canceled = false;
        let closeWebSocket: () => void;
        if (token?.trim()) {
            closeWebSocket = newWebSocket(token, message => {
                if (canceled) {
                    return;
                }
                const {type, payload: meal} = message;
                if (type === 'created' || type === 'updated') {
                    dispatch({type: SAVE_MEAL_SUCCEEDED, payload: {meal: meal}});
                }
            });
            return () => {
                canceled = true;
                closeWebSocket?.();
            }
        }
    }
}