import React from "react";
import dateFormat from "dateformat";

export const libraryDateFormat = "yyyy-mm-dd HH:mm";
export const IonDateTimeDateFormat = "YYYY-MM-DD HH:mm";

export interface MealProps {
    _id?: string;
    name: string;
    calories: number;
    dateAdded: Date;
    vegetarian: boolean;
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

export function dateToString(date: Date): string {
    return dateFormat(date, libraryDateFormat);
}

export function stringToDate(string: string | undefined | null): Date {
    return new Date(string || new Date());
}
