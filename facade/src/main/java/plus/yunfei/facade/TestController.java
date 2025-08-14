package plus.yunfei.facade;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import plus.yunfei.MyPlugin;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author houyunfei
 */
@RestController
public class TestController {

    private MyPlugin myPlugin;

    @GetMapping("/time")
    public String time() {
        if (myPlugin != null) {
            myPlugin.beforeGetTime();
        }
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @GetMapping("/load/{path}")
    public String loadPlugin(@PathVariable("path") String path) {
        File file = new File(path);
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toPath().toUri().toURL()});
             InputStream stream = classLoader.getResourceAsStream("cxk.txt  ")
        ) {
            String myPluginName = new String(stream.readAllBytes());
            Class<?> aClass = classLoader.loadClass(myPluginName.trim());
            String name = aClass.getName();
            Constructor<?> constructor = aClass.getConstructor();
            myPlugin = (MyPlugin) constructor.newInstance();
            return "加载成功，插件：" + name;
        } catch (Exception e) {
            return "加载失败";
        }
    }

}
