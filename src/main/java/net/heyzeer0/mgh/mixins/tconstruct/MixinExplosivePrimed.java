package net.heyzeer0.mgh.mixins.tconstruct;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Created by HeyZeer0 on 01/07/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "tconstruct/mechworks/entity/item/ExplosivePrimed", remap = false)
public class MixinExplosivePrimed {

    @Overwrite
    public void createExplosion(World world, Entity par1Entity, double par2, double par4, double par6, float par8) {
        world.createExplosion(par1Entity, par2, par4, par6, par8, true).exploder = par1Entity;
    }

}
