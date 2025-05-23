import React from 'react';
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';
import logo from './logo.svg';
import './App.css';
import * as http from "node:http";


function Dashboard() {
  const navigate = useNavigate();

  return (
    <div className="App">
      <header className="App-header">
        <section>
          <h1>Dashboard</h1>
        </section>



      </header>
    </div>
  );
}

export default Dashboard;
