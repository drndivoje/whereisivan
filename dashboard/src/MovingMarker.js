import React, { useEffect, useRef } from 'react';
import { Marker } from 'react-leaflet';

const MovingMarker = ({ position, children, ...props }) => {
    const markerRef = useRef(null);

    useEffect(() => {
        if (markerRef.current) {
            markerRef.current.setLatLng(position);
        }
    }, [position]);

    return (
        <Marker position={position} ref={markerRef} {...props}>
            {children}
        </Marker>
    );
};

export default MovingMarker;