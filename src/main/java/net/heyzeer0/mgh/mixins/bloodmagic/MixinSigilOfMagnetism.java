package net.heyzeer0.mgh.mixins.bloodmagic;

import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Frani on 12/08/2017.
 */

@Pseudo
@Mixin(targets = "WayofTime/alchemicalWizardry/common/items/sigil/SigilOfMagnetism", remap = false)
public abstract class MixinSigilOfMagnetism extends EnergyItems {

    @Shadow
    private int tickDelay = 300;

    @Overwrite
    public void func_77663_a(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (!(par3Entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;
        if (par1ItemStack.getTagCompound() == null) {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }
        if (par1ItemStack.getTagCompound().getBoolean("isActive")) {
            if (par2World.getWorldTime() % this.tickDelay == par1ItemStack.getTagCompound().getInteger("worldTimeDelay")) {
                if (!EnergyItems.syphonBatteries(par1ItemStack, (EntityPlayer) par3Entity, getEnergyUsed())) {
                    par1ItemStack.getTagCompound().setBoolean("isActive", false);
                }
            }
            int range = 5;
            int verticalRange = 5;
            float posX = (float)Math.round(par3Entity.posX);
            float posY = (float)(par3Entity.posY - (double)par3Entity.getEyeHeight());
            float posZ = (float)Math.round(par3Entity.posZ);
            List<EntityItem> entities = par3EntityPlayer.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox((double)(posX - 0.5F), (double)(posY - 0.5F), (double)(posZ - 0.5F), (double)(posX + 0.5F), (double)(posY + 0.5F), (double)(posZ + 0.5F)).expand((double)range, (double)verticalRange, (double)range));
            List<EntityXPOrb> xpOrbs = par3EntityPlayer.worldObj.getEntitiesWithinAABB(EntityXPOrb.class, AxisAlignedBB.getBoundingBox((double)(posX - 0.5F), (double)(posY - 0.5F), (double)(posZ - 0.5F), (double)(posX + 0.5F), (double)(posY + 0.5F), (double)(posZ + 0.5F)).expand((double)range, (double)verticalRange, (double)range));
            Iterator i$ = entities.iterator();

            while(i$.hasNext()) {
                EntityItem entity = (EntityItem)i$.next();
                if (!entity.isDead && entity != null && !par2World.isRemote) {
                    entity.onCollideWithPlayer(par3EntityPlayer);
                }
            }

            i$ = xpOrbs.iterator();

            while(i$.hasNext()) {
                EntityXPOrb xpOrb = (EntityXPOrb)i$.next();
                if (!xpOrb.isDead && xpOrb != null && !par2World.isRemote) {
                    xpOrb.onCollideWithPlayer(par3EntityPlayer);
                }
            }

        }
    }

}
