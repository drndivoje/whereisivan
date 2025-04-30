import L from "leaflet";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import './App.css';
import Dashboard from './components/Dashboard';
import Footer from "./components/Footer";
import Header from "./components/Header";
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
      <Header />
      <main style={{ minHeight: 'calc(100vh - 140px)' }}>
        <Routes>
          <Route path="/" element={<ListActivities />} />
          <Route path="/dashboard/:activityId" element={<Dashboard />} />
        </Routes>
      </main>
      <Footer />
    </Router>
  );
}

export default App;