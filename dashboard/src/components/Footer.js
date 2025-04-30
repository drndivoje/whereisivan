import React from 'react';

function Footer() {
  return (
    <footer style={{ padding: '10px', backgroundColor: '#f2f2f2', textAlign: 'center' }}>
      &copy; {new Date().getFullYear()} Activity Tracker
    </footer>
  );
}

export default Footer;