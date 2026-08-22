import React from 'react';
import { Link } from 'react-router-dom';
import ActivityStatus from './ActivityStatus';
import './ActivityListItem.css';

const ActivityListItem = ({ activity }) => {
    const lastUpdate = new Date(activity.lastLocation.timeStamp);
    const now = new Date();
    const timeDifference = Math.floor((now - lastUpdate) / 1000);
    const relativeTime =
        timeDifference < 60
            ? `${timeDifference} seconds ago`
            : `${Math.floor(timeDifference / 60)} minutes ago`;

    return (
        <li
            className="activityListItem"
        >
            <Link to={`/dashboard/${activity.id}`} className="activity-link">
                <ActivityStatus status={activity.status} />
                <span className="activity-relative">Last update: {relativeTime}</span>
                
            </Link>
        </li>
    );
};

export default ActivityListItem;