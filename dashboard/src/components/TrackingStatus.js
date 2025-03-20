import { formatMilliseconds } from '../utils/format';

const TrackingStatus = ({ data }) => {
    if (data.time > 0) {
        return <div className="dashboard-header">
            <div>
                <div class="label">Elapsed time:</div>
                <div class="metric">{data ? `${formatMilliseconds(data.elapsedTime)}` : "N/A"}</div>
            </div>
            <div>
                <div class="label">Speed:</div>
                <div class="metric">{data ? `${data.currentSpeed} m/s` : "N/A"}</div>
            </div>
            <div>
                <div class="label">Distance:</div> 
                <div class="metric">{data ? `${data.distance} m` : "N/A"}</div>
            </div>
            <div>
                <div class="label">Last Update:</div>
                <div class="metric"> {data ? new Date(data.time).toLocaleString() : "N/A"}</div>
            </div>
        </div>
    } else {
        return <div className="dashboard-header">
            <div>
                Waiting for updates...
            </div>
        </div>
    }
};
export default TrackingStatus;