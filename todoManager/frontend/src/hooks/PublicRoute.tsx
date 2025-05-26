// src/hooks/PublicRoute.tsx
import { Navigate } from "react-router-dom";
import Cookies from "js-cookie";
import { jwtDecode } from "jwt-decode";

interface JwtPayload {
    exp: number;
    username: string;
}

const PublicRoute = ({ children }: { children: React.ReactElement }) => {
    const token = Cookies.get("token");

    if (token) {
        try {
            const decoded = jwtDecode<JwtPayload>(token);
            const now = Date.now() / 1000;

            if (decoded.exp > now) {
                return <Navigate to="/dashboard" />;
            }
        } catch (e) {
            // invalid token — fall through to show children
        }
    }

    return children;
};

export default PublicRoute;
