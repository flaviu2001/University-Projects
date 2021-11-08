import React from "react";
import {GoogleMap, Marker, withGoogleMap, withScriptjs} from 'react-google-maps';
import {compose, withProps} from 'recompose';
import {mapsApiKey} from './mapsApiKey';

interface MyMapProps {
    lat?: number;
    lng?: number;
    onMapClick: (e: any) => void;
    onMarkerClick: (e: any) => void;
}

export const MyMap =
    compose<MyMapProps, any>(
        withProps({
            googleMapURL:
                `https://maps.googleapis.com/maps/api/js?key=${mapsApiKey}&v=3.exp&libraries=geometry,drawing,places`,
            loadingElement: <div style={{height: `100%`}}/>,
            containerElement: <div style={{height: `600px`}}/>,
            mapElement: <div style={{height: `100%`}}/>
        }),
        withScriptjs,
        withGoogleMap
    )(props => (
        <GoogleMap
            defaultZoom={8}
            defaultCenter={{lat: props.lat!!, lng: props.lng!!}}
            onClick={props.onMapClick}
        >
            <Marker
                position={{lat: props.lat!!, lng: props.lng!!}}
                onClick={props.onMarkerClick}
            />
        </GoogleMap>
    ))
