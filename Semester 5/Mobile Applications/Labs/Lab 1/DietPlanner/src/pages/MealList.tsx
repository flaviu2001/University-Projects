import React, {useContext} from 'react';
import {RouteComponentProps} from 'react-router';
import {
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon,
    IonList,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import {add} from 'ionicons/icons';
import {MealContext} from "./MealCommon";
import Meal from "./Meal";

const MealList: React.FC<RouteComponentProps> = ({history}) => {
    const {meals, fetching, fetchingError} = useContext(MealContext);
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Diet Planner</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonLoading isOpen={fetching} message="Fetching items"/>
                {meals && (
                    <IonList>
                        {meals.map(({id, name, calories, dateAdded, vegetarian}) =>
                            <Meal key={id} id={id} name={name} calories={calories} dateAdded={dateAdded}
                                  vegetarian={vegetarian} onEdit={id => history.push(`/meal/${id}`)}/>)}
                    </IonList>
                )}
                {fetchingError && (
                    <div>{fetchingError.message || 'Failed to fetch items'}</div>
                )}
                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={() => history.push('/meal')}>
                        <IonIcon icon={add}/>
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );
};

export default MealList;
