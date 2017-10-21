package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.minecraft.block.BlockEventData;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 20/10/2017.
 */
@Mixin(WorldServer.class)
public abstract class MixinWorldServer extends World {

    public MixinWorldServer(ISaveHandler a, String b, WorldSettings c, WorldProvider d, Profiler e) {
        super(a, b, c, d, e);
    }

    @Inject(method = "func_147485_a", at = @At("HEAD"))
    private void onSendBlockEvents(BlockEventData data, CallbackInfoReturnable<Boolean> cir) {
        TileEntity te = this.getTileEntity(data.func_151340_a(), data.func_151342_b(), data.func_151341_c());
        if (te != null) {
            MagiHandlers.instance.phase.push(te);
        }
    }

    @Inject(method = "func_147488_Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendToAllNear(DDDDILnet/minecraft/network/Packet;)V", shift = At.Shift.AFTER))
    private void onSendToAllNear(CallbackInfo ci) {
        MagiHandlers.instance.phase.poll();
    }

}
