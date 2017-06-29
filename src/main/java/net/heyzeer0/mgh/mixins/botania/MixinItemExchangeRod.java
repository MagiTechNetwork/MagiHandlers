package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

/**
 * Created by Frani on 23/06/2017.
 */

@Pseudo
@Mixin(targets = "vazkii/botania/common/item/rod/ItemExchangeRod", remap = false)
public abstract class MixinItemExchangeRod {

    @Shadow public static ItemStack removeFromInventory(EntityPlayer player, ItemStack stack, Block block, int meta, boolean doit) { return null; }

    @Shadow public abstract void displayRemainderCounter(EntityPlayer player, ItemStack stack);

    @Overwrite
    public boolean exchange(World world, EntityPlayer player, int x, int y, int z, ItemStack stack, Block blockToSet, int metaToSet) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile != null)
            return false;

        BlockEvent.BreakEvent event = MixinManager.generateBlockEvent(x, y, z, world, player);
        MinecraftForge.EVENT_BUS.post(event);

        ItemStack placeStack = removeFromInventory(player, stack, blockToSet, metaToSet, false);
        if(placeStack != null) {
            Block blockAt = world.getBlock(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);
            if(!blockAt.isAir(world, x, y, z) && blockAt.getPlayerRelativeBlockHardness(player, world, x, y, z) > 0 && (blockAt != blockToSet || meta != metaToSet)) {
                if(!world.isRemote) {
                    if(!event.isCanceled()) {
                        if (!player.capabilities.isCreativeMode) {
                            List<ItemStack> drops = blockAt.getDrops(world, x, y, z, meta, 0);
                            for (ItemStack drop : drops)
                                world.spawnEntityInWorld(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, drop));
                            removeFromInventory(player, stack, blockToSet, metaToSet, true);
                        }
                        world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(blockAt) + (meta << 12));
                        world.setBlock(x, y, z, blockToSet, metaToSet, 1 | 2);
                        blockToSet.onBlockPlacedBy(world, x, y, z, player, placeStack);
                    }
                    return false;
                }
                displayRemainderCounter(player, stack);
                return true;
            }
        }
        return false;
    }

}
