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
                <section className="calender-section">

                    <Calendar onChange={handleDateChange} value={date} selectRange={false} />
                    {date instanceof Date && <p>Selected date: {date.toDateString()}</p>}
                </section>


            </header>
        </div>
    );
}

export default Dashboard;

