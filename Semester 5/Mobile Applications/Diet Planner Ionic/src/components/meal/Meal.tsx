import React from "react";
import {IonButton, IonCard, IonItem, IonTitle} from "@ionic/react";
import {usePhotoGallery} from "../photo/usePhotoGallery";
import {dateToString} from "../../core";
import {MapModal} from "../modal/MapModal";

interface MealPropsExt extends MealProps {
    onEdit: (_id?: string) => void;
}

export interface MealProps {
    _id?: string;
    name: string;
    calories: number;
    dateAdded: Date;
    vegetarian: boolean;
    latitude: number;
    longitude: number;
}

const Meal: React.FC<MealPropsExt> = ({_id, name, calories, dateAdded, vegetarian, onEdit, latitude, longitude}) => {
    const {photos} = usePhotoGallery();
    // const [showMap, setShowMap] = useState<boolean>(false);
    let filteredPhotos = photos.filter(it => it.filepath.startsWith(`${name}=>`))
    return (
        <IonCard class="meal-card">
            <IonTitle>
                {name}
            </IonTitle>
            <div>
                {
                    filteredPhotos.map(photo =>
                        <img height="200px"
                             src={photo!!.webviewPath}
                             alt="meal"
                        />
                    )
                }
            </div>
            <IonItem>{calories} calories per 100g</IonItem>
            <IonItem>It was added on {dateToString(dateAdded)}</IonItem>
            <IonItem>It is {vegetarian ? "" : "not"} vegetarian</IonItem>
            <IonButton onClick={() => onEdit(_id)}>Edit</IonButton>
            {latitude && longitude &&
            <MapModal latitude={latitude} longitude={longitude}/>
            }
            {/*{latitude && longitude &&*/}
            {/*<IonButton onClick={() => {*/}
            {/*    setShowMap(!showMap)*/}
            {/*}}>Show map</IonButton>*/}
            {/*}*/}
            {/*{latitude && longitude && showMap &&*/}
            {/*<div style={{width: "60%"}}>*/}
            {/*    <MyMap*/}
            {/*        lat={latitude}*/}
            {/*        lng={longitude}*/}
            {/*    />*/}
            {/*</div>}*/}
        </IonCard>
    );
};

export default Meal;
