package ua.visicom.service.dpf.web.filters;

import com.google.common.base.Optional;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author kosogon
 */
@WebFilter(
        urlPatterns = {"/*"},
        asyncSupported = true
)
public class Parameter implements Filter {

    String defaultWidth = "1000";
    String defaultHeight = "600";
    String defaultSettlement = "kiev";
    String defaultProvider = "test";

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        request.setCharacterEncoding("UTF-8");

        Map<String, String> param = new HashMap<String, String>() {
            {
                put("w", Optional.fromNullable(request.getParameter("w")).or(defaultWidth));
                put("h", Optional.fromNullable(request.getParameter("h")).or(defaultHeight));
                put("s", Optional.fromNullable(request.getParameter("s")).or(defaultSettlement));
                put("p", Optional.fromNullable(request.getParameter("p")).or(defaultProvider));
            }

        };
        request.setAttribute("param", param);
        
        chain.doFilter(req, resp);

    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig fc) throws ServletException {

    }

}
