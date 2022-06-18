package uz.pdp.class_manager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.class_manager.service.AuthService;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization!= null && authorization.startsWith("Bearer")){

            String token = authorization.substring(7);

            // validate : // true  false
            boolean validateToken = jwtProvider.validateToken(token);

            if (validateToken) {
                String userName = jwtProvider.getUsernameFromToken(token);

                if (userName!=null){
                    UserDetails userDetails = authService.loadUserByUsername(userName);

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
                }
            }
        }


        filterChain.doFilter(request,response); // checking
    }
}