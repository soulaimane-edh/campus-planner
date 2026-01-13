package com.example.campusplanner.config;

import com.example.campusplanner.model.Role;
import com.example.campusplanner.model.UserAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Set;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/login",
            "/logout",
            "/error"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (isPublicPath(path)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        UserAccount currentUser = session == null ? null : (UserAccount) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("/login");
            return false;
        }

        if (!isRoleAllowed(currentUser.getRole(), path)) {
            response.sendRedirect("/");
            return false;
        }

        return true;
    }

    private boolean isPublicPath(String path) {
        if (path == null) {
            return true;
        }
        if (path.startsWith("/css/") || path.startsWith("/h2-console")) {
            return true;
        }
        return PUBLIC_PATHS.contains(path);
    }

    private boolean isRoleAllowed(Role role, String path) {
        if (role == Role.ADMIN) {
            return true;
        }
        if (path.startsWith("/students") || path.startsWith("/teachers") || path.startsWith("/groups")) {
            return false;
        }
        if (path.startsWith("/modules") && !path.startsWith("/modules/my")) {
            return false;
        }
        if (path.startsWith("/sessions") && !path.startsWith("/sessions/my")) {
            return false;
        }
        if (path.startsWith("/evaluations") && !path.startsWith("/evaluations/my")) {
            return role == Role.TEACHER && !path.startsWith("/evaluations/my");
        }
        if (path.startsWith("/evaluations/my")) {
            return role == Role.STUDENT;
        }
        if (path.startsWith("/modules/my") || path.startsWith("/sessions/my")) {
            return role == Role.TEACHER || role == Role.STUDENT;
        }
        return true;
    }
}
