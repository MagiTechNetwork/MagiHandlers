package net.heyzeer0.mgh.mixins.mekanism;

import mekanism.common.block.BlockMachine;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Created by Frani on 13/08/2017.
 */

@Pseudo
@Mixin(targets = "mekanism/common/block/BlockMachine", remap = false)
public abstract class MixinBlockMachine extends BlockContainer {

    public MixinBlockMachine(Material m) {
        super(m);
    }

    EntityPlayer owner;

    @Shadow public BlockMachine.MachineBlock blockType;

    @Overwrite
    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        if (BlockMachine.MachineType.get(blockType, metadata) == null) {
            return null;
        }

        TileEntity te = BlockMachine.MachineType.get(blockType, metadata).create();
        if (te instanceof ITileEntityOwnable && this.owner != null) {
            ((ITileEntityOwnable) te).setOwner(owner.getCommandSenderName());
            ((ITileEntityOwnable) te).setUUID(owner.getUniqueID().toString());
        }

        return te;
    }
}
