import 'leaflet/dist/leaflet.css'
import './Dashboard.css'
import { MapContainer } from 'react-leaflet/MapContainer'
import { TileLayer } from 'react-leaflet/TileLayer'
import { Polyline } from 'react-leaflet';
import { useMap } from 'react-leaflet/hooks'
import { Popup } from 'react-leaflet/Popup'
import { Marker } from 'react-leaflet/Marker'
import React, { useState, useEffect } from 'react';
function Dashboard() {
    const [data, setData] = useState({ latitude: 52.51632949, longitude: 13.37684391, time: 0, path: [[]] });
    const fillBlueOptions = { fillColor: 'blue' }
    const backend_host = "localhost:8080"
    useEffect(() => {
        const interval = setInterval(() => {
            fetch('http://' + backend_host + '/dashboard/current')
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
            <MapContainer center={[data.latitude, data.longitude]} zoom={13} scrollWheelZoom={false} style={{ height: 536 }} >
                <MapRecenter lat={data.latitude} lng={data.longitude} zoomLevel={13} />
                <TileLayer
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <Marker position={[data.latitude, data.longitude]}>
                    <Popup>
                        Seen on: <br />{new Date(data.time).toLocaleString()}.
                    </Popup>
                </Marker>
                <Polyline pathOptions={fillBlueOptions} positions={data.path} />
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
        return <header className="dashboard-header">
            <div>Speed: {data ? `${data.currentSpeed} km/h` : "N/A"}</div>
            <div>Last Update: {data ? new Date(data.time).toLocaleString() : "N/A"}</div>
        </header>
    } else {
        return <header className="dashboard-header">
            <div>
                Waiting for updates...
            </div>
        </header>
    }

};

export default Dashboard