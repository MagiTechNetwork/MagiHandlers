package net.heyzeer0.mgh.mixins.thaumcraft;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.wands.ItemWandCasting;

/**
 * Created by HeyZeer0 on 10/06/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "thaumcraft/common/items/wands/foci/ItemFocusPortableHole", remap = false)
public abstract class MixinItemFocusPortableHole extends ItemFocusBasic {

    @Overwrite
    public ItemStack onFocusRightClick(ItemStack itemstack, World world, EntityPlayer player, MovingObjectPosition mop)
    {
        ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();

        if ((mop != null) && (mop.typeOfHit == net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK)) {
            if (world.provider.dimensionId == Config.dimensionOuterId) {
                if (!world.isRemote) {
                    world.playSoundEffect(mop.blockX + 0.5D, mop.blockY + 0.5D, mop.blockZ + 0.5D, "thaumcraft:wandfail", 1.0F, 1.0F);
                }
                player.swingItem();
                return itemstack;
            }
            int ii = mop.blockX;
            int jj = mop.blockY;
            int kk = mop.blockZ;
            int enlarge = wand.getFocusEnlarge(itemstack);
            int distance = 0;
            int maxdis = 33 + enlarge * 8;
            for (distance = 0; distance < maxdis; distance++) {
                Block bi = world.getBlock(ii, jj, kk);
                if ((ThaumcraftApi.portableHoleBlackList.contains(bi)) || (bi == Blocks.bedrock) || (bi == ConfigBlocks.blockHole) || (bi.isAir(world, ii, jj, kk)) || (bi.getBlockHardness(world, ii, jj, kk) == -1.0F)) {
                    break;
                }

                switch (mop.sideHit) {
                    case 0:  jj++; break;
                    case 1:  jj--; break;
                    case 2:  kk++; break;
                    case 3:  kk--; break;
                    case 4:  ii++; break;
                    case 5:  ii--;
                }

            }

            AspectList c = getVisCost(itemstack);
            for (Aspect a : c.getAspects()) {
                c.merge(a, c.getAmount(a) * distance);
            }

            if (wand.consumeAllVis(itemstack, player, c, true, false)) {
                int di = getUpgradeLevel(wand.getFocusItem(itemstack), FocusUpgradeType.extend);
                short dur = (short)(120 + 60 * di);

                MixinManager.createHole(player, world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, (byte)(distance + 1), dur);
            }
            player.swingItem();
            if (!world.isRemote) { world.playSoundEffect(mop.blockX + 0.5D, mop.blockY + 0.5D, mop.blockZ + 0.5D, "mob.endermen.portal", 1.0F, 1.0F);
            }
        }
        return itemstack;
    }

    @Shadow
    public AspectList getVisCost(ItemStack is) {
        return null;
    }


}
