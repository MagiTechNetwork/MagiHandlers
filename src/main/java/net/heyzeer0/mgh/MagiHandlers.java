package net.heyzeer0.mgh;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;

/**
 * Created by HeyZeer0 on 08/03/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class MagiHandlers extends DummyModContainer {
    
    public MagiHandlers()
    {
        super(new ModMetadata());
        ModMetadata metadata = getMetadata();
        metadata.authorList.add("HeyZeer0");
        metadata.name = "MagiHandlers";
        metadata.modId = "MagiHandlers";
        metadata.version = "1.0";

        MinecraftForge.EVENT_BUS.register(new EventCore());
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event){
        LogManager.getLogger().warn(" ");
        LogManager.getLogger().warn("Class transformes aplicados nos mods: " + StringUtils.join(MagiCore.loader.getLoadedPatches(), ", "));
        LogManager.getLogger().warn(" ");
    }

}
