package net.heyzeer0.mgh.mixins.witchery;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Created by Frani on 13/06/2017.
 */

@Pseudo
@Mixin(targets = "com/emoniph/witchery/entity/EntitySpellEffect", remap = false)
public abstract class MixinEntitySpellEffect extends Entity {

    public MixinEntitySpellEffect(World w) {
        super(w);
    }

    @Shadow
    public EntityLivingBase shootingEntity;

    @Inject(method = "onImpact", at = @At(value = "INVOKE", target = "Lcom/emoniph/witchery/infusion/infusions/symbols/SymbolEffectProjectile;onCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/util/MovingObjectPosition;Lcom/emoniph/witchery/entity/EntitySpellEffect;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void injectImpact(MovingObjectPosition mop, CallbackInfo ci) {
        if(mop.entityHit != null && mop.entityHit instanceof EntityLivingBase && shootingEntity instanceof EntityPlayer) {
            if(!MixinManager.canAttack((EntityPlayer)shootingEntity, mop.entityHit)) {
                ci.cancel();
                setDead();
            }
        }
        if(mop.entityHit == null) {
            BlockEvent.BreakEvent evt2 = MixinManager.generateBlockEvent(mop.blockX, mop.blockY, mop.blockZ, worldObj, (EntityPlayer)shootingEntity);
            MinecraftForge.EVENT_BUS.post(evt2);
            if(evt2.isCanceled()) {
                ci.cancel();
                setDead();
            }
        }
    }

}
