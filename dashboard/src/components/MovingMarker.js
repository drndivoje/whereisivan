import React, { useEffect, useRef } from 'react';
import { Marker } from 'react-leaflet';
import L from 'leaflet';

const ANIM_DURATION = 4500;

const easeInOut = (t) => t < 0.5 ? 2 * t * t : -1 + (4 - 2 * t) * t;

const personIcon = L.divIcon({
    className: '',
    html: `<svg xmlns="http://www.w3.org/2000/svg" width="36" height="48" viewBox="0 0 36 48">
        <path d="M18 2 C8.059 2 0 10.059 0 20 C0 30 18 46 18 46 C18 46 36 30 36 20 C36 10.059 27.941 2 18 2Z" fill="#2563eb" stroke="white" stroke-width="2.5"/>
        <circle cx="18" cy="14" r="5" fill="white"/>
        <path d="M10 28 C10 22.477 13.582 20 18 20 C22.418 20 26 22.477 26 28" fill="white"/>
    </svg>`,
    iconSize: [36, 48],
    iconAnchor: [18, 46],
    popupAnchor: [0, -46],
});

const MovingMarker = ({ position, children, ...props }) => {
    const markerRef = useRef(null);
    const animFrameRef = useRef(null);
    const stablePosition = useRef(position);

    useEffect(() => {
        const marker = markerRef.current;
        if (!marker) return;

        if (animFrameRef.current) cancelAnimationFrame(animFrameRef.current);

        const current = marker.getLatLng();
        const startLat = current.lat;
        const startLng = current.lng;
        const [endLat, endLng] = position;

        const startTime = performance.now();

        const animate = (now) => {
            const t = Math.min((now - startTime) / ANIM_DURATION, 1);
            const eased = easeInOut(t);
            marker.setLatLng([
                startLat + (endLat - startLat) * eased,
                startLng + (endLng - startLng) * eased,
            ]);
            if (t < 1) {
                animFrameRef.current = requestAnimationFrame(animate);
            }
        };

        animFrameRef.current = requestAnimationFrame(animate);

        return () => {
            if (animFrameRef.current) cancelAnimationFrame(animFrameRef.current);
        };
    }, [position]);

    return (
        <Marker position={stablePosition.current} ref={markerRef} icon={personIcon} {...props}>
            {children}
        </Marker>
    );
};

export default MovingMarker;
