package net.heyzeer0.mgh.mixins.mekanism;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


/**
 * Created by Frani on 13/06/2017.
 */

@Pseudo
@Mixin(targets = "mekanism/common/chunkloading/ChunkManager", remap = false)
public abstract class MixinChunkManager implements ForgeChunkManager.LoadingCallback {

    @Inject(method = "ticketsLoaded", at = @At("HEAD"), cancellable = true)
    private void injectTicketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world, CallbackInfo ci) {
        ci.cancel();
    }

}
