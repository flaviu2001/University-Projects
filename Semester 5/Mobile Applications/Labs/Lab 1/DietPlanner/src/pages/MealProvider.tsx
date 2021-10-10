import React, {useCallback, useEffect, useReducer} from "react";
import PropTypes from "prop-types";
import {createMeal, getMeals, newWebSocket, updateMeal} from "./MealService";
import {initialState, MealProps, MealsState, SaveMealFunction} from "./MealCommon";
import {MealContext} from "./MealCommon";

interface ActionProps {
    type: string,
    payload?: any,
}

const FETCH_MEALS_STARTED = 'FETCH_MEALS_STARTED';
const FETCH_MEALS_SUCCEEDED = 'FETCH_MEALS_SUCCEEDED';
const FETCH_MEALS_FAILED = 'FETCH_MEALS_FAILED';
const SAVE_MEAL_STARTED = 'SAVE_MEAL_STARTED';
const SAVE_MEAL_SUCCEEDED = 'SAVE_MEAL_SUCCEEDED';
const SAVE_MEAL_FAILED = 'SAVE_MEAL_FAILED';

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
                const index = meals.findIndex(it => it.id === meal.id);
                if (index === -1) {
                    meals.splice(0, 0, meal);
                } else {
                    meals[index] = meal;
                }
                return { ...state, meals: meals, saving: false };
            case SAVE_MEAL_FAILED:
                return { ...state, savingError: payload.error, saving: false };
            default:
                return state;
        }
    };

interface MealProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const MealProvider: React.FC<MealProviderProps> = ({children}) => {
    const [state, dispatch] = useReducer(reducer, initialState);
    const { meals, fetching, fetchingError, saving, savingError } = state;
    useEffect(getMealsEffect, []);
    useEffect(webSocketsEffect, []);
    const saveMeal = useCallback<SaveMealFunction>(saveMealCallback, []);
    const value = { meals, fetching, fetchingError, saving, savingError, saveMeal: saveMeal };
    return (
        <MealContext.Provider value={value}>
            {children}
        </MealContext.Provider>
    );

    function getMealsEffect() {
        let canceled = false;
        fetchMeals().then(_ => {});
        return () => {
            canceled = true;
        }

        async function fetchMeals() {
            try {
                dispatch({ type: FETCH_MEALS_STARTED });
                const meals = await getMeals();
                console.log(meals);
                if (!canceled)
                    dispatch({ type: FETCH_MEALS_SUCCEEDED, payload: { meals: meals } });
            } catch (error) {
                dispatch({ type: FETCH_MEALS_FAILED, payload: { error } });
            }
        }
    }

    async function saveMealCallback(meal: MealProps) {
        try {
            dispatch({ type: SAVE_MEAL_STARTED });
            console.log(meal.id);
            const savedMeal = await (meal.id ? updateMeal(meal) : createMeal(meal));
            dispatch({ type: SAVE_MEAL_SUCCEEDED, payload: { meal: savedMeal } });
        } catch (error) {
            dispatch({ type: SAVE_MEAL_FAILED, payload: { error } });
        }
    }

    function webSocketsEffect() {
        let canceled = false;
        const closeWebSocket = newWebSocket(message => {
            if (canceled) {
                return;
            }
            const { event, payload: { meal }} = message;
            if (event === 'created' || event === 'updated') {
                dispatch({ type: SAVE_MEAL_SUCCEEDED, payload: { meal: meal } });
            }
        });
        return () => {
            canceled = true;
            closeWebSocket();
        }
    }
}
