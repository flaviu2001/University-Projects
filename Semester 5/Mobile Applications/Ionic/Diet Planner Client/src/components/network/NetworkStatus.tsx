import {useNetwork} from "./useNetwork";
import React from "react";
import {IonItem} from "@ionic/react";

const NetworkStatus: React.FC = () => {
    const {networkStatus} = useNetwork();
    return (
        <div>
            {
                networkStatus.connected &&
                <IonItem>
                    Connected
                </IonItem>
            }
            {
                !networkStatus.connected &&
                <IonItem>
                    Not Connected
                </IonItem>
            }
        </div>
    )
};

export default NetworkStatus