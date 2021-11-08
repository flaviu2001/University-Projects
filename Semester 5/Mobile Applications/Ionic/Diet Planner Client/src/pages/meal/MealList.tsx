import React, {useContext, useState} from 'react';
import {RouteComponentProps} from 'react-router';
import {
    IonButton,
    IonButtons,
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon, IonInfiniteScroll, IonInfiniteScrollContent, IonPage, IonSearchbar,
    IonTitle,
    IonToolbar, useIonViewWillEnter
} from '@ionic/react';
import {add} from 'ionicons/icons';
import Meal, {MealProps} from "./Meal";
import NetworkStatus from "../network/NetworkStatus";
import {MealContext} from "./MealProvider";
import {AuthContext} from "../auth/AuthProvider";

const indicesPresented = 5;

const MealList: React.FC<RouteComponentProps> = ({history}) => {
    const {meals, fetchingError, setSearchText, searchText} = useContext(MealContext);
    const {logout} = useContext(AuthContext);
    const [index, setIndex] = useState<number>(0);
    const [items, setItems] = useState<MealProps[]>([]);
    const [hasMore, setHasMore] = useState<boolean>(true);
    const [hasFetched, setHasFetched] = useState<boolean>(false);

    if (!hasFetched) {
        if (meals) {
            fetchData()
            setHasFetched(true);
        }
    }

    function fetchData() {
        if (meals) {
            const newIndex = Math.min(index + indicesPresented, meals.length);
            if (newIndex >= meals.length)
                setHasMore(false);
            else setHasMore(true);
            setItems(meals.slice(0, newIndex));
            setIndex(newIndex)
        }
    }

    useIonViewWillEnter(async () => {
        await fetchData();
    });

    async function searchNext($event: CustomEvent<void>) {
        await fetchData();
        await ($event.target as HTMLIonInfiniteScrollElement).complete();
    }

    function handleLogout() {
        logout?.()
        history.push('/login')
    }

    function handleTextChange(e: any) {
        setItems([])
        setIndex(0)
        setHasFetched(false)
        setHasMore(true)
        setSearchText?.(e.detail.value!!)
    }

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Diet Planner</IonTitle>
                    <NetworkStatus/>
                    <IonButtons slot="end">
                        <IonButton onClick={handleLogout}>
                            Logout
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent id="dude">
                <IonSearchbar value={searchText} onIonChange={e => handleTextChange(e)}/>
                {items.map(({_id, name, calories, dateAdded, vegetarian, latitude, longitude}) =>
                    <Meal key={_id} _id={_id} name={name} calories={calories} dateAdded={dateAdded}
                          vegetarian={vegetarian} latitude={latitude} longitude={longitude} onEdit={id => history.push(`/meal/${id}`)}/>)}
                <IonInfiniteScroll threshold="0px" disabled={!hasMore}
                                   onIonInfinite={(e: CustomEvent<void>) => searchNext(e)}>
                    <IonInfiniteScrollContent
                        loadingText="Loading more food...">
                    </IonInfiniteScrollContent>
                </IonInfiniteScroll>
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
