package net.heyzeer0.mgh.mixins.ic2;

import ic2.api.energy.tile.IEnergySink;
import ic2.core.block.TileEntityInventory;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Created by Frani on 05/08/2017.
 */

@Pseudo
@Mixin(targets = "ic2/core/block/machine/tileentity/TileEntityElectricMachine", remap = false)
public abstract class MixinTileEntityElectricMachine extends TileEntityInventory implements IEnergySink, ITileEntityOwnable {

    @Shadow public double energy;
    private String owner;
    private String uuid;

    @Override
    public String getUUID() {
        return this.uuid;
    }

    @Override
    public String getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    @Overwrite
    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.energy = nbttagcompound.getDouble("energy");
        this.owner = nbttagcompound.getString("owner");
        this.uuid = nbttagcompound.getString("uuid");
    }

    @Overwrite
    public void func_145841_b(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("energy", this.energy);
        nbttagcompound.setString("owner", this.owner);
        nbttagcompound.setString("uuid", this.uuid);
    }

}
