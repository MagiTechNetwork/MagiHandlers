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
    public String getMHOwner() {
        return this.tileOwner;
    }

    @Override
    public void setMHOwner(Player player) {
        this.tileOwner = player.getPlayerListName();
        this.tileUuid = player.getUniqueId().toString();
    }

    @Override
    public String getMHUuid() {
        return this.tileUuid;
    }

    @Override
    public void setMHUuid(String uuid) {
        this.tileUuid = uuid;
    }

    @Override
    public void setMHOwner(String owner) {
        this.tileOwner = owner;
    }

    @Override
    public boolean hasMHPlayer() {
        return this.tileOwner != null && this.tileUuid != null;
    }

    @Override
    public boolean hasMHOwner() {
        return hasMHPlayer();
    }

    @Override
    public Player getMHBukkitOwner() {
        return (Player) ((IBukkitEntity) getMHPlayer()).getCraftEntity();
    }

    @Override
    public EntityPlayer getMHPlayer() {
        if (this.hasMHPlayer() && MagiHandlers.getPlayer(this.tileOwner) != null) {
            realFakePlayer = MagiHandlers.getPlayer(this.tileOwner);
            return realFakePlayer;
        }
        if (realFakePlayer == null) {
            if (this.hasMHPlayer()) {
                if (MagiHandlers.getPlayer(this.tileOwner) != null) {
                    realFakePlayer = MagiHandlers.getPlayer(this.tileOwner);
                } else {
                    try {
                        realFakePlayer = FakePlayerFactory.get((WorldServer) this.worldObj, new GameProfile(UUID.fromString(getMHUuid()), getMHOwner()));
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
    public EntityPlayer getMHPlayerReplacing() {
        if (!this.hasMHPlayer()) {
            ItemStack item = new ItemStack(this.getBlockType(), 1, 0);
            NBTTagCompound nbt = new NBTTagCompound();
            this.writeToNBT(nbt);
            item.setTagCompound(nbt);
            this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, Blocks.air);
            this.invalidate();
            this.getWorldObj().setBlock(this.xCoord, this.yCoord, this.zCoord, GameData.getBlockRegistry().getObject("ExtraUtilities:chestMini"));
            ((IInventory)this.getWorldObj().getTileEntity(this.xCoord, this.yCoord, this.zCoord)).setInventorySlotContents(0, item);
        }
        return getMHPlayer();
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
