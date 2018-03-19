package net.heyzeer0.mgh.mixins.thaumcraft;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.hacks.BlockHelper;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.heyzeer0.mgh.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
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

import java.util.List;

/**
 * Created by HeyZeer0 on 10/06/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "thaumcraft/common/items/wands/foci/ItemFocusPortableHole", remap = false)
public abstract class MixinItemFocusPortableHole extends ItemFocusBasic {

    @Shadow
    public static boolean createHole(World world, int ii, int jj, int kk, int side, byte count, int max) {
        return true;
    }

    @Overwrite
    public ItemStack onFocusRightClick(ItemStack itemstack, World world, EntityPlayer player, MovingObjectPosition mop) {
        ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (world.provider.dimensionId == Config.dimensionOuterId) {
                if (!world.isRemote) {
                    world.playSoundEffect((double) mop.blockX + 0.5D, (double) mop.blockY + 0.5D, (double) mop.blockZ + 0.5D, "thaumcraft:wandfail", 1.0F, 1.0F);
                }

                player.swingItem();
                return itemstack;
            }

            int ii = mop.blockX;
            int jj = mop.blockY;
            int kk = mop.blockZ;
            int enlarge = wand.getFocusEnlarge(itemstack);
            boolean distance = false;
            int maxdis = 33 + enlarge * 8;

            // MagiHandlers
            boolean cancel = false;

            int var17;
            for (var17 = 0; var17 < maxdis; ++var17) {
                Block c = world.getBlock(ii, jj, kk);

                // MagiHandlers start
                boolean breakLoop = false;
                BlockPos pos = new BlockPos(ii, jj, kk, world);

                // entities that would probably be affected by the hole, falling for example
                List<Entity> entities = MixinManager.getEntitiesNear(Entity.class, pos, 1);
                for (Entity entity : entities) {
                    if (entity != player && !MixinManager.canAttack(player, entity)) {
                        breakLoop = true;
                    }
                }

                if (!MixinManager.canBuild(player, pos)) {
                    breakLoop = true;
                }

                if (world.getTileEntity(pos.x, pos.y, pos.z) != null) {
                    breakLoop = true;
                }

                if (breakLoop) {
                    // update the client's block/tile entity
                    MagiHandlers.schedule(() -> BlockHelper.sendRangedBlockUpdate(pos.x, pos.y, pos.z, world, 2));
                    cancel = true;
                    break;
                }
                // MagiHandlers end

                if (ThaumcraftApi.portableHoleBlackList.contains(c) || c == Blocks.bedrock || c == ConfigBlocks.blockHole || c.isAir(world, ii, jj, kk) || c.getBlockHardness(world, ii, jj, kk) == -1.0F) {
                    break;
                }

                switch (mop.sideHit) {
                    case 0:
                        ++jj;
                        break;
                    case 1:
                        --jj;
                        break;
                    case 2:
                        ++kk;
                        break;
                    case 3:
                        --kk;
                        break;
                    case 4:
                        ++ii;
                        break;
                    case 5:
                        --ii;
                }
            }

            if (cancel) return itemstack;

            AspectList var18 = this.getVisCost(itemstack);
            Aspect[] di = var18.getAspects();
            int dur = di.length;

            for (int i$ = 0; i$ < dur; ++i$) {
                Aspect a = di[i$];
                var18.merge(a, var18.getAmount(a) * var17);
            }

            if (wand.consumeAllVis(itemstack, player, var18, true, false)) {
                int var19 = this.getUpgradeLevel(wand.getFocusItem(itemstack), FocusUpgradeType.extend);
                short var20 = (short) (120 + 60 * var19);
                createHole(world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, (byte) (var17 + 1), var20);
            }

            player.swingItem();
            if (!world.isRemote) {
                world.playSoundEffect((double) mop.blockX + 0.5D, (double) mop.blockY + 0.5D, (double) mop.blockZ + 0.5D, "mob.endermen.portal", 1.0F, 1.0F);
            }
        }

        return itemstack;
    }

    @Shadow
    public AspectList getVisCost(ItemStack is) {
        return null;
    }


}
