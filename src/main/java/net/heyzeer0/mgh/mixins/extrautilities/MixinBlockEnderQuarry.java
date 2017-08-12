package net.heyzeer0.mgh.mixins.extrautilities;

import com.rwtema.extrautils.tileentity.enderquarry.TileEntityEnderQuarry;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Created by Frani on 12/08/2017.
 */

@Pseudo
@Mixin(targets = "com/rwtema/extrautils/tileentity/enderquarry/BlockEnderQuarry", remap = false)
public abstract class MixinBlockEnderQuarry extends Block {

    EntityPlayer owner;

    public MixinBlockEnderQuarry(Material m) {
        super(m);
    }

    @Override
    public void onBlockPlacedBy(World w, int par2, int par3, int par4, EntityLivingBase e, ItemStack stack) {
        if(e instanceof EntityPlayer) {
            this.owner = (EntityPlayer) e;
        }
    }

    @Overwrite
    public TileEntity createTileEntity(final World world, final int metadata) {
        TileEntity te = new TileEntityEnderQuarry();
        if(te instanceof ITileEntityOwnable) {
            ((ITileEntityOwnable) te).setOwner(owner.getCommandSenderName());
            ((ITileEntityOwnable) te).setUUID(owner.getUniqueID().toString());
        }
        return te;
    }

}
