package net.heyzeer0.mgh;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by HeyZeer0 on 08/03/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class MagiHandlers extends DummyModContainer {

    public static MagiHandlers instance;
    private PhaseStack stack;
    public List<Runnable> tasks;
    
    public MagiHandlers() {
        super(new ModMetadata());
        ModMetadata metadata = getMetadata();
        metadata.authorList.add("HeyZeer0");
        metadata.name = "MagiHandlers";
        metadata.modId = "MagiHandlers";
        metadata.version = "1.0";
        this.tasks = new CopyOnWriteArrayList<>();
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent e) {
        LogManager.getLogger().warn(" ");
        LogManager.getLogger().warn("[MagiHandlers] ASM modules injected:");
        String x = "- Forge, ";
        for(String patches : MagiCore.loader.getLoadedPatches()) {
            x = x + patches + ", ";
        }
        x = x.substring(0, x.length() - 2) + ".";
        LogManager.getLogger().warn(x);
        LogManager.getLogger().warn(" ");

        MinecraftForge.EVENT_BUS.register(new EventCore());
        instance = this;
        this.stack = new PhaseStack();
    }

    public static EntityPlayer getPlayer(String name) {
        return MinecraftServer.getServer().getConfigurationManager().func_152612_a(name);
    }

    public static PhaseStack getStack() {
        return instance.stack;
    }

    public static void scheduleTileCheck(EntityPlayer player, World world, int x, int y, int z) {
        instance.tasks.add(new Runnable() {
            @Override
            public void run() {
                ITileEntityOwnable tile = (ITileEntityOwnable) world.getTileEntity(x, y, z);
                if (tile != null) tile.setPlayer(player);
            }
        });
    }

    public static void log(String content) {
        LogManager.getLogger().warn(" ");
        LogManager.getLogger().warn("[MagiHandlers] " + content);
    }

}
