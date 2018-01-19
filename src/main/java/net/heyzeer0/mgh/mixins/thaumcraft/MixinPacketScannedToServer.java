package net.heyzeer0.mgh.mixins.thaumcraft;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.heyzeer0.mgh.hacks.thaumcraft.IPacketScanned;
import net.minecraft.entity.Entity;
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
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.network.playerdata.PacketScannedToServer;
import thaumcraft.common.lib.research.ScanManager;

/**
 * Created by Frani on 19/01/2018.
 */
@Pseudo
@Mixin(value = PacketScannedToServer.class, remap = false)
public abstract class MixinPacketScannedToServer implements IPacketScanned {
    private String name;
    private String entityUuid;

    @Shadow
    private int dim;

    @Shadow
    private byte type;

    @Shadow
    private int id;

    @Shadow
    private int md;

    @Shadow
    private String phenomena;

    @Shadow
    private String prefix;


    @Shadow
    private int entityid;

    @Inject(method = "fromBytes", at = @At("RETURN"))
    private void onFromBytes(ByteBuf buf, CallbackInfo ci) {
        this.name = ByteBufUtils.readUTF8String(buf);
        this.entityUuid = ByteBufUtils.readUTF8String(buf);
    }

    @Overwrite
    public IMessage onMessage(PacketScannedToServer message, MessageContext ctx) {
        IPacketScanned iMsg = (IPacketScanned) message;
        World world = DimensionManager.getWorld(iMsg.getDim());
        if (world == null) {
            return null;
        } else {
            EntityPlayer player = world.getPlayerEntityByName(iMsg.getPlayerName());
            Entity e = null;
            if (iMsg.getEntityId() != 0) {
                e = (Entity) world.loadedEntityList.stream().filter(en -> ((Entity) en).getUniqueID().toString().equals(iMsg.getEntityUuid())).findFirst().orElse(null);
            }
            if (player != null) {
                ScanManager.completeScan(player, new ScanResult(iMsg.getType(), iMsg.getId(), iMsg.getMd(), e, iMsg.getPhenomena()), iMsg.getPrefix());
            }
            return null;
        }
    }


    @Override
    public int getDim() {
        return dim;
    }

    @Override
    public byte getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getMd() {
        return md;
    }

    @Override
    public String getPhenomena() {
        return phenomena;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getPlayerName() {
        return name;
    }

    @Override
    public String getEntityUuid() {
        return entityUuid;
    }

    @Override
    public int getEntityId() {
        return entityid;
    }

}
