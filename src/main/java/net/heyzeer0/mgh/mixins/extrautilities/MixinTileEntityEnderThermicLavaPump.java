package net.heyzeer0.mgh.mixins.extrautilities;

import cofh.api.energy.EnergyStorage;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Facing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.TileFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 13/06/2017.
 */

@Pseudo
@Mixin(targets = "com/rwtema/extrautils/tileentity/TileEntityEnderThermicLavaPump", remap = false)
public abstract class MixinTileEntityEnderThermicLavaPump extends TileFluidHandler {

    @Shadow private ForgeChunkManager.Ticket chunkTicket;

    @Shadow public boolean finished;

    @Shadow private int pump_y;

    @Shadow private int chunk_x;

    @Shadow private int chunk_z;

    @Shadow private EnergyStorage cofhEnergy;

    @Shadow private int chunk_no;

    @Shadow public EntityPlayer owner;

    @Shadow private int b;

    @Shadow private boolean init;

    @Shadow private float p;

    @Shadow public abstract void setChunk(int chunk_no);

    @Inject(method = "forceChunkLoading", at = @At("HEAD"), cancellable = true)
    private void injectChunkLoading(final ForgeChunkManager.Ticket ticket, CallbackInfo ci) {
        ci.cancel();
    }

    @Overwrite
    public void func_145845_h() {
        if (this.worldObj.isRemote) {
            return;
        }
        if (this.finished) {
            if (this.chunkTicket != null) {
                ForgeChunkManager.releaseTicket(this.chunkTicket);
                this.chunkTicket = null;
            }
            return;
        }
        if (this.chunkTicket == null) {
            boolean valid = false;
            if (ExtraUtils.validDimensionsForEnderPump != null) {
                if (ExtraUtils.allNonVanillaDimensionsValidForEnderPump) {
                    valid = true;
                }
                for (int i = 0; i < ExtraUtils.validDimensionsForEnderPump.length; ++i) {
                    if (ExtraUtils.validDimensionsForEnderPump[i] == this.worldObj.provider.dimensionId) {
                        valid = !valid;
                        break;
                    }
                }
            }
            if (!valid) {
                this.finished = true;
                if (this.owner != null) {
                    this.owner.addChatComponentMessage((IChatComponent) new ChatComponentText("Pump will not function in this dimension"));
                    this.owner = null;
                }
                this.markDirty();
                return;
            }
            /*
            this.chunkTicket = ForgeChunkManager.requestTicket((Object) ExtraUtilsMod.instance, this.worldObj, ForgeChunkManager.Type.NORMAL);
            if (this.chunkTicket == null) {
                this.finished = true;
                if (this.owner != null) {
                    this.owner.addChatComponentMessage((IChatComponent)new ChatComponentText("Unable to assign Chunkloader, this pump will not work"));
                    this.owner = null;
                }
                this.markDirty();
                return;
            }
            this.owner = null;
            this.chunkTicket.getModData().setString("id", "pump");
            this.chunkTicket.getModData().setInteger("pumpX", this.xCoord);
            this.chunkTicket.getModData().setInteger("pumpY", this.yCoord);
            this.chunkTicket.getModData().setInteger("pumpZ", this.zCoord);
            ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
            */
        }
        boolean goAgain = true;
        for (int t = 0; t < 16 && goAgain; ++t) {
            goAgain = false;
            final int bx = this.b >> 4;
            final int bz = this.b & 0xF;
            final int pump_x = (this.chunk_x << 4) + bx;
            final int pump_z = (this.chunk_z << 4) + bz;
            if (this.pump_y >= 0 && XUHelper.drainBlock(this.worldObj, pump_x, this.pump_y, pump_z, false) != null) {
                if ((this.tank.getInfo().fluid == null || this.tank.getInfo().fluid.amount <= 0) && this.cofhEnergy.extractEnergy(3000, true) == 3000 && this.cofhEnergy.extractEnergy(3000, false) > 0) {
                    final FluidStack liquid = XUHelper.drainBlock(this.worldObj, pump_x, this.pump_y, pump_z, true);
                    this.tank.fill(liquid, true);
                    if (this.worldObj.isAirBlock(pump_x, this.pump_y, pump_z)) {
                        if (this.worldObj.rand.nextDouble() < this.p) {
                            this.worldObj.setBlock(pump_x, this.pump_y, pump_z, Blocks.stone, 0, 2);
                        }
                        else {
                            this.worldObj.setBlock(pump_x, this.pump_y, pump_z, Blocks.cobblestone, 0, 2);
                        }
                    }
                    --this.pump_y;
                    this.markDirty();
                }
            }
            else {
                goAgain = true;
                if (!this.init) {
                    this.b = 256;
                }
                ++this.b;
                if (this.b >= 256) {
                    this.b = 0;
                    goAgain = false;
                    if (this.init && this.chunk_no > 0) {
                        /*for (int dx = -2; dx <= 2; ++dx) {
                            for (int dz = -2; dz <= 2; ++dz) {
                                ForgeChunkManager.unforceChunk(this.chunkTicket, new ChunkCoordIntPair(this.chunk_x + dx, this.chunk_z + dz));
                            }
                        }*/
                    }
                    this.setChunk(++this.chunk_no);
                    /*for (int dx = -2; dx <= 2; ++dx) {
                        for (int dz = -2; dz <= 2; ++dz) {
                            ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.chunk_x + dx, this.chunk_z + dz));
                            this.worldObj.getChunkFromChunkCoords(this.chunk_x + dx, this.chunk_z + dz);
                        }
                    }
                    ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
                    */
                }
                this.pump_y = this.yCoord - 1;
                this.init = true;
                this.markDirty();
            }
        }
        FluidStack liquid2 = this.tank.getInfo().fluid;
        if (liquid2 != null && liquid2.amount > 0) {
            final int[] seq = XUHelper.rndSeq(6, this.worldObj.rand);
            for (int j = 0; j < 6; ++j) {
                final TileEntity tile = this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[seq[j]], this.yCoord + Facing.offsetsYForSide[seq[j]], this.zCoord + Facing.offsetsZForSide[seq[j]]);
                if (tile instanceof IFluidHandler) {
                    final int moved = ((IFluidHandler)tile).fill(ForgeDirection.values()[seq[j]].getOpposite(), liquid2, true);
                    this.markDirty();
                    this.tank.drain(moved, true);
                    liquid2 = this.tank.getInfo().fluid;
                    if (liquid2 == null) {
                        break;
                    }
                    if (liquid2.amount <= 0) {
                        break;
                    }
                }
            }
        }
    }

}
