package net.heyzeer0.mgh.mixins.mekanism;

import com.mojang.authlib.GameProfile;
import mekanism.common.CommonProxy;
import mekanism.common.Mekanism;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * Created by Frani on 13/08/2017.
 */

@Pseudo
@Mixin(targets = "mekanism/common/tile/TileEntityDigitalMiner", remap = false)
public abstract class MixinTileEntityDigitalMiner extends TileEntity implements ITileEntityOwnable {

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

    @Redirect(method = "setReplace", at = @At(value = "INVOKE", target = "Lmekanism/common/CommonProxy;getDummyPlayer(Lnet/minecraft/world/WorldServer;DDD)Ljava/lang/ref/WeakReference;"))
    public WeakReference<EntityPlayer> redirectFakePlayer(CommonProxy proxy, WorldServer world, double x, double y, double z) {
        EntityPlayer player = this.getOwner() == null ? null : MinecraftServer.getServer().getConfigurationManager().func_152612_a(this.getOwner());
        return new WeakReference<EntityPlayer>(player == null ? getFakePlayer() : player);
    }

}
