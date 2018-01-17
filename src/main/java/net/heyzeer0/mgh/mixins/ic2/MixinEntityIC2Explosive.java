package net.heyzeer0.mgh.mixins.ic2;

import ic2.core.ExplosionIC2;
import ic2.core.block.EntityIC2Explosive;
import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.bukkit.IBukkitEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ExplosionEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

/**
 * Created by Frani on 13/12/2017.
 */
@Pseudo
@Mixin(value = EntityIC2Explosive.class, remap = false)
public abstract class MixinEntityIC2Explosive {

    @Redirect(method = "explode", at = @At(value = "INVOKE", target = "Lic2/core/ExplosionIC2;doExplosion()V"))
    private void onExplode(ExplosionIC2 explosion) {
        ExplosionEvent.Start event = new ExplosionEvent.Start(explosion.exploder.worldObj, explosion);
        if (MinecraftForge.EVENT_BUS.post(event)) return;
        try {
            Location loc = new Location((World) event.world.getClass().getMethod("getWorld").invoke(event.world), explosion.explosionX, explosion.explosionY, explosion.explosionZ);
            List<Block> blocks = MagiHandlers.getNearbyBlocks(loc, 5);
            int size = blocks.size();
            EntityExplodeEvent e = new EntityExplodeEvent(
                    ((IBukkitEntity) this).getCraftEntity(),
                    loc,
                    blocks,
                    explosion.explosionSize);
            Bukkit.getPluginManager().callEvent(e);
            if (e.isCancelled() || size != blocks.size()) return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        explosion.doExplosion();
    }

}
