package net.heyzeer0.mgh.mixins.forge;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.registry.GameData;
import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.bukkit.IBukkitEntity;
import net.heyzeer0.mgh.api.bukkit.IBukkitTileEntity;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.bukkit.entity.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

/**
 * Created by Frani on 15/10/2017.
 */
@Mixin(TileEntity.class)
public abstract class MixinTileEntity implements IForgeTileEntity, IBukkitTileEntity {

    @Shadow protected World worldObj;
    @Shadow public abstract Block getBlockType();
    @Shadow public abstract void writeToNBT(NBTTagCompound p_145841_1_);
    @Shadow public int xCoord;
    @Shadow public int yCoord;
    @Shadow public int zCoord;
    @Shadow public abstract void invalidate();
    @Shadow public abstract World getWorldObj();

    private String tileOwner;
    private String tileUuid;
    private EntityPlayer realFakePlayer;

    @Override
    public String getOwner() {
        return this.tileOwner;
    }

    @Override
    public String getUUID() {
        return this.tileUuid;
    }

    @Override
    public void setOwner(String owner) {
        this.tileOwner = owner;
    }

    @Override
    public void setUUID(String uuid) {
        this.tileUuid = uuid;
    }

    @Override
    public void setPlayer(EntityPlayer player) {
        this.setOwner(player.getCommandSenderName());
        this.setUUID(player.getUniqueID().toString());
    }

    @Override
    public boolean hasTrackedPlayer() {
        return this.tileOwner != null && this.tileUuid != null;
    }

    @Override
    public boolean hasOwner() {
        return hasTrackedPlayer();
    }

    @Override
    public void setOwner(Player player) {
        this.tileOwner = player.getPlayerListName();
        this.tileUuid = player.getUniqueId().toString();
    }

    @Override
    public Player getBukkitOwner() {
        return (Player) ((IBukkitEntity) getFakePlayer()).getCraftEntity();
    }

    @Override
    public EntityPlayer getFakePlayer() {
        if (this.hasTrackedPlayer() && MagiHandlers.getPlayer(this.tileOwner) != null) {
            realFakePlayer = MagiHandlers.getPlayer(this.tileOwner);
            return realFakePlayer;
        }
        if (realFakePlayer == null) {
            if (this.hasTrackedPlayer()) {
                if (MagiHandlers.getPlayer(this.tileOwner) != null) {
                    realFakePlayer = MagiHandlers.getPlayer(this.tileOwner);
                } else {
                    try {
                        realFakePlayer = FakePlayerFactory.get((WorldServer) this.worldObj, new GameProfile(UUID.fromString(getUUID()), getOwner()));
                    } catch (IllegalArgumentException e) {
                        realFakePlayer = FakePlayerFactory.getMinecraft((WorldServer) this.worldObj);
                    }
                }
            } else {
                realFakePlayer = FakePlayerFactory.getMinecraft((WorldServer) this.worldObj);
            }
        }
        return realFakePlayer;
    }

    @Override
    public EntityPlayer getFakePlayerReplacingBlock() {
        if (!this.hasTrackedPlayer()) {
            ItemStack item = new ItemStack(this.getBlockType(), 1, 0);
            NBTTagCompound nbt = new NBTTagCompound();
            this.writeToNBT(nbt);
            item.setTagCompound(nbt);
            this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, Blocks.air);
            this.invalidate();
            this.getWorldObj().setBlock(this.xCoord, this.yCoord, this.zCoord, GameData.getBlockRegistry().getObject("ExtraUtilities:chestMini"));
            ((IInventory)this.getWorldObj().getTileEntity(this.xCoord, this.yCoord, this.zCoord)).setInventorySlotContents(0, item);
        }
        return getFakePlayer();
    }

    @Inject(method = "readFromNBT", at = @At("HEAD"))
    private void injectReadFromNBT(NBTTagCompound nbttagcompound, CallbackInfo ci) {
        this.tileOwner = nbttagcompound.getString("MHData.Owner");
        this.tileUuid = nbttagcompound.getString("MHData.UUID");
    }

    @Inject(method = "writeToNBT", at = @At("HEAD"))
    private void injectWriteToNBT(NBTTagCompound nbttagcompound, CallbackInfo ci) {
        if(this.tileOwner != null && this.tileUuid != null) {
            nbttagcompound.setString("MHData.Owner", this.tileOwner);
            nbttagcompound.setString("MHData.UUID", this.tileUuid);
        }
    }
}
