package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.api.IEntityThrowable;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.entity.EntityManaBurst;

/**
 * Created by Frani on 02/11/2017.
 */

@Pseudo
@Mixin(value = TileSpreader.class, remap = false)
public abstract class MixinTileSpreader implements IForgeTileEntity {

    @Redirect(method = "getBurst", at = @At(value = "INVOKE", target = "Lvazkii/botania/common/entity/EntityManaBurst;setSourceLens(Lnet/minecraft/item/ItemStack;)V"))
    private void onSpawnEntity(EntityManaBurst entity, ItemStack lens) {
        if (entity != null) {
            ((IEntityThrowable)entity).setThrower(getFakePlayer());
        }

        entity.setSourceLens(lens);
    }

}
