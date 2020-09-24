package ua.visicom.service.dpf.web;

import com.google.common.cache.*;
import com.google.common.io.ByteStreams;
import freemarker.template.*;
;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import ua.visicom.service.dpf.domain.Providers;
import ua.visicom.service.dpf.domain.TempProviders;

/**
 *
 * @author kosogon
 */


@WebServlet(
        name = "Image",
        urlPatterns = {"/image"}
)
public class Image extends HttpServlet {

    Cache<String, byte[]> cache;

    @Override
    public void init() throws ServletException {

        cache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .build();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = getServletContext();

        Map model = (Map) request.getAttribute("param");
        model.put("s", model.get("s").toString());
        String key = request.getQueryString();
        //System.out.println("CITY: " + model.get("s").toString());
        TempProviders.getDataFromHTML(model.get("s").toString());

        int keyHash = 7;
        for (int i = 0; i < key.length(); i++) {
            keyHash = keyHash * 31 + key.charAt(i);
        }
        String screenshotFileName = String.valueOf(keyHash) + ".png";

        byte[] image = cache.getIfPresent(key);
        if (image == null) {

            //TODO 
            //Перенести выше в инит сервлета или вообще приложения
            String chromePath
                    = context.getRealPath("/").substring(
                            0, context.getRealPath("/").indexOf("visicom-tools")
                            + "visicom-tools".length()) + "/bin/chrome";

            String templateFile = context.getRealPath("/WEB-INF/tpl/image_" + model.get("p") + ".html");
            String templateRenderedFile = context.getRealPath("/WEB-INF/tpl/image_" + model.get("p") + String.valueOf(keyHash) + ".html");

            model.put("data", TempProviders.getDataFromHTML(model.get("s").toString()));

            try {
                //1. render image.html
                _writeTemplate(model, templateFile, templateRenderedFile);

                //2. create & run process
                Process p = new ProcessBuilder(chromePath + "/chrome.exe",
                        "--headless",
                        "--disable-gpu",
                        "--window-size=" + model.get("w") + "," + model.get("h"),
                        "--screenshot=" + screenshotFileName,
                        templateRenderedFile).start();
                p.waitFor();
                p.destroy();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            // 3. read rendered file
            File screenshotFile = new File(chromePath + "/83.0.4103.106/" + screenshotFileName);
            image = ByteStreams.toByteArray(new FileInputStream(screenshotFile));
            screenshotFile.deleteOnExit();

            //4. put file in cache
            cache.put(key, image);

        }

        response.setContentType("image/png");
        response.setContentLength(image.length);
        response.getOutputStream().write(image);
    }

    private static void _writeTemplate(Map model, String templateFile, String templateRenderedFile) throws Exception {

        Writer file = new FileWriter(new File(templateRenderedFile));

        Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        config.setDirectoryForTemplateLoading(new File(templateFile).getParentFile());
        Template template = config.getTemplate(new File(templateFile).getName());
        template.process(model, file);
        file.flush();
        file.close();
    }

}
