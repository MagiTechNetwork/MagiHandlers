package net.heyzeer0.mgh.mixins.thermalexpansion;

import cofh.thermalexpansion.block.strongbox.TileStrongbox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

/**
 * Created by Frani on 16/03/2018.
 */
@Mixin(value = TileStrongbox.class, remap = false)
public abstract class MixinTileStrongbox {

    @Redirect(method = "getNumPlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;"))
    private List<EntityPlayer> redirectGetEntitiesWithinAABB(World world, Class entityClass, AxisAlignedBB bb) {
        return world.playerEntities;
    }

}
