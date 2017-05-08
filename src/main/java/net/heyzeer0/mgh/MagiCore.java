package net.heyzeer0.mgh;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.heyzeer0.mgh.loader.PathLoader;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.util.Map;

/**
 * Created by HeyZeer0 on 08/03/2017.
 * Copyright © HeyZeer0 - 2016
 */

@IFMLLoadingPlugin.Name("MagiHandlers")
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class MagiCore implements IFMLLoadingPlugin {

    public MagiCore() {
        MixinBootstrap.init();

        LogManager.getLogger().warn(" ");
        LogManager.getLogger().warn("[MagiHandlers] Trying to apply class transformers.");
        LogManager.getLogger().warn("    - If you got any errors contact the developer immediately.");
        LogManager.getLogger().warn(" ");

        try{
            new PathLoader().loadPatches();
        }catch (Exception e) {
            LogManager.getLogger().warn(" ");
            LogManager.getLogger().warn("[MagiHandlers] An error ocurred while trying to load class transformers.");
            LogManager.getLogger().warn(" ");
        }

    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
        };
    }

    @Override
    public String getModContainerClass() {
        return "net.heyzeer0.mgh.MagiHandlers";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}