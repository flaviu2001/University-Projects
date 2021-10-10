import {dateToString, MealProps} from "./MealCommon";
import React from "react";
import {IonItem, IonLabel} from "@ionic/react";

interface ItemPropsExt extends MealProps {
    onEdit: (id?: string) => void;
}

const Item: React.FC<ItemPropsExt> = ({ id, name, calories, dateAdded, vegetarian, onEdit }) => {
    return (
        <IonItem onClick={() => onEdit(id)}>
            <IonLabel>{name} has {calories}. It was added on {dateToString(dateAdded)} and it is {vegetarian ? "" : "not"} vegetarian</IonLabel>
        </IonItem>
    );
};

export default Item;
