import 'leaflet/dist/leaflet.css'
import './Dashboard.css'
import { MapContainer, TileLayer, Popup } from 'react-leaflet'
import { useMap, useMapEvents } from 'react-leaflet/hooks'
import MovingMarker from './MovingMarker';
import React, { useState, useEffect } from 'react';
import { formatMilliseconds } from '../utils/format';
function Dashboard({ activityId }) {
    const [data, setData] = useState({ latitude: 52.51632949, longitude: 13.37684391, time: 0, path: [[]] });
    const [zoomLevel, setZoomLevel] = useState(13);
    const backend_host = window.location.hostname === "localhost" ? "http://localhost:8080" : ""
    const finalActivityId = activityId || window.location.pathname.split('/').pop();
    useEffect(() => {
        const interval = setInterval(() => {
            var url = backend_host + '/dashboard/' + finalActivityId
            console.log("Fetching data from backend at: " + url)
            fetch(url)
                .then(response => response.json())
                .then(json => setData(json))
                .catch(error => console.error(error));
        }, 5000);

        //Clearing the interval
        return () => clearInterval(interval);
    }, []);

    return (
        <div className="dashboard-container">
            <ConnectionStatus data={data}></ConnectionStatus>
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
        // Fly to that coordinates and set new zoom level
        map.flyTo([lat, lng], zoomLevel);
    }, [lat, lng]);
    return null;

};
const ConnectionStatus = ({ data }) => {
    if (data.time > 0) {
        return <div className="dashboard-header">
            <div><div class="label">Elapsed time:</div> <div class="metric">{data ? `${formatMilliseconds(data.elapsedTime)}` : "N/A"}</div></div>
            <div><div class="label">Speed:</div> <div class="metric">{data ? `${data.currentSpeed} km/h` : "N/A"}</div></div>
            <div><div class="label">Distance:</div> <div class="metric">{data ? `${data.distance} m` : "N/A"}</div></div>
            <div><div class="label">Last Update:</div><div class="metric"> {data ? new Date(data.time).toLocaleString() : "N/A"}</div></div>
        </div>
    } else {
        return <div className="dashboard-header">
            <div>
                Waiting for updates...
            </div>
        </div>
    }

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