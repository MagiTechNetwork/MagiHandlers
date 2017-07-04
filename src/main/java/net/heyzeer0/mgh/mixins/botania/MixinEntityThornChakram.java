package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 03/07/2017.
 */

@Pseudo
@Mixin(targets = "vazkii/botania/common/entity/EntityThornChakram", remap = false)
public abstract class MixinEntityThornChakram extends EntityThrowable {

    public MixinEntityThornChakram(World world) {
        super(world);
    }

    @Inject(method = "func_70184_a", at = @At("HEAD"), cancellable = true)
    private void injectImpact(MovingObjectPosition pos, CallbackInfo ci) {
        if (pos.entityHit != null && pos.entityHit instanceof EntityLivingBase && pos.entityHit != getThrower() && getThrower() instanceof EntityPlayer) {
            if (!MixinManager.canAttack((EntityPlayer)getThrower(), pos.entityHit)) {
                ci.cancel();
            }
        }
    }

}

