package net.heyzeer0.mgh.mixins.buildcraft;

import com.mojang.authlib.GameProfile;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

/**
 * Created by Frani on 12/08/2017.
 */

@Pseudo
@Mixin(targets = "buildcraft/factory/TileFloodGate", remap = false)
public abstract class MixinTileFloodGate extends TileEntity implements ITileEntityOwnable {

    private String player;
    private String uuid;
    private FakePlayer realFakePlayer;

    @Override
    public String getOwner() {
        return this.player;
    }

    @Override
    public String getUUID() {
        return this.uuid;
    }

    @Override
    public void setOwner(String owner) {
        this.player = owner;
    }

    @Override
    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    @Inject(method = "func_145839_a", at = @At("HEAD"))
    public void injectReadFromNBT(NBTTagCompound nbttagcompound, CallbackInfo ci) {
        this.player = nbttagcompound.getString("owner");
        this.uuid = nbttagcompound.getString("uuid");
    }

    @Inject(method = "func_145841_b", at = @At("HEAD"))
    public void injectWriteToNBT(NBTTagCompound nbttagcompound, CallbackInfo ci) {
        nbttagcompound.setString("owner", this.player);
        nbttagcompound.setString("uuid", this.uuid);
    }

    private FakePlayer getFakePlayer() {
        if (realFakePlayer == null) {
            if (this.getOwner() != null && this.getUUID() != null) {
                realFakePlayer = FakePlayerFactory.get((WorldServer) this.worldObj, new GameProfile(UUID.fromString(getUUID()), getOwner()));
            } else {
                realFakePlayer = FakePlayerFactory.getMinecraft((WorldServer)this.worldObj);
            }
        }
        return realFakePlayer;
    }

    @Inject(method = "canPlaceFluidAt", at = @At("HEAD"), cancellable = true)
    public void fireBreak(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        EntityPlayer player = this.getOwner() == null ? null : MinecraftServer.getServer().getConfigurationManager().func_152612_a(this.getOwner());
        BlockEvent.BreakEvent e = MixinManager.generateBlockEvent(x, y, z, this.worldObj, player == null ? getFakePlayer() : player);
        MinecraftForge.EVENT_BUS.post(e);
        if(e.isCanceled()) {
            cir.setReturnValue(false);
        }
    }

}
