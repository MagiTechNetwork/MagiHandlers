package net.heyzeer0.mgh.mixins.witchery;

import com.emoniph.witchery.entity.EntitySpellEffect;
import net.heyzeer0.mgh.events.ThrowableHitEntityEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


/**
 * Created by Frani on 13/06/2017.
 */

@Pseudo
@Mixin(targets = "com/emoniph/witchery/entity/EntitySpellEffect", remap = false)
public abstract class MixinEntitySpellEffect {

    @Shadow
    public EntityLivingBase shootingEntity;

    @Invoker("onImpact")
    protected abstract void impact(MovingObjectPosition mop);

    @Redirect(method = "func_70071_h_", at = @At(value = "INVOKE", target = "Lcom/emoniph/witchery/entity/EntitySpellEffect;onImpact(Lnet/minecraft/util/MovingObjectPosition;)V"))
    private void injectOnImpact(EntitySpellEffect instance, MovingObjectPosition mop) {
        ThrowableHitEntityEvent event = new ThrowableHitEntityEvent(instance, mop, shootingEntity);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            ((MixinEntitySpellEffect)(Object)instance).impact(mop);
        }
    }

}
