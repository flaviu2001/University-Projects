import React, {useContext, useEffect, useState} from 'react';

import {
    IonButton,
    IonButtons,
    IonCheckbox,
    IonContent,
    IonDatetime,
    IonHeader,
    IonInput,
    IonItem,
    IonLabel,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import {RouteComponentProps} from 'react-router';
import {dateToString, IonDateTimeDateFormat, MealContext, MealProps, stringToDate} from "./MealCommon";
import NetworkStatus from "./NetworkStatus";

interface EditMealProps extends RouteComponentProps<{
    id?: string;
}> {
}

const EditMeal: React.FC<EditMealProps> = ({history, match}) => {
    const {meals, saving, savingError, saveMeal} = useContext(MealContext);
    const [name, setName] = useState<string>('');
    const [calories, setCalories] = useState<number>(0);
    const [dateAdded, setDateAdded] = useState<Date>(new Date());
    const [vegetarian, setVegetarian] = useState<boolean>(false);
    const [meal, setMeal] = useState<MealProps>();
    const [mealState, setMealState] = useState<boolean>(false);
    useEffect(() => {
        const routeId = match.params.id || '';
        const meal = meals?.find(it => it._id === routeId);
        setMeal(meal);
        if (meal && !mealState) {
            setName(meal.name);
            setCalories(meal.calories);
            setDateAdded(meal.dateAdded);
            setVegetarian(meal.vegetarian);
            setMealState(true)
        }
    }, [match.params.id, mealState, meals, name]);
    const handleSave = () => {
        const editedMeal = meal ? {
            ...meal,
            name: name,
            calories: calories,
            dateAdded: dateAdded,
            vegetarian: vegetarian
        } : {name: name, calories: calories, dateAdded: dateAdded, vegetarian: vegetarian};
        saveMeal && saveMeal(editedMeal).then(() => history.goBack());
    };
    const handleBack = () => {
        history.goBack()
    }
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Edit Meal</IonTitle>
                    <IonButtons slot="end">
                        <IonButton onClick={handleBack}>
                            Back
                        </IonButton>
                        <IonButton onClick={handleSave}>
                            Save
                        </IonButton>
                    </IonButtons>
                    <NetworkStatus/>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonItem>
                    <IonLabel>Name: </IonLabel>
                    <IonInput value={name} onIonChange={e => setName(e.detail.value || '')}/>
                </IonItem>
                <IonItem>
                    <IonLabel>Calories per 100g: </IonLabel>
                    <IonInput type="number" value={calories}
                              onIonChange={e => setCalories(e.detail.value ? +e.detail.value : 0)}/>
                </IonItem>
                <IonItem>
                    <IonLabel position="fixed">Date: </IonLabel>
                    <IonDatetime displayFormat={IonDateTimeDateFormat} value={dateToString(dateAdded)}
                                 onIonChange={e => setDateAdded(stringToDate(e.detail.value))} />
                </IonItem>
                <IonItem>
                    <IonLabel position="fixed">Vegetarian: </IonLabel>
                    <IonCheckbox checked={vegetarian} onIonChange={e => setVegetarian(e.detail.checked)}/>
                </IonItem>
                <IonLoading isOpen={saving}/>
                {savingError && (
                    <div>{savingError.message || 'Failed to save meal'}</div>
                )}
            </IonContent>
        </IonPage>
    );
};

export default EditMeal;
