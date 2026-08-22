import React, { useEffect, useState } from 'react';
import ActivityListItem from './ActivityListItem';
import './ListActivities.css';

const ListActivities = () => {
    const [activities, setActivities] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const backend_host =
        window.location.hostname === 'localhost'
            ? 'http://localhost:8080'
            : '';

    useEffect(() => {
        const fetchActivities = async () => {
            try {
                const response = await fetch(backend_host + '/activities');
                if (response.status === 204 || !response.ok) {
                    setActivities([]);
                    return;
                }

                const data = await response.json();
                setActivities(data);

            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchActivities();
    }, []);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div className="activities-container">
            <h3 className="activities-header">Current Activities</h3>
            {activities.length === 0 ? (
                <p className="empty-message">
                    No activities available at the moment.
                </p>
            ) : (
                <ul className="activity-list">
                    {activities.map((activity) => (
                        <ActivityListItem key={activity.id} activity={activity} />
                    ))}
                </ul>
            )}
        </div>
    );
};

export default ListActivities;