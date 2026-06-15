import 'leaflet/dist/leaflet.css'
import './Dashboard.css'
import { MapContainer, TileLayer, Popup } from 'react-leaflet'
import { useMap, useMapEvents } from 'react-leaflet/hooks'
import MovingMarker from './MovingMarker';
import TrackingStatus from './TrackingStatus';
import { useState, useEffect } from 'react';

function Dashboard({ activityId }) {
    const [data, setData] = useState(null);
    const [zoomLevel, setZoomLevel] = useState(18);
    const backend_host = window.location.hostname === "localhost" ? "http://localhost:8080" : ""
    const finalActivityId = activityId || window.location.pathname.split('/').pop();

    useEffect(() => {
        const fetchData = () => {
            const url = backend_host + '/dashboard/' + finalActivityId;
            console.log("Fetching data from backend at: " + url);
            fetch(url)
                .then(response => response.json())
                .then(json => setData(json))
                .catch(error => console.error(error));
        };

        fetchData();
        const interval = setInterval(fetchData, 5000);
        return () => clearInterval(interval);
    }, []);

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
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
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