import React, { useState } from 'react';
import { createAnimation, IonModal, IonButton} from '@ionic/react';
import {MyMap} from "../map/MyMap";
import './MapModal.css'

export interface ModalProps {
    latitude: number;
    longitude: number;
}

export const MapModal: React.FC<ModalProps> = ({latitude, longitude}) => {
  const [showModal, setShowModal] = useState(false);

  const enterAnimation = (baseEl: any) => {
    const backdropAnimation = createAnimation()
      .addElement(baseEl.querySelector('ion-backdrop')!)
      .fromTo('opacity', '0.01', 'var(--backdrop-opacity)');

    const wrapperAnimation = createAnimation()
      .addElement(baseEl.querySelector('.modal-wrapper')!)
      .keyframes([
        { offset: 0, opacity: '0', transform: 'scale(0)' },
        { offset: 1, opacity: '0.99', transform: 'scale(1)' }
      ]);

    return createAnimation()
      .addElement(baseEl)
      .easing('ease-out')
      .duration(500)
      .addAnimation([backdropAnimation, wrapperAnimation]);
  }

  const leaveAnimation = (baseEl: any) => {
    return enterAnimation(baseEl).direction('reverse');
  }

  return (
    <>
      <IonModal cssClass="modal" isOpen={showModal} enterAnimation={enterAnimation} leaveAnimation={leaveAnimation}>
          <MyMap
              lat={latitude}
              lng={longitude}
          />
        <IonButton onClick={() => setShowModal(false)}>Close Map</IonButton>
      </IonModal>
      <IonButton onClick={() => setShowModal(true)}>Show Map</IonButton>
    </>
  );
};
