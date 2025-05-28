import React, {useState} from 'react';
import './styles/Dashboard.css';
import Navbar from "./components/Navbar";
import Calendar, {CalendarProps} from "react-calendar";
import "./styles/CustomCalendar.css";
import {Value} from "react-calendar/dist/cjs/shared/types";

function Dashboard() {
    const [date, setDate] = useState<Value>(new Date());

    const handleDateChange: CalendarProps['onChange'] = (value, _event) => {
        setDate(value);
    };
    return (
        <div className="Dashboard">
            <Navbar/>
            <header className="Dashboard-header">
                <section className="appointment-section">
                    <section><h1>Appointments from<br/>
                        {date instanceof Date && date.toLocaleDateString()}</h1></section>
                    <section className={"appointment-list"}><div className="appointment-item">
                        <h2>Appointment 1</h2>

                    </div></section>
                    <section><button>Add Appointment</button></section>
                </section>
                <section className="calender-section">

                    <Calendar onChange={handleDateChange} value={date} selectRange={false} />
                </section>


            </header>
        </div>
    );
}

export default Dashboard;

