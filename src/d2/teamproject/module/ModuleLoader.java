package d2.teamproject.module;

<<<<<<< Updated upstream
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import javafx.scene.image.Image;
import org.xeustechnologies.jcl.JarClassLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
=======
//import module.planets.PlanetModule;
//import module.tubesearch.TubeSearchModule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
>>>>>>> Stashed changes
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleLoader {
    private static final String MODULE_PATH = "res/module/";
    private static ModuleLoader instance = new ModuleLoader();

    private List<BaseModule> modules;
    private Thread loaderThread;
<<<<<<< Updated upstream
    private boolean isLoaded = false;
=======
>>>>>>> Stashed changes

    public static ModuleLoader getInstance() {
        return instance;
    }

    private ModuleLoader() {
        modules = new ArrayList<>();
    }

    /**
     * loads all available modules and calls back after each is loaded
     *
<<<<<<< Updated upstream
     * @param callback callback for loaded visualisations
     */
    public void loadAllModules(ModuleLoadState callback) {
        loaderThread = new Thread(() -> {
            // TODO: Abstract out visualisations loading through a ClassLoader...
            // (if we have time..)

            // List all directories which have a 'visualisations.json' file
            File moduleDir = new File(MODULE_PATH);
            File[] files = moduleDir.listFiles();
            List<File> moduleFiles = Arrays.stream(files)
                    .filter(f -> f.getName().endsWith(".vism"))
                    .collect(Collectors.toList());

            for (int i = 0; i < files.length; i++) {
                try {
                    BaseModule module = loadModule(files[i]); // could throw exception if load fails
                    modules.add(module);
                    if (callback != null)
                        callback.onLoadProgress(module, i, files.length);
                } catch (Exception e) {
                    System.out.printf("Error loading visualisations %s\n", files[i].getName());
                    e.printStackTrace();
                    System.out.println();
                    continue;
                }
            }
            isLoaded = true;
            synchronized (this) {
                this.notifyAll();
            }
=======
     * @param callback callback for loaded module
     */
    public void loadAllModules(ModuleLoadState callback) {
        loaderThread = new Thread(() -> {
            // TODO: Abstract out module loading through a ClassLoader...
            // (if we have time..)

            // List all directories which have a 'module.json' file
            File moduleDir = new File(MODULE_PATH);
            File[] files = moduleDir.listFiles();
            List<File> dirs = Arrays.stream(files)
                    .filter(f -> f.listFiles((d, fn) -> fn.equals("module.json")).length > 0)
                    .collect(Collectors.toList());

            for (int i = 0; i < dirs.size(); i++) {
                try {
                    File dir = dirs.get(i);
                    BaseModule module = loadModule(dir); // could throw exception if load fails
                    modules.add(module);
                    callback.onLoadProgress(module, i, dirs.size());
                } catch (Exception e) {
                    continue;
                }
            }

//            modules.add(new PlanetModule());
//            modules.add(new TubeSearchModule());
            if (callback == null)
                return;

            for (int i = 0; i < modules.size(); i++)
                callback.onLoadProgress(modules.get(i), i, modules.size());
>>>>>>> Stashed changes
        });
        loaderThread.start();
    }

<<<<<<< Updated upstream
    private BaseModule loadModule(File moduleFile) throws Exception {
        JarClassLoader loader = new JarClassLoader();
        loader.add(new FileInputStream(moduleFile));

        InputStream infoStream = loader.getResourceAsStream("module.json");
        JsonObject info = Json.parse(new InputStreamReader(infoStream)).asObject();
        JsonObject load = info.get("load").asObject();
        String packageName = load.get("package").asString() + ".";

        Class mainCls = loader.loadClass(packageName + load.get("main").asString()).asSubclass(JsonModule.class);
        Class viewCls = loader.loadClass(packageName + load.get("view").asString()).asSubclass(BaseView.class);
        JsonModule module = (JsonModule) mainCls.newInstance();
        BaseView view = (BaseView) viewCls.getConstructor(BaseModule.class).newInstance(module);

        Image banner = new Image(loader.getResourceAsStream(info.get("banner").asString()));
        module.init(info, banner, view);

        return module;
=======
    private BaseModule loadModule(File directory) throws Exception {
        File infoFile = directory.listFiles((dir, fn) -> fn.equals("module.json"))[0];
        JsonObject info = Json.parse(new FileReader(infoFile)).asObject();
        JsonObject load = info.get("load").asObject();
        String packageName = info.get("package").asString() + ".";
        URLClassLoader loader = new URLClassLoader();
        Class<?> moduleClass = loader.loadClass(info.get("name").asString());
        BaseModule module = (BaseModule) moduleClass.newInstance();


        return null;
>>>>>>> Stashed changes
    }

    public List<BaseModule> getLoadedModules() {
        return modules;
<<<<<<< Updated upstream
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
=======
>>>>>>> Stashed changes
    }
}
