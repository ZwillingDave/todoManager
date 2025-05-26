import {Navigate} from "react-router-dom";
import Cookies from "js-cookie";
import {jwtDecode} from "jwt-decode";
import React from "react";

interface JwtPayload {
    exp: number;
    username: string;
}

const ProtectedRoute = ({children}: { children: React.ReactElement }) => {
    const token = Cookies.get("token");

    if (!token) {
        return <Navigate to="/"/>;
    }

    try {
        let decoded: JwtPayload;
        decoded = jwtDecode<JwtPayload>(token);
        const now = Date.now() / 1000;

        if (decoded.exp < now) {
            Cookies.remove("token");
            return <Navigate to="/"/>;
        }

        return children;
    } catch (e) {
        Cookies.remove("token");
        return <Navigate to="/"/>;
    }
};

export default ProtectedRoute;



