package net.heyzeer0.mgh.mixins.mekanism;

import mekanism.common.CommonProxy;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.ref.WeakReference;

/**
 * Created by Frani on 13/08/2017.
 */

@Pseudo
@Mixin(targets = "mekanism/common/tile/TileEntityDigitalMiner", remap = false)
public abstract class MixinTileEntityDigitalMiner extends TileEntity implements IForgeTileEntity {

    @Redirect(method = "setReplace", at = @At(value = "INVOKE", target = "Lmekanism/common/CommonProxy;getDummyPlayer(Lnet/minecraft/world/WorldServer;DDD)Ljava/lang/ref/WeakReference;"))
    public WeakReference<EntityPlayer> redirectFakePlayer(CommonProxy proxy, WorldServer world, double x, double y, double z) {
        return new WeakReference<EntityPlayer>(this.getMHPlayer());
    }

}
