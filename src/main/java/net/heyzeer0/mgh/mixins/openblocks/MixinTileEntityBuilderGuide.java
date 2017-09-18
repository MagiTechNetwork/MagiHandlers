package net.heyzeer0.mgh.mixins.openblocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 18/09/2017.
 */

@Pseudo
@Mixin(targets = "openblocks/common/tileentity/TileEntityBuilderGuide", remap = false)
public abstract class MixinTileEntityBuilderGuide {

    @Redirect(method = "survivalPlaceBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/ItemInWorldManager;activateBlockOrUseItem(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIIIFFF)Z"))
    public boolean injectBlockPlace(ItemInWorldManager instance, EntityPlayer player, World world, ItemStack stack, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (stack.getItem() instanceof ItemBlock) {

            ItemBlock itemBlock = (ItemBlock) stack.getItem();
            BlockSnapshot snapshot = BlockSnapshot.getBlockSnapshot(world, x, y, z);
            BlockEvent.PlaceEvent event = new BlockEvent.PlaceEvent(snapshot, world.getBlock(x, y, z), player);
            MinecraftForge.EVENT_BUS.post(event);

            if (!event.isCanceled()) {
                itemBlock.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, itemBlock.getMetadata(stack.getItemDamage()));
                --stack.stackSize;
                return true;
            } else {
                snapshot.restore();
            }
        }
        return false;
    }

}
