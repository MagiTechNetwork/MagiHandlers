package net.heyzeer0.mgh.loader;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import cpw.mods.fml.relauncher.CoreModManager;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Thomas Vanmellaerts
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class PathLoader {

    private final static Gson gson = new Gson();
    private final static File modFolder = new File("mods");
    private List<String> loadedPatches = new ArrayList<>();

    public void loadPatches() throws Exception{
        Patch[] patches = gson.fromJson(Resources.toString(Resources.getResource("mixin-patches.json"), Charset.forName("UTF-8")), Patch[].class);
        for (Patch patch : patches) {
            File modfile = new File(modFolder, patch.getFile());
            if (modfile.exists()){
                loadModJar(modfile);
                Mixins.addConfiguration(patch.getMixin());
                loadedPatches.add(patch.getName());
            }
        }
    }

    private void loadModJar(File jar) throws Exception{
        ((LaunchClassLoader) this.getClass().getClassLoader()).addURL(jar.toURI().toURL());
        CoreModManager.getReparseableCoremods().add(jar.getName());
    }

    public List<String> getLoadedPatches() {
        return loadedPatches;
    }

}
