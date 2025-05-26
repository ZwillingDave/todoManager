import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import "../styles/Navbar.css"; // Optional if you're using separate styles

function Navbar() {
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const dropdownRef = useRef<HTMLDivElement>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
                setDropdownOpen(false);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    const handleLogout = () => {
        Cookies.remove("token");
        navigate("/");
    };

    return (
        <nav className="navbar">
            <h1 className="navbar-title">Dashboard</h1>
            <div className="profile-container" ref={dropdownRef}>
                <div className="profile-circle" onClick={() => setDropdownOpen(!dropdownOpen)}>
                    <span>U</span>
                </div>
                {dropdownOpen && (
                    <div className="dropdown-menu">
                        <button onClick={handleLogout}>Logout</button>
                    </div>
                )}
            </div>
        </nav>
    );
}

export default Navbar;
