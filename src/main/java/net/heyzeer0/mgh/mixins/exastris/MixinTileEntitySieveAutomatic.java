package net.heyzeer0.mgh.mixins.exastris;

import cofh.api.energy.EnergyStorage;
import net.minecraft.item.ItemStack;
import org.bukkit.Bukkit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by HeyZeer0 on 10/09/2017.
 * Copyright © HeyZeer0 - 2016
 */

@Pseudo
@Mixin(targets = "ExAstris/Block/TileEntity/TileEntitySieveAutomatic", remap = false)
public abstract class MixinTileEntitySieveAutomatic {

    @Shadow
    protected ItemStack[] inventory;

    @Shadow public EnergyStorage storage;

    @Inject(method = "ProcessContents", at = @At("HEAD"), cancellable = true)
    public void injectProcess(CallbackInfo ci) {

        boolean space = false;
        for(ItemStack i : inventory) {
            if(i == null) {
                space = true;
                break;
            }
        }

        if(!space)
            ci.cancel();

    }

}
