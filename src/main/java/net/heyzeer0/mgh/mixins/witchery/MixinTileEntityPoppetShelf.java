package net.heyzeer0.mgh.mixins.witchery;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by HeyZeer0 on 13/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "com/emoniph/witchery/blocks/BlockPoppetShelf$TileEntityPoppetShelf")
public abstract class MixinTileEntityPoppetShelf {

    @Inject(method = "forceChunkLoading", at = @At("HEAD"), cancellable = true)
    private void injectChunkloading(ForgeChunkManager.Ticket ticket, CallbackInfo ci) {
        ci.cancel();
    }

    @Redirect(method = "initiate", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeChunkManager;requestTicket(Ljava/lang/Object;Lnet/minecraft/world/World;Lnet/minecraftforge/common/ForgeChunkManager$Type;)Lnet/minecraftforge/common/ForgeChunkManager$Ticket;"))
    private ForgeChunkManager.Ticket request(Object mod, World world, ForgeChunkManager.Type type) {
        return null;
    }

}