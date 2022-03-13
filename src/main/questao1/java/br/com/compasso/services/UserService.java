package br.com.compasso.services;

import br.com.compasso.Key;
import br.com.compasso.dao.UserDao;
import br.com.compasso.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class UserService {
    UserDao dao = new UserDao();

    public Response login(String username, String password) {
        try{
            User user = dao.readName(username);
            if(user != null && (user.getUsername().equals(username) && user.getPassword().equals(password)))
            {
                String jwtToken = Jwts.builder()
                        .setSubject(username)
                        .setIssuer("localhost:8080")
                        .setIssuedAt(new Date())
                        .setExpiration(Date.from(LocalDateTime.now().plusMinutes(15L)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()))
                        .signWith(Key.KEY.getPrivate(), SignatureAlgorithm.RS512)
                        .compact();

                NewCookie cookie1 = new NewCookie("user", user.getUserId().toString(), "/", "localhost", "user", 60*60, false, true);
                NewCookie cookie2 = new NewCookie("token", "Bearer " + jwtToken, "/", "localhost", "token", 60*60, false, true);
                return Response.seeOther(URI.create("http://localhost:8080/api/order/cart")).cookie(cookie1,cookie2).entity(jwtToken).build();
            }
            return Response.status(Response.Status.UNAUTHORIZED).entity("Usuario e/ou senha Invalidos!").build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    public Response register(User user) {
        User i = dao.readName(user.getUsername());

        if(i != null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Usuário já existe no sistema!").build();
        }

        dao.save(user);
        return Response.seeOther(URI.create("http://localhost:8080/login")).entity("Usuario cadastrado com sucesso!").build();
    }

    public Response listAllUsers() {
        List<User> listUsers = dao.readAll();
        return Response.status(Response.Status.OK).entity(listUsers).build();
    }
}
