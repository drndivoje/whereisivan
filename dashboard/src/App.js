import logo from './logo.svg';
import './App.css';
import 'leaflet/dist/leaflet.css'
import { MapContainer } from 'react-leaflet/MapContainer'
import { TileLayer } from 'react-leaflet/TileLayer'
import { useMap } from 'react-leaflet/hooks'
import {Popup} from 'react-leaflet/Popup'
import {Marker} from 'react-leaflet/Marker'
import React, { useState, useEffect } from 'react';

function App() {
  const [data, setData] = useState({latitude:33, longitude:33});

  useEffect(() => {
    fetch('http://0.0.0.0:8080/dashboard/current')
      .then(response => response.json())
      .then(json => setData(json))
      .catch(error => console.error(error));
  }, []);
  return (
    <div className="App">

      <MapContainer center={[52.5286832, 13.2080905]} zoom={13} scrollWheelZoom={false} style={{ height: 536 }}>
  <TileLayer
    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
  />
  <Marker position={[52.5286832, 13.2080905]}>
    <Popup>
    latitude :{
    data.latitude} <br /> longitude :{data.longitude}.
    </Popup>
  </Marker>
</MapContainer>
      </div>
     

  );
}

export default App;