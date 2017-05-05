package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.hacks.botania.IMixinTileSpreader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

/**
 * Created by HeyZeer0 on 01/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "vazkii/botania/common/block/tile/mana/TileSpreader", remap = false)
public abstract class MixinTileSpreader extends TileEntity implements IMixinTileSpreader {

    @Shadow
    UUID identity;

    @Inject(method = "writeCustomNBT", at = @At(value = "INVOKE", args = {"getIdentifier"}), cancellable = true)
    private void injectWriteNBT(NBTTagCompound cmp, CallbackInfo cl) {
        if(identity != null) {
            cmp.setString("ownerUUID", identity.toString());
        }else{
            cmp.setString("ownerUUID", UUID.randomUUID().toString());
        }

    }

    @Inject(method = "readCustomNBT", at = @At(value = "INVOKE", args = {"func_74762_e"}), cancellable = true)
    private void injectReadNBT(NBTTagCompound cmp, CallbackInfo cl) {
        if(cmp.hasKey("ownerUUID"))
            identity = UUID.fromString(cmp.getString("ownerUUID"));
    }

    public void setOwner(UUID u) {
        identity = u;
    }

}
