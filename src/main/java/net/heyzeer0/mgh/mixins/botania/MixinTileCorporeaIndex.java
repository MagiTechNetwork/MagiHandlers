package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 04/07/2017.
 */

@Pseudo
@Mixin(targets = "vazkii/botania/common/block/tile/corporea/TileCorporeaIndex$InputHandler", remap = false)
public abstract class MixinTileCorporeaIndex {

    @Inject(method = "onChatMessage", at = @At(value = "INVOKE", target = "Lvazkii/botania/common/block/tile/corporea/TileCorporeaIndex;doCorporeaRequest(Ljava/lang/Object;ILvazkii/botania/api/corporea/ICorporeaSpark;)V"), cancellable = true)
    private void injectChatMessage(ServerChatEvent event, CallbackInfo ci) {
        BlockEvent.BreakEvent evt = MixinManager.generateBlockEvent((int)event.player.posX, (int)event.player.posY, (int)event.player.posZ, event.player.worldObj, event.player);
        MinecraftForge.EVENT_BUS.post(evt);
        if(evt.isCanceled()) ci.cancel();
    }

}
