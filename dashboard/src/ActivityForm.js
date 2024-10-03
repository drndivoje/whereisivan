// src/ActivityForm.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import './ActivityForm.css';

const ActivityForm = () => {
  const [activityId, setActivityId] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    if (activityId) {
      // Redirect to the dashboard with the entered activity ID
      navigate(`/dashboard/${activityId}`);
    }
  };

  return (
    <div className="form-container">
      {/* Welcome Message Section */}
      <div className="welcome-message">
        <h1>Welcome to the Activity Tracker!</h1>
        <p>Enjoy tracking your activity with this awesome app.</p>
        {/* Cat GIF */}
        <img 
          src="./catlogo.png" 
          alt="Cat Riding Bicycle"
          className="cat-gif"
        />
      </div>

      {/* Activity ID Form Section */}
      <form onSubmit={handleSubmit} className="activity-form">
        <input
          type="text"
          placeholder="Enter Activity ID"
          value={activityId}
          onChange={(e) => setActivityId(e.target.value)}
          required
        />
        <button type="submit">Go to Dashboard</button>
      </form>
    </div>
  );
};

export default ActivityForm;
