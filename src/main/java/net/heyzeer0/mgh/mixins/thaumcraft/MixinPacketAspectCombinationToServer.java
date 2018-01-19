package net.heyzeer0.mgh.mixins.thaumcraft;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.lib.network.playerdata.PacketAspectCombinationToServer;

import java.lang.reflect.Field;

/**
 * Created by Frani on 19/01/2018.
 */
@Pseudo
@Mixin(value = PacketAspectCombinationToServer.class, remap = false)
public abstract class MixinPacketAspectCombinationToServer {

    private static Field playerIdField;
    private static Field playerNameField;
    private static Field worldField;
    private String playerName;

    @Inject(method = "fromBytes", at = @At("RETURN"))
    private void onFromBytes(ByteBuf buf, CallbackInfo ci) {
        this.playerName = ByteBufUtils.readUTF8String(buf);
    }

    @Inject(method = "onMessage", at = @At("HEAD"))
    private void injectOnMessage(PacketAspectCombinationToServer packet, MessageContext context) {
        try {
            if (playerNameField == null) {
                playerNameField = this.getClass().getDeclaredField("playerName");
                playerNameField.setAccessible(true);
            }
            if (worldField == null) {
                worldField = this.getClass().getDeclaredField("dim");
                worldField.setAccessible(true);
            }
            if (playerIdField == null) {
                playerIdField = PacketAspectCombinationToServer.class.getDeclaredField("playerid");
                playerIdField.setAccessible(true);
            }
            World w = DimensionManager.getWorld(worldField.getInt(packet));
            EntityPlayer p = w.getPlayerEntityByName((String) playerNameField.get(packet));
            if (p != null) playerIdField.set(packet, p.getEntityId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
