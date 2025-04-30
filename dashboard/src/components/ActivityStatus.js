import React from 'react';
import PropTypes from 'prop-types';
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

ActivityStatus.propTypes = {
    status: PropTypes.string.isRequired,
};

export default ActivityStatus;