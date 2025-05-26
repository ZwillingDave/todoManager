import React, {useState} from 'react';
import './styles/Dashboard.css';
import Navbar from "./components/Navbar";
import Calendar, {CalendarProps} from "react-calendar";
import "./styles/CustomCalendar.css";
import {Value} from "react-calendar/dist/cjs/shared/types";

function Dashboard() {
    let [date, setDate] = useState<Value>(new Date());

    const handleDateChange: CalendarProps['onChange'] = (value, _event) => {
        setDate(value);
    };
    return (
        <div className="Dashboard">
            <Navbar/>
            <header className="Dashboard-header">
                <section className="appointment-section">
                    <h1>Appointments</h1>
                </section>
                <section className="calender-section">

                    <Calendar onChange={handleDateChange} value={date} selectRange={false} />
                </section>


            </header>
        </div>
    );
}

export default Dashboard;

