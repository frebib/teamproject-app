package d2.teamproject.module;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import javafx.scene.image.Image;
import org.xeustechnologies.jcl.JarClassLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

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
     *
     * @param callback callback for loaded visualisations
     */
    public void loadAllModules(ModuleLoadState callback) {
        new Thread(() -> {
            // List all directories which have a 'visualisations.json' file
            File moduleDir = new File(MODULE_PATH);
            List<File> files = Arrays.stream(moduleDir.listFiles())
                    .filter(f -> f.getName().endsWith(".vism"))
                    .collect(Collectors.toList());

            for (int i = 0; i < files.size(); i++) {
                try {
                    BaseController module = loadModule(files.get(i)); // could throw exception if load fails
                    modules.add(module);
                    if (callback != null)
                        callback.onLoadProgress(module, i, files.size());
                } catch (LoadException e) {
                    System.out.printf("Error loading visualisation %s\n", files.get(i).getName());
                    e.printStackTrace();
                    System.out.println();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
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
            Class viewCls = loader.loadClass(packageName + load.get("view").asString());
            JsonController module = (JsonController) mainCls.newInstance();

            try {
                JsonObject res = info.get("res").asObject();
                module.loadResources(loadResources(res, loader));
            } catch (Exception e) {
                // Nothing to do here...
                // Probably no resources available
            }

            BaseView view = (BaseView) viewCls.getConstructor(mainCls).newInstance(module);
            Image banner = new Image(loader.getResourceAsStream(info.get("banner").asString()));
            module.init(info, banner, view);

            infoStream.close();
            isr.close();

            return module;
        } catch (Exception e) {
            throw new LoadException();
        }
    }
    private Map<String, Object> loadResources(JsonObject resJson, JarClassLoader loader) {
        Map<String, Object> resources = new LinkedHashMap<>(resJson.size());

        String rName = null;
        for (JsonObject.Member res : resJson) {
            try {
                Object obj;
                rName = res.getName();
                JsonObject resObj = res.getValue().asObject();
                String fname = resObj.get("file").asString();

                InputStream is = loader.getResourceAsStream(fname);
                InputStreamReader isr = new InputStreamReader(is);

                switch (resObj.get("type").asString().toLowerCase()) {
                    case "jsonobject":
                        obj = Json.parse(isr).asObject();
                        break;
                    case "jsonarray":
                        obj = Json.parse(isr).asArray();
                        break;
                    case "image":
                        obj = new Image(is);
                        break;
                    case "text":
                    case "string":
                    case "plain":
                        obj = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
                        break;
                    default:
                        obj = isr;
                }

                resources.put(rName, obj);
            } catch (Exception e) { // Catch then continue loading resources
                System.out.printf(" > Error loading resource \"%s\"\n", String.valueOf(rName));
            }
        }
        return resources;
    }

    public List<BaseController> getLoadedModules() {
        return modules;
    }

    public synchronized void onLoaded(ModulesLoaded callback) {
        try {
            while (!isLoaded)
                this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        callback.onLoaded(modules);
    }

    public static class LoadException extends Exception { }
}
