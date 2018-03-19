package net.heyzeer0.mgh;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.heyzeer0.mgh.api.bukkit.BukkitAPI;
import net.heyzeer0.mgh.api.bukkit.BukkitStack;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by HeyZeer0 on 08/03/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class MagiHandlers extends DummyModContainer implements BukkitAPI.MagiHandlers {

    public static MagiHandlers instance;
    private PhaseStack stack;
    public List<Runnable> tasks;
    public static boolean isCauldron = false;
    private static List<String> fakePlayers = Lists.newArrayList(
            "[computercraft]",
            "[cofh]",
            "[buildcraft]",
            "[mekanism]",
            "[minefactoryreloaded]",
            "[computercraft]",
            "[forestry]",
            "[openmods]",
            "[extrautilities]",
            "[minecraft]",
            "[draconic-evolution]",
            "[eiokillera]",
            "[eiofarmer]",
            "[fakethaumcraftgolem]",
            "fakethaumcraftgolem",
            "[pr_fake]",
            "[tt]",
            "fakethaumcraft",
            "fakethaumcraftbore",
            "[thaumcrafttablet]"
    );

    private static List<String> longRangeProtected = Lists.newArrayList(
            "ItemIgniter",
            "ItemChiller"
    );
    
    public MagiHandlers() {
        super(new ModMetadata());
        ModMetadata metadata = getMetadata();
        metadata.authorList.add("HeyZeer0");
        metadata.name = "MagiHandlers";
        metadata.modId = "MagiHandlers";
        metadata.version = "1.0";
        this.tasks = new CopyOnWriteArrayList<>();
        try {
            Class.forName("thermos.Thermos");
            isCauldron = true;
        } catch (Exception e) {}
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    public static void scheduleTileCheck(String username, String uuid, World world, int x, int y, int z) {
        instance.tasks.add(() -> {
            IForgeTileEntity tile = (IForgeTileEntity) world.getTileEntity(x, y, z);
            if (tile != null) {
                tile.setMHOwner(username, uuid);
            }
        });
    }

    public static void schedule(Runnable r) {
        instance.tasks.add(r);
    }

    public static void track(String name, String uuid, World world, int x, int y, int z) {
        IForgeTileEntity te = (IForgeTileEntity) world.getTileEntity(x, y, z);
        if (te != null) {
            te.setMHOwner(name, uuid);
        } else {
            scheduleTileCheck(name, uuid, world, x, y, z);
        }
    }

    public static void closeScreen(EntityPlayer p) {
        ((EntityPlayerMP) p).playerNetServerHandler.sendPacket(new S2EPacketCloseWindow());
    }

    public static boolean isFakePlayer(String name) {
        return name.toLowerCase().contains("openmodsfakeplayer") || fakePlayers.contains(name.toLowerCase());
    }

    public static boolean isLongRangeBlocked(ItemStack o) {
        return longRangeProtected.contains(o.getItem().getClass().getSimpleName());
    }

    public static EntityPlayer getPlayer(String name) {
        return MinecraftServer.getServer().getConfigurationManager().func_152612_a(name);
    }

    public static PhaseStack getStack() {
        return instance.stack;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    @Override
    public void closeInventory(Player p) {
        closeScreen(getPlayer(p.getName()));
    }

    public static void log(String content) {
        LogManager.getLogger().warn(" ");
        LogManager.getLogger().warn("[MagiHandlers] " + content);
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
        ForgeStack.stack = this.stack;
        BukkitStack.stack = this.stack;
        BukkitAPI.handlers = this;
    }

    @Subscribe
    public void onInit(FMLPostInitializationEvent e) {
        if (Loader.isModLoaded("OpenComputers")) MinecraftForge.EVENT_BUS.register(new OpenComputersEventHandler());
    }

}
