package net.heyzeer0.mgh.mixins.forge;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

/**
 * Created by Frani on 09/02/2018.
 */
@Mixin(EntityTameable.class)
public abstract class MixinEntityTameable extends EntityAnimal {

    private UUID tamedOwner;

    public MixinEntityTameable(World w) {
        super(w);
    }

    @Shadow
    public abstract String func_152113_b();

    @Overwrite
    public EntityLivingBase getOwner() {
        try {
            if (tamedOwner == null) {
                tamedOwner = UUID.fromString(func_152113_b());
            }
        } catch (Exception e) {
            return null;
        }
        return this.worldObj.func_152378_a(tamedOwner);
    }

    @Inject(method = "func_152115_b", at = @At("RETURN"))
    private void onSetOwner(String uuid, CallbackInfo ci) {
        this.tamedOwner = UUID.fromString(uuid);
    }


}
