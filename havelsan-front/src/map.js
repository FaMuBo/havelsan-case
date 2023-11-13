import React, { useState, useEffect, useRef } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { useNavigate } from 'react-router-dom';
import './styles.css';

const App = () => {
  const navigate = useNavigate();
  const token = localStorage.authToken;
  const [markers, setMarkers] = useState([]);
  let [zoom, setZoom] = useState(parseInt(localStorage.zoom) || 5);
  const mapRef = useRef(null);
  if (mapRef.current != null) {
    let updatedZoom = mapRef?.current?._zoom || 6;
    console.log('a :>> ', updatedZoom);
    console.log('mapRef :>> ', mapRef.current._zoom);
    zoom = updatedZoom;
  }

  useEffect(() => {
    const getRandomPosition = () => {
      fetch(`http://localhost:8080/map/random/position?zoom=${zoom}`, {
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
          console.log('data :>> ', data);
          setMarkers(data.position);
          // mapRef.current.setView([39.453616481252034, 34.92024946948174], data.zoom);
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
  }, [token, navigate, zoom]);

  const handleViewportChanged = (viewport) => {
    const newZoom = Math.round(viewport.zoom);
    if (newZoom !== zoom) {
      setZoom(newZoom);
      localStorage.setItem('zoom', newZoom.toString());
      mapRef.current.setZoom(newZoom);
      console.log('localStorage.zoom :>> ', localStorage);
    }
  };

  return (
    <div>
      <div
        style={{
          position: 'absolute',
          top: 10,
          right: 10,
          background: 'white',
          padding: 1,
          zIndex: 1000,
        }}
      >
        Zoom Level = {zoom}
      </div>
      <MapContainer
        ref={mapRef}
        zoom={zoom}
        center={[37.9334, 35.1597]}
        onViewportChange={(_, viewport) => handleViewportChanged(viewport)}
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url='https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
        />
        {markers.map((marker, index) => (
          <Marker key={index} position={[marker[0], marker[1]]}>
            <Popup>
              !Random Location!
              <br />
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
};

export default App;
