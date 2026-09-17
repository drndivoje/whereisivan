import 'leaflet/dist/leaflet.css'
import './Dashboard.css'
import { MapContainer, TileLayer, Popup } from 'react-leaflet'
import { useMap, useMapEvents } from 'react-leaflet/hooks'
import MovingMarker from './MovingMarker';
import TrackingStatus from './TrackingStatus';
import { useState, useEffect } from 'react';

function Dashboard() {
    const [data, setData] = useState(null);
    const [zoomLevel, setZoomLevel] = useState(18);
    const [hasNoCurrentActivity, setHasNoCurrentActivity] = useState(false);
    const backend_host = window.location.hostname === "localhost" ? "http://localhost:8080" : ""

    useEffect(() => {
        const fetchData = async () => {
            const url = backend_host + '/api/dashboard';
            console.log("Fetching data from backend at: " + url);

            try {
                const response = await fetch(url);

                if (response.status === 204) {
                    setHasNoCurrentActivity(true);
                    setData(null);
                    return;
                }

                if (!response.ok) {
                    throw new Error(`Request failed with status ${response.status}`);
                }

                const json = await response.json();
                const normalizedData = Array.isArray(json) ? json[0] : json;

                if (!normalizedData || !Number.isFinite(normalizedData.latitude) || !Number.isFinite(normalizedData.longitude)) {
                    const latitude = normalizedData?.lastLocation?.latitude ?? normalizedData?.latitude;
                    const longitude = normalizedData?.lastLocation?.longitude ?? normalizedData?.longitude;
                    const time = normalizedData?.lastLocation?.timeStamp ?? normalizedData?.time ?? normalizedData?.lastLocation?.time ?? Date.now();

                    if (!Number.isFinite(latitude) || !Number.isFinite(longitude)) {
                        setHasNoCurrentActivity(true);
                        setData(null);
                        return;
                    }

                    setHasNoCurrentActivity(false);
                    setData({ ...normalizedData, latitude, longitude, time });
                    return;
                }

                setHasNoCurrentActivity(false);
                setData(normalizedData);
            } catch (error) {
                console.error(error);
                setHasNoCurrentActivity(true);
                setData(null);
            }
        };

        fetchData();
        const interval = setInterval(fetchData, 5000);
        return () => clearInterval(interval);
    }, []);

    if (hasNoCurrentActivity) {
        return (
            <div className="dashboard-container">
                <div className="dashboard-loading">
                    <span className="dashboard-loading-text">No current activity.</span>
                </div>
            </div>
        );
    }

    if (!data) {
        return (
            <div className="dashboard-container">
                <div className="dashboard-loading">
                    <span className="dashboard-loading-text">Waiting for location data...</span>
                </div>
            </div>
        );
    }

    return (
        <div className="dashboard-container">
            <TrackingStatus data={data}></TrackingStatus>
            <MapContainer center={[data.latitude, data.longitude]} zoom={zoomLevel} scrollWheelZoom={false} style={{ height: 536 }} >
                <ZoomHandler onZoomChange={setZoomLevel} />
                <MapRecenter lat={data.latitude} lng={data.longitude} zoomLevel={zoomLevel} />
                <TileLayer
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>'
                    url={process.env.REACT_APP_TILE_URL || "https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png"}
                />
                <MovingMarker position={[data.latitude, data.longitude]}>
                    <Popup>
                        Seen on: <br />{new Date(data.time).toLocaleString()}.
                    </Popup>
                </MovingMarker>
            </MapContainer>
        </div>
    );

}
const MapRecenter = ({ lat, lng, zoomLevel }) => {
    const map = useMap();

    useEffect(() => {
        map.flyTo([lat, lng], zoomLevel);
    }, [lat, lng, zoomLevel]);
    return null;

};

const ZoomHandler = ({ onZoomChange }) => {
    const map = useMapEvents({
        zoom: () => {
            onZoomChange(map.getZoom());
        },
    });
    return null;
};

export default Dashboard