import React, { useState, useEffect, useRef } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { useNavigate } from 'react-router-dom';
import './styles.css';

const App = () => {
  const navigate = useNavigate();
  const token = localStorage.authToken;
  const [markers, setMarkers] = useState([]);

  const mapRef = useRef(null);

  useEffect(() => {
    const getRandomPosition = () => {
      fetch('http://localhost:8080/map/random/position', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: token,
        },
      })
        .then((response) => {
          if (!response.ok) {
            navigate('/');
          } else {
            return response.json();
          }
        })
        .then((data) => {
          setMarkers(data.position);
          mapRef.current.setView(data.center, data.zoom);
        })
        .catch((ex) => {
          console.log(ex);
          navigate('/');
        });
    };

    const interval = setInterval(() => {
      getRandomPosition();
    }, 1000);

    return () => {
      clearInterval(interval);
    };
  }, [token, navigate]);

  return (
    <div>
      <MapContainer ref={mapRef} whenCreated={(map) => (mapRef.current = map)}>
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url='https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
        />
        {markers.map((marker, index) => (
          <Marker key={index} position={[marker[0], marker[1]]}>
            <Popup>
              A pretty CSS3 popup. <br /> Easily customizable.
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
};

export default App;
