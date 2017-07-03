package net.heyzeer0.mgh.mixins.mfr;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import powercrystals.minefactoryreloaded.item.gun.ammo.ItemNeedlegunAmmoBlock;

/**
 * Created by Frani on 02/07/2017.
 */

@Pseudo
@Mixin(targets = "powercrystals/minefactoryreloaded/item/gun/ammo/ItemNeedlegunAmmoAnvil", remap = false)
public abstract class MixinItemNeedlegunAmmoAnvil extends ItemNeedlegunAmmoBlock {

    public MixinItemNeedlegunAmmoAnvil(Block block, int meta) {
        super(block, meta);
    }

    protected void placeBlockAt(World world, int x, int y, int z, double distance, EntityPlayer owner) {
        if (!world.isRemote) {
            BlockEvent.PlaceEvent evt = MixinManager.generatePlaceEvent(x, y, z, world, owner);
            MinecraftForge.EVENT_BUS.post(evt);
            if(!evt.isCanceled()) {
                EntityFallingBlock anvil = new EntityFallingBlock(world, x + 0.5, y + 0.5, z + 0.5,
                        _block, _blockMeta);
                anvil.func_145806_a(true);
                world.spawnEntityInWorld(anvil);
                anvil.fallDistance = ((float) distance) + 1f;
                anvil.field_145812_b = 3;
                anvil.onUpdate();
            }
        }
    }
}
