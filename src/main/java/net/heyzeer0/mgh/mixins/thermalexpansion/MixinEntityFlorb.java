package net.heyzeer0.mgh.mixins.thermalexpansion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 10/06/2017.
 */
@Pseudo
@Mixin(targets = "cofh/thermalexpansion/entity/projectile/EntityFlorb", remap = false)
public abstract class MixinEntityFlorb extends EntityThrowable {

    public MixinEntityFlorb(World world) {
        super(world);
    }

    @Inject(method = "func_70184_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z", shift = At.Shift.BEFORE), cancellable = true)
    private void injectImpact(MovingObjectPosition mop, CallbackInfo ci) {
        if(getThrower() instanceof EntityPlayer){
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(mop.blockX, mop.blockY, mop.blockZ, ((EntityPlayer) getThrower()).getEntityWorld(), worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ), worldObj.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ), (EntityPlayer)getThrower());
            MinecraftForge.EVENT_BUS.post(event);
            if(event.isCanceled()) {
                ci.cancel();
                this.setDead();
            }
        }
    }

}
