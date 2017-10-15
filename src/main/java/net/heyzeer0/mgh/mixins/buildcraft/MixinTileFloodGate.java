package net.heyzeer0.mgh.mixins.buildcraft;

import com.mojang.authlib.GameProfile;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

/**
 * Created by Frani on 12/08/2017.
 */

@Pseudo
@Mixin(targets = "buildcraft/factory/TileFloodGate", remap = false)
public abstract class MixinTileFloodGate extends TileEntity implements ITileEntityOwnable {

    @Inject(method = "canPlaceFluidAt", at = @At("HEAD"), cancellable = true)
    public void fireBreak(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (!MixinManager.canBuild(x, y, z, worldObj, getFakePlayer())) {
            cir.setReturnValue(false);
        }
    }

}
