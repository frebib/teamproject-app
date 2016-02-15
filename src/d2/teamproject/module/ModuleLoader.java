package d2.teamproject.module;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import javafx.scene.image.Image;
import org.xeustechnologies.jcl.JarClassLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleLoader {
    private static final String MODULE_PATH = "res/module/";
    private static ModuleLoader instance = new ModuleLoader();

    private List<BaseController> modules;
    private Thread loaderThread;
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
        loaderThread = new Thread(() -> {
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
                } catch (Exception e) {
                    System.out.printf("Error loading visualisation %s\n", files.get(i).getName());
                    e.printStackTrace();
                    System.out.println();
                }
            }
            isLoaded = true;
            synchronized (this) {
                this.notifyAll();
            }
        });
        loaderThread.start();
    }

    private BaseController loadModule(File moduleFile) throws Exception {
        JarClassLoader loader = new JarClassLoader();
        loader.add(new FileInputStream(moduleFile));

        InputStream infoStream = loader.getResourceAsStream("module.json");
        JsonObject info = Json.parse(new InputStreamReader(infoStream)).asObject();
        JsonObject load = info.get("load").asObject();
        String packageName = load.get("package").asString() + ".";

        Class mainCls = loader.loadClass(packageName + load.get("main").asString());//.asSubclass(JsonController.class);
        Class viewCls = loader.loadClass(packageName + load.get("view").asString());//.asSubclass(BaseView.class);
        JsonController module = (JsonController) mainCls.newInstance();
        BaseView view = (BaseView) viewCls.getConstructor(mainCls).newInstance(module);

        Image banner = new Image(loader.getResourceAsStream(info.get("banner").asString()));
        module.init(info, banner, view);

        return module;
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
}
