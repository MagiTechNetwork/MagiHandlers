package net.heyzeer0.mgh.mixins.cofhcore;

import cofh.core.entity.CoFHFakePlayer;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Created by Frani on 17/06/2017.
 */

@Pseudo
@Mixin(targets = "cofh/core/entity/CoFHFakePlayer", remap = false)
public abstract class MixinCoFHFakePlayer {

    @Overwrite
    public static boolean isBlockBreakable(CoFHFakePlayer myFakePlayer, World worldObj, int x, int y, int z) {

        Block block = worldObj.getBlock(x, y, z);

        if (block.isAir(worldObj, x, y, z)) {
            return false;
        }
        if (myFakePlayer == null) {
            return block.getBlockHardness(worldObj, x, y, z) > -1;
        } else {
            BlockEvent.BreakEvent event = MixinManager.generateBlockEvent(x, y, z, worldObj, myFakePlayer);
            MinecraftForge.EVENT_BUS.post(event);
            if(!event.isCanceled()) {
                return block.getPlayerRelativeBlockHardness(myFakePlayer, worldObj, x, y, z) > -1;
            }
            return false;
        }
    }

}
