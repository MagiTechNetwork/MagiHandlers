package net.heyzeer0.mgh.mixins.extrautilities;

import cofh.api.energy.EnergyStorage;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 13/06/2017.
 */

@Pseudo
@Mixin(targets = "com/rwtema/extrautils/tileentity/TileEntityEnderThermicLavaPump", remap = false)
public abstract class MixinTileEntityEnderThermicLavaPump {

    @Inject(method = "forceChunkLoading", at = @At("HEAD"), cancellable = true)
    private void injectChunkLoading(final ForgeChunkManager.Ticket ticket, CallbackInfo ci) {
        ci.cancel();
    }

    @Redirect(method = "func_145845_h", at = @At(value = "INVOKE", target = "Lcofh/api/energy/EnergyStorage;extractEnergy(IZ)I", ordinal = 0))
    private int redirectExtractEnergy1(EnergyStorage storage, int energy, boolean simulate) {
        return storage.extractEnergy(5000, true);
    }

    @Redirect(method = "func_145845_h", at = @At(value = "INVOKE", target = "Lcofh/api/energy/EnergyStorage;extractEnergy(IZ)I", ordinal = 1))
    private int redirectExtractEnergy2(EnergyStorage storage, int energy, boolean simulate) {
        return storage.extractEnergy(5000, false);
    }

    @Redirect(method = "func_145845_h", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeChunkManager;forceChunk(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;Lnet/minecraft/world/ChunkCoordIntPair;)V", ordinal = 0))
    private void onForceChunk(ForgeChunkManager.Ticket ticket, ChunkCoordIntPair chunk) {}

    @Redirect(method = "func_145845_h", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeChunkManager;forceChunk(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;Lnet/minecraft/world/ChunkCoordIntPair;)V", ordinal = 2))
    private void onForceChunk1(ForgeChunkManager.Ticket ticket, ChunkCoordIntPair chunk) {}

}
