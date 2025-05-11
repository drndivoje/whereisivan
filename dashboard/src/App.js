import L from "leaflet";
import React from 'react';
import { Link, Route, BrowserRouter as Router, Routes } from "react-router-dom";
import './App.css';
import CreateUser from "./components/CreateUser";
import Dashboard from './components/Dashboard';
import ListActivities from './components/ListActivities';
import LoginUser from "./components/LoginUser";

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
            <h1>Activity Tracker</h1>
          </div>
          <nav>
            <Link to="/">Dashboard</Link>
            <Link to="/register">Register</Link>
            <Link to="/login">Login</Link>
          </nav>
        </div>

        {/* Mobile Topbar */}
        <div className="mobile-navbar">
          <div className="branding">
            <h1>Activity Tracker</h1>
          </div>
          <nav>
            <Link to="/">Dashboard</Link>
            <Link to="/register">Register</Link>
            <Link to="/login">Login</Link>
          </nav>
        </div>

        <div className="main-content">
          <main style={{ minHeight: 'calc(100vh - 140px)' }}>
            <Routes>
              <Route path="/" element={<ListActivities />} />
              <Route path="/dashboard/:activityId" element={<Dashboard />} />
              <Route path="/register" element={<CreateUser />} />
              <Route path="/login" element={<LoginUser />} />
            </Routes>
          </main>
        </div>
      </div>
    </Router>
  );
}

export default App;