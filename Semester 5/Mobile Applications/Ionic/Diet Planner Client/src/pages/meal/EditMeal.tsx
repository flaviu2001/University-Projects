import React, {useContext, useEffect, useState} from 'react';

import {
    IonActionSheet,
    IonButton,
    IonButtons,
    IonCheckbox,
    IonContent,
    IonDatetime,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon,
    IonInput,
    IonItem,
    IonLabel,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import {RouteComponentProps} from 'react-router';
import NetworkStatus from "../network/NetworkStatus";
import {camera, close, trash} from "ionicons/icons";
import {Photo, usePhotoGallery} from '../photo/usePhotoGallery';
import {MealContext} from "./MealProvider";
import {MealProps} from "./Meal";
import {dateToString, IonDateTimeDateFormat, stringToDate} from "../../core";
import {AuthContext} from "../auth/AuthProvider";
import {useMyLocation} from "../map/useMyLocation";
import {MyMap} from "../map/MyMap";

interface EditMealProps extends RouteComponentProps<{
    id?: string;
}> {
}

const EditMeal: React.FC<EditMealProps> = ({history, match}) => {
    const myLocation = useMyLocation();
    const [latitude, setLatitude] = useState<number|undefined>(undefined)
    const [longitude, setLongitude] = useState<number|undefined>(undefined)
    const {meals, saving, savingError, saveMeal} = useContext(MealContext);
    const {token} = useContext(AuthContext);
    const [name, setName] = useState<string>('');
    const [calories, setCalories] = useState<number>(0);
    const [dateAdded, setDateAdded] = useState<Date>(new Date());
    const [vegetarian, setVegetarian] = useState<boolean>(false);
    const [meal, setMeal] = useState<MealProps>();
    const [mealState, setMealState] = useState<boolean>(false);
    const {photos, takePhoto, deletePhoto} = usePhotoGallery(token);
    const [photoToDelete, setPhotoToDelete] = useState<Photo>();
    useEffect(() => {
        let lat = myLocation.position?.coords.latitude
        let lng = myLocation.position?.coords.longitude
        if (lat && lng && !latitude && !longitude) {
            setLatitude(lat)
            setLongitude(lng)
        }
    }, [latitude, longitude, myLocation.position])
    useEffect(() => {
        const routeId = match.params.id || '';
        const meal = meals?.find(it => it._id === routeId);
        setMeal(meal);
        if (meal && !mealState) {
            setName(meal.name);
            setCalories(meal.calories);
            setDateAdded(meal.dateAdded);
            setVegetarian(meal.vegetarian);
            if (!latitude)
                setLatitude(meal.latitude)
            if (!longitude)
                setLongitude(meal.longitude)
            setMealState(true)
        }
    }, [latitude, longitude, match.params.id, mealState, meals, name]);
    const handleSave = () => {
        const editedMeal = meal ? {
            ...meal,
            name: name,
            calories: calories,
            dateAdded: dateAdded,
            vegetarian: vegetarian,
            latitude: latitude??0,
            longitude: longitude??0
        } : {name: name, calories: calories, dateAdded: dateAdded, vegetarian: vegetarian, latitude: latitude??0, longitude: longitude??0};
        saveMeal && saveMeal(editedMeal).then(() => history.goBack());
    };
    const handleBack = () => {
        history.goBack()
    }

    function onMap() {
        return (e: any) => {
            setLatitude(e.latLng.lat())
            setLongitude(e.latLng.lng())
        };
    }

    let filteredPhotos = photos.filter(it => it.filepath.startsWith(`${meal!!.name}=>`))
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
                                 onIonChange={e => setDateAdded(stringToDate(e.detail.value))}/>
                </IonItem>
                <IonItem>
                    <IonLabel position="fixed">Vegetarian: </IonLabel>
                    <IonCheckbox checked={vegetarian} onIonChange={e => setVegetarian(e.detail.checked)}/>
                </IonItem>
                <div>
                    {
                        filteredPhotos.map(photo =>
                            <img height="300px"
                                 src={photo!!.webviewPath}
                                 onClick={() => setPhotoToDelete(photo)}
                                 alt="meal"
                            />
                        )
                    }
                </div>
                {latitude && longitude &&
                <div style={{width: "60%"}}>
                    <MyMap
                        lat={latitude}
                        lng={longitude}
                        onMapClick={onMap()}
                    />
                </div>}
                <IonLoading isOpen={saving}/>
                {savingError && (
                    <div>{savingError.message || 'Failed to save meal'}</div>
                )}
                <IonFab vertical="bottom" horizontal="center" slot="fixed">
                    <IonFabButton onClick={() => takePhoto(meal!!.name)}>
                        <IonIcon icon={camera}/>
                    </IonFabButton>
                </IonFab>
                <IonActionSheet
                    isOpen={!!photoToDelete}
                    buttons={[{
                        text: 'Delete',
                        role: 'destructive',
                        icon: trash,
                        handler: () => {
                            if (photoToDelete) {
                                deletePhoto(photoToDelete).then(_ => {
                                });
                                setPhotoToDelete(undefined);
                            }
                        }
                    }, {
                        text: 'Cancel',
                        icon: close,
                        role: 'cancel'
                    }]}
                    onDidDismiss={() => setPhotoToDelete(undefined)}
                />
            </IonContent>
        </IonPage>
    );
};

export default EditMeal;
