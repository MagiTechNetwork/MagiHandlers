package net.heyzeer0.mgh.mixins.thaumcraft;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.registry.GameData;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.TileThaumcraft;

import java.util.UUID;

/**
 * Created by Frani on 11/08/2017.
 */

@Pseudo
@Mixin(targets = "thaumcraft/common/tiles/TileArcaneBore", remap = false)
public abstract class MixinTileArcaneBore extends TileThaumcraft implements ITileEntityOwnable {

    private FakePlayer realFakePlayer;

    @Shadow int digX;
    @Shadow int digY;
    @Shadow int digZ;

    @Override
    public FakePlayer getFakePlayer() {
        if (realFakePlayer == null) {
            if (!this.getOwner().isEmpty() && !this.getUUID().isEmpty()) {
                realFakePlayer = FakePlayerFactory.get((WorldServer) this.worldObj, new GameProfile(UUID.fromString(getUUID()), getOwner()));
            } else {
                realFakePlayer = FakePlayerFactory.getMinecraft((WorldServer)this.worldObj);
                ItemStack item = new ItemStack(this.getBlockType(), 1, this.getBlockMetadata());
                NBTTagCompound nbt = new NBTTagCompound();
                this.writeToNBT(nbt);
                item.setTagCompound(nbt);
                this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, Blocks.air);
                this.invalidate();
                this.getWorldObj().setBlock(this.xCoord, this.yCoord, this.zCoord, GameData.getBlockRegistry().getObject("ExtraUtilities:chestMini"));
                ((IInventory)this.getWorldObj().getTileEntity(this.xCoord, this.yCoord, this.zCoord)).setInventorySlotContents(0, item);
            }
        }
        return realFakePlayer;
    }

    @Inject(method = "dig", at = @At("HEAD"), cancellable = true)
    public void fireBreak(CallbackInfo ci) {
        EntityPlayer player = this.getOwner() == null ? null : MinecraftServer.getServer().getConfigurationManager().func_152612_a(this.getOwner());
        BlockEvent.BreakEvent e = MixinManager.generateBlockEvent(this.digX, this.digY, this.digZ, this.worldObj, player == null ? this.getFakePlayer() : player);
        MinecraftForge.EVENT_BUS.post(e);
        if(e.isCanceled()) {
            ci.cancel();
        }
    }

}
