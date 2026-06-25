import L from "leaflet";
import React from 'react';
import { Link, Route, BrowserRouter as Router, Routes } from "react-router-dom";
import './App.css';
import Dashboard from './components/Dashboard';
import ListActivities from './components/ListActivities';

// Fix for Leaflet marker icon not displaying
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon-2x.png",
  iconUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png",
  shadowUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png"
});

function App() {
  return (
    <Router>
      <div className="container">
        {/* Desktop Sidebar */}
        <div className="sidebar">
          <div className="branding">
            <img src="/logo.svg" alt="WhereIsIvan" />
            <h1>WhereIsIvan</h1>
          </div>
          <nav>
            <Link to="/">Activities</Link>
          </nav>
        </div>

        {/* Mobile Topbar */}
        <div className="mobile-navbar">
          <div className="branding">
            <img src="/logo.svg" alt="WhereIsIvan" />
            <h1>WhereIsIvan</h1>
          </div>
          <nav>
            <Link to="/">Activities</Link>
            <Link to="/register">Register</Link>
            <Link to="/login">Login</Link>
          </nav>
        </div>

        <div className="main-content">
          <main style={{ minHeight: 'calc(100vh - 140px)' }}>
            <Routes>
              <Route path="/" element={<ListActivities />} />
              <Route path="/dashboard/:activityId" element={<Dashboard />} />
         
            </Routes>
          </main>
        </div>
      </div>
    </Router>
  );
}

export default App;