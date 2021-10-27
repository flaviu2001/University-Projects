import {dateToString, MealProps} from "./MealCommon";
import React from "react";
import {IonItem, IonLabel} from "@ionic/react";

interface MealPropsExt extends MealProps {
    onEdit: (_id?: string) => void;
}

const Meal: React.FC<MealPropsExt> = ({ _id, name, calories, dateAdded, vegetarian, onEdit }) => {
    return (
        <IonItem onClick={() => onEdit(_id)}>
            <IonLabel>{name} has {calories}. It was added on {dateToString(dateAdded)} and it is {vegetarian ? "" : "not"} vegetarian</IonLabel>
        </IonItem>
    );
};

export default Meal;
