package net.heyzeer0.mgh.mixins.enderio;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

/**
 * Created by Frani on 13/08/2017.
 */

@Pseudo
@Mixin(targets = "crazypants/enderio/teleport/telepad/TileTelePad", remap = false)
public abstract class MixinTileTelePad {

    @Redirect(method = "serverTeleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeEntity(Lnet/minecraft/entity/Entity;)V"))
    public void redirectEntityRemove(WorldServer w, Entity e) {
        AxisAlignedBB area = AxisAlignedBB.getBoundingBox(e.posX - 2, e.posY - 2, e.posZ - 2, e.posX + 2, e.posY + 2, e.posZ + 2);
        w.removeEntity(e);
        List<EntityItem> items = w.getEntitiesWithinAABB(EntityItem.class, area);
        if(!items.isEmpty()) {
            for (EntityItem i : items) {
                i.setDead();
            }
        }
    }
}
