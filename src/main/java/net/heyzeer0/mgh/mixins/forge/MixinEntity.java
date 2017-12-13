package net.heyzeer0.mgh.mixins.forge;

import com.mojang.authlib.GameProfile;
import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.IEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

/**
 * Created by Frani on 02/11/2017.
 */
@Mixin(Entity.class)
public abstract class MixinEntity implements IEntity {

    @Shadow public World worldObj;
    private String uuid, username;
    private EntityPlayer owner;

    @Override
    public void setOwner(EntityPlayer player) {
        if (player.getUniqueID().toString().isEmpty()) {
            MagiHandlers.log("Tried to add a invalid player as entity owner, player: " + player.getCommandSenderName());
            return;
        }
        this.uuid = player.getUniqueID().toString();
        this.username = player.getCommandSenderName();
    }

    @Override
    public boolean hasOwner() {
        return hasTrackedPlayer();
    }

    public boolean hasTrackedPlayer() {
        return this.username != null && this.uuid != null && !this.username.isEmpty() && !this.uuid.isEmpty();
    }

    @Inject(method = "writeToNBT", at = @At("HEAD"))
    private void onWriteToNBT(NBTTagCompound nbt, CallbackInfo ci) {
        if (hasTrackedPlayer()) {
            nbt.setString("MHData.Owner", username);
            nbt.setString("MHData.UUID", uuid);
        }
    }

    @Inject(method = "readFromNBT", at = @At("HEAD"))
    private void onReadFromNBT(NBTTagCompound nbt, CallbackInfo ci) {
        this.username = nbt.getString("MHData.Owner");
        this.uuid = nbt.getString("MHData.UUID");
    }

    @Override
    public EntityPlayer getOwner() {
        if (this.hasTrackedPlayer() && MagiHandlers.getPlayer(this.username) != null) {
            owner = MagiHandlers.getPlayer(this.username);
            return owner;
        }
        if (owner == null) {
            if (this.hasTrackedPlayer()) {
                try {
                    owner = FakePlayerFactory.get((WorldServer) this.worldObj, new GameProfile(UUID.fromString(uuid), username));
                } catch (IllegalArgumentException e) {
                    LogManager.getLogger().error("[EntityTracker] An entity had an invalid owner UUID: " + uuid + ", skipping.");
                    owner = FakePlayerFactory.getMinecraft((WorldServer) this.worldObj);
                }
            } else {
                owner = FakePlayerFactory.getMinecraft((WorldServer) this.worldObj);
            }
        }
        return owner;
    }

}
