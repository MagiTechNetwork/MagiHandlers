package net.heyzeer0.mgh.mixins.thaumcraft;

import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.tiles.*;

/**
 * Created by Frani on 11/08/2017.
 */

@Pseudo
@Mixin(targets = "thaumcraft/common/blocks/BlockWoodenDevice", remap = false)
public abstract class MixinBlockWoodenDevice extends BlockContainer {

    public MixinBlockWoodenDevice(Material m) {
        super(m);
    }

    public EntityPlayer owner;

    @Inject(method = "func_149689_a", at = @At("HEAD"))
    public void setOwner(World w, int x, int y, int z, EntityLivingBase e, ItemStack s, CallbackInfo ci) {
        if (e instanceof EntityPlayer) {
            this.owner = (EntityPlayer)e;
        }
    }

    @Overwrite
    public TileEntity createTileEntity(World world, int metadata) {
        TileEntity te = (TileEntity)(metadata == 0 ? new TileBellows()
                : (metadata == 1 ? new TileSensor()
                : (metadata == 2 ? new TileArcanePressurePlate()
                : (metadata == 3 ? new TileArcanePressurePlate()
                : (metadata == 4 ? new TileArcaneBoreBase()
                : (metadata == 5 ? new TileArcaneBore()
                : (metadata == 8 ? new TileBanner()
                : super.createTileEntity(world, metadata))))))));

        if (te instanceof ITileEntityOwnable && owner != null) {
            ((ITileEntityOwnable)te).setOwner(owner.getCommandSenderName());
            ((ITileEntityOwnable)te).setUUID(owner.getUniqueID().toString());
        }
        return te;
    }

}
