import {dateToString, MealProps} from "./MealCommon";
import React from "react";
import {IonCard, IonItem, IonTitle} from "@ionic/react";

interface MealPropsExt extends MealProps {
    onEdit: (_id?: string) => void;
}

const Meal: React.FC<MealPropsExt> = ({ _id, name, calories, dateAdded, vegetarian, onEdit }) => {
    return (
        <IonCard onClick={() => onEdit(_id)}>
            <IonTitle>
                {name}
            </IonTitle>
            <IonItem>{calories} calories per 100g</IonItem>
            <IonItem>It was added on {dateToString(dateAdded)}</IonItem>
            <IonItem>It is {vegetarian ? "" : "not"} vegetarian</IonItem>
        </IonCard>
    );
};

export default Meal;
