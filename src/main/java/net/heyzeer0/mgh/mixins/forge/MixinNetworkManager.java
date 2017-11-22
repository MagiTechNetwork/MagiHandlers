package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 22/11/2017.
 */
@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {

    @Redirect(method = "processReceivedPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V"))
    private void onProcessPacket(Packet packet, INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer) {
            EntityPlayer packetPlayer = ((NetHandlerPlayServer) handler).playerEntity;
            if (!MagiHandlers.getStack().ignorePhase) {
                MagiHandlers.getStack().push(packetPlayer);
                packet.processPacket(handler);
                MagiHandlers.getStack().remove(packetPlayer);
            } else {
                packet.processPacket(handler);
            }
        }
    }

}
