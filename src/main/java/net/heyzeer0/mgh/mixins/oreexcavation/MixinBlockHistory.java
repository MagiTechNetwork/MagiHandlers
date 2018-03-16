package net.heyzeer0.mgh.mixins.oreexcavation;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import oreexcavation.undo.BlockHistory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 18/09/2017.
 */

@Pseudo
@Mixin(value = BlockHistory.class, remap = false)
public abstract class MixinBlockHistory {

    @Redirect(method = "Loreexcavation/undo/BlockHistory;<init>(Lnet/minecraftforge/common/util/BlockSnapshot;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;writeToNBT(Lnet/minecraft/nbt/NBTTagCompound;)V"))
    private void onWriteToNBT(TileEntity tile, NBTTagCompound nbt) {
        if (!tile.getClass().getName().equals("codechicken.multipart.handler.MultipartSaveLoad$TileNBTContainer")) {
            tile.writeToNBT(nbt);
        }
    }

}
