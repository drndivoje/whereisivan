import './App.css';
import Dashboard from './Dashboard';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import ActivityForm from "./ActivityForm";
import L from "leaflet";
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
      <Routes>
        {/* Route for the form */}
        <Route path="/" element={<ActivityForm />} />
        {/* Route for the dashboard with activity id in URL */}
        <Route path="/dashboard/:activityId" element={<Dashboard />} />
      </Routes>
    </Router>
  );
}

export default App;
