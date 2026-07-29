import React from 'react';
import './ActivityStatus.css'; // Import CSS for styling

const ActivityStatus = ({ status }) => {
    return (
        <div>
            {status === 'STARTED' ? (
                <span className="live-status">LIVE</span>
            ) : status === 'FINISHED' ? (
                <span className="finished-status">Finished</span>
            ) : null}
        </div>
    );
};

export default ActivityStatus;