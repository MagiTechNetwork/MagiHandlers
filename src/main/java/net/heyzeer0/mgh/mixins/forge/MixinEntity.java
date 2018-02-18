package net.heyzeer0.mgh.mixins.forge;

import com.mojang.authlib.GameProfile;
import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.bukkit.IBukkitEntity;
import net.heyzeer0.mgh.api.forge.IForgeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.Player;
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
public abstract class MixinEntity implements IForgeEntity, IBukkitEntity {

    @Shadow public World worldObj;

    @Shadow
    public abstract CraftEntity getBukkitEntity();
    private String uuid, username;
    private EntityPlayer realFakePlayer;

    // Forge
    @Override
    public void setMHOwner(EntityPlayer player) {
        if (player.getUniqueID().toString().isEmpty()) {
            MagiHandlers.log("Tried to add a invalid player as entity owner, player: " + player.getCommandSenderName());
            return;
        }
        this.uuid = player.getUniqueID().toString();
        this.username = player.getCommandSenderName();
    }

    @Override
    public EntityPlayer getMHOwner(boolean fake) {
        if (this.hasTrackedPlayer()) {
            if (realFakePlayer == null) {
                EntityPlayer f = FakePlayerFactory.get((WorldServer) this.worldObj, new GameProfile(UUID.fromString(this.uuid), this.username));
                if (fake) {
                    realFakePlayer = f;
                } else {
                    EntityPlayer p = MagiHandlers.getPlayer(this.username);
                    realFakePlayer = p != null ? p : f;
                }
                return realFakePlayer;
            } else {
                if (fake) {
                    if (realFakePlayer instanceof FakePlayer) {
                        return realFakePlayer;
                    } else {
                        realFakePlayer = null;
                        return this.getMHOwner(true);
                    }
                } else {
                    if (realFakePlayer instanceof FakePlayer) {
                        if (!realFakePlayer.getCommandSenderName().equals("[Minecraft]")) {
                            realFakePlayer = null;
                            realFakePlayer = this.getMHOwner(false);
                        }
                    }
                }
            }
        } else {
            realFakePlayer = FakePlayerFactory.getMinecraft((WorldServer) this.worldObj);
        }
        return realFakePlayer;

        /*
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
        return owner;*/
    }

    // Bukkit
    @Override
    public void setOwner(Player player) {
        this.uuid = player.getUniqueId().toString();
        this.username = player.getPlayerListName();
    }

    @Override
    public org.bukkit.entity.Entity getCraftEntity() {
        return this.getBukkitEntity();
    }

    @Override
    public Player getBukkitOwner() {
        return (Player) ((IBukkitEntity) getMHOwner()).getCraftEntity();
    }

    // Bukkit/Forge
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

}
