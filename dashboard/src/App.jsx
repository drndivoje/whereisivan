import L from "leaflet";
import './App.css';
import Dashboard from './components/Dashboard';

// Fix for Leaflet marker icon not displaying
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon-2x.png",
  iconUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png",
  shadowUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png"
});

function App() {
  return (
    <div className="container">
      <header className="topbar">
        <div className="branding">
          <img src="/logo.svg" alt="WhereIsIvan" />
          <h1>WhereIsIvan</h1>
        </div>
      </header>

      <div className="main-content">
        <main style={{ minHeight: 'calc(100vh - 140px)' }}>
          <Dashboard />
        </main>
      </div>
    </div>
  );
}

export default App;
