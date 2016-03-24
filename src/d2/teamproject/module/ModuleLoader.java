package d2.teamproject.module;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import javafx.scene.image.Image;
import org.xeustechnologies.jcl.JarClassLoader;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipInputStream;

import static d2.teamproject.PARTH.LOG;

/**
 * @author Joseph Groocock
 */
public class ModuleLoader {
    private static final String MODULE_PATH = "res/module/";
    private static ModuleLoader instance = new ModuleLoader();

    private List<BaseController> modules;
    private boolean isLoaded = false;

    public static ModuleLoader getInstance() {
        return instance;
    }

    private ModuleLoader() {
        modules = new ArrayList<>();
    }

    /**
     * loads all available modules and calls back after each is loaded
     * @param callback callback for loaded visualisations
     */
    public void loadAllModules(ModuleLoadState callback) {
        new Thread(() -> {
            // List all directories which have a 'visualisations.json' file
            File moduleDir = new File(MODULE_PATH);

            // TODO: Change to extract all resources from JAR
            // If no modules, extract the default ones from the JAR
            if (!moduleDir.exists() || moduleDir.list() == null) {
                try {
                    moduleDir.mkdirs();

                    File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                    if (!jarFile.isFile())
                        return;

                    JarFile jar = new JarFile(jarFile);
                    Enumeration<JarEntry> modules = jar.entries();

                    while (modules.hasMoreElements()) {
                        JarEntry entry = modules.nextElement();
                        String name = entry.getName();

                        // Ensure the file is a module
                        if (!name.matches(MODULE_PATH + "(.*)\\.vism"))
                            continue;

                        // Write the resource to file
                        File f = new File(entry.getName());
                        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
                        Files.copy(is, f.toPath());
                    }
                    jar.close();
                } catch (Exception e) {
                    LOG.exception(e);
                }
            }
            File[] files = moduleDir.listFiles((f, n) -> n.endsWith(".vism"));

            for (int i = 0; i < files.length; i++) {
                try {
                    BaseController module = loadModule(files[i]); // could throw exception if load fails
                    modules.add(module);
                    if (callback != null)
                        callback.onLoadProgress(module, i, files.length);
                } catch (LoadException e) {
                    LOG.severe("Error loading visualisation %s", files[i].getName());
                    LOG.exception(e);
                } catch (Exception e) {
                    LOG.exception(e);
                    LOG.exit(1, true);
                }
            }
            isLoaded = true;
            synchronized (this) {
                this.notifyAll();
            }
        }).start();
    }

    private BaseController loadModule(File moduleFile) throws LoadException {
        try {
            JarClassLoader loader = new JarClassLoader();
            loader.add(new FileInputStream(moduleFile));

            InputStream infoStream = loader.getResourceAsStream("module.json");
            InputStreamReader isr = new InputStreamReader(infoStream);
            JsonObject info = Json.parse(isr).asObject();
            JsonObject load = info.get("load").asObject();
            String packageName = load.get("package").asString() + ".";

            Class mainCls = loader.loadClass(packageName + load.get("main").asString());
            JsonController module = (JsonController) mainCls.newInstance();

            try {
                JsonValue res = info.get("res");
                if (res != null && res.asObject().size() > 0)
                    module.loadResources(loadResources(res.asObject(), loader));
            } catch (Exception e) {
                LOG.warning("Error loading resources for %s", load.get("main").asString());
                LOG.exception(e);
            }

            Image banner = new Image(loader.getResourceAsStream("res/" + info.get("banner").asString()));
            module.init(info, banner);

            infoStream.close();
            isr.close();

            return module;
        } catch (Exception e) {
            throw new LoadException(e);
        }
    }

    private Map<String, Object> loadResources(JsonObject resJson, JarClassLoader loader) {
        Map<String, Object> resources = new LinkedHashMap<>(resJson.size());

        String rName = null;
        for (JsonObject.Member res : resJson) {
            try {
                rName = res.getName();
                JsonObject resObj = res.getValue().asObject();
                String fname = resObj.get("file").asString();

                InputStream is = loader.getResourceAsStream("res/" + fname);
                Object obj = loadResourceFromStream(is, resObj.get("type").asString());
                resources.put(rName, obj);
            } catch (Exception e) { // Catch then continue loading resources
                LOG.warning(" > Error loading resource \"%s\"", String.valueOf(rName));
                LOG.exception(e);
            }
        }
        return resources;
    }

    private Object loadResourceFromStream(InputStream is, String type) throws IOException {
        InputStreamReader isr = new InputStreamReader(is);

        switch (type.toLowerCase()) {
            case "jsonobject":
                return Json.parse(isr).asObject();
            case "jsonarray":
                return Json.parse(isr).asArray();
            case "image":
                return new Image(is);
            case "zip":
                return new ZipInputStream(is);
            case "text":
            case "string":
            case "plain":
                return new Scanner(is, "UTF-8").useDelimiter("\\A").next();
            default:
                return isr;
        }
    }

    public List<BaseController> getLoadedModules() {
        return modules;
    }

    public synchronized void onLoaded(Consumer<List<BaseController>> callback) {
        try {
            while (!isLoaded)
                this.wait();
        } catch (InterruptedException e) {
            LOG.exception(e);
            return;
        }
        callback.accept(modules);
    }

    public static class LoadException extends Exception {
        public LoadException(Throwable cause) {
            super(cause);
        }
    }
}
