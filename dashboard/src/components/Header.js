import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Header.css';

function Header() {
  const location = useLocation();
  const isHome = location.pathname === '/';
  const label = isHome ? '' : '< Back';

  return (
    <header className="header">
      <Link to="/" className="menu-item">
        {label}
      </Link>
    </header>
  );
}

export default Header;