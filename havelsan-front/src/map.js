import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { useNavigate } from 'react-router-dom';
import './styles.css';

function App() {
  const navigate = useNavigate();
  const token = localStorage.authToken;
  const [markers, setMarkers] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [initialPosition, setInitialPosition] = useState([0, 0]);

  useEffect(() => {
    fetch('http://localhost:8080/user/validate', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: token,
      },
    })
      .then((response) => {
        if (!response.ok) {
          navigate('/');
        }
      })
      .catch((error) => {
        console.error('Token doğrulama hatası:', error);
      });

    function getRandomPosition() {
      const lat = -90 + Math.random() * 180;
      const lng = -180 + Math.random() * 360;
      return [lat, lng];
    }

    setInitialPosition(getRandomPosition());

    const initialMarkers = Array.from({ length: 100 }, () => ({
      position: getRandomPosition(),
    }));

    setMarkers(initialMarkers);

    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) => (prevIndex + 1) % 100);
    }, 1000);

    return () => {
      clearInterval(interval);
    };
  }, [currentIndex]);

  return (
    <div>
      <MapContainer center={initialPosition} zoom={2} scrollWheelZoom={false}>
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url='https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
        />
        {markers.map((marker, index) => (
          <Marker key={index} position={marker.position}>
            <Popup>
              A pretty CSS3 popup. <br /> Easily customizable.
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
}

export default App;
