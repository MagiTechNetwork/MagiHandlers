package net.heyzeer0.mgh.mixins.thaumcraft;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.heyzeer0.mgh.hacks.thaumcraft.IFocusChangePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.network.misc.PacketFocusChangeToServer;

/**
 * Created by Frani on 19/01/2018.
 */
@Pseudo
@Mixin(value = PacketFocusChangeToServer.class, remap = false)
public abstract class MixinPacketFocusChangeToServer implements IFocusChangePacket {

    public String name;
    @Shadow
    private int dim;
    @Shadow
    private int playerid;
    @Shadow
    private String focus;

    @Inject(method = "fromBytes", at = @At("RETURN"))
    private void onFromBytes(ByteBuf bufer, CallbackInfo ci) {
        this.name = ByteBufUtils.readUTF8String(bufer);
    }

    @Overwrite
    public IMessage onMessage(PacketFocusChangeToServer message, MessageContext ctx) {
        World world = DimensionManager.getWorld(((IFocusChangePacket) message).getDim());
        if (world != null && (ctx.getServerHandler().playerEntity == null || ctx.getServerHandler().playerEntity.getCommandSenderName().equals(((IFocusChangePacket) message).getName()))) {
            EntityPlayer player = world.getPlayerEntityByName(((IFocusChangePacket) message).getName());
            if (player != null && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWandCasting && !((ItemWandCasting) player.getHeldItem().getItem()).isSceptre(player.getHeldItem())) {
                WandManager.changeFocus(player.getHeldItem(), world, player, ((IFocusChangePacket) message).getFocus());
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public String getFocus() {
        return focus;
    }

    @Override
    public int getPlayerId() {
        return playerid;
    }

    @Override
    public int getDim() {
        return dim;
    }

    @Override
    public String getName() {
        return name;
    }

}
