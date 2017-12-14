package net.heyzeer0.mgh.mixins.ic2;

import ic2.api.item.ITerraformingBP;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 06/08/2017.
 */

@Pseudo
@Mixin(targets = "ic2/core/block/machine/tileentity/TileEntityTerra", remap = false)
public abstract class MixinTileEntityTerra implements IForgeTileEntity {

    @Redirect(method = "updateEntityServer", at = @At(value = "INVOKE", target = "Lic2/api/item/ITerraformingBP;terraform(Lnet/minecraft/world/World;III)Z"))
    public boolean redirectTerraform(ITerraformingBP instance, World world, int x, int z, int y) {
        if (MixinManager.canBuild(x, y, z, world, getFakePlayer())) {
            return instance.terraform(world, x, z, y);
        }
        return false;
    }

}
