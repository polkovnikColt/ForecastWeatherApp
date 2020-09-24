package ua.visicom.service.dpf.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kosogon
 */
@WebServlet(
        name = "Index",
        urlPatterns = {"/index"},
        loadOnStartup = 1
)
public class Index extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/index.html").forward(request, response);

    }


}
