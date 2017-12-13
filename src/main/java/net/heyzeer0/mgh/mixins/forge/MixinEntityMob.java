package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.api.IMixinChunk;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 15/10/2017.
 */
@Mixin(EntityMob.class)
public abstract class MixinEntityMob extends Entity {

    public MixinEntityMob(World w) {
        super(w);
    }

    @Inject(method = "isValidLightLevel", at = @At("HEAD"))
    private void checkIfChunkIsInvalid(CallbackInfoReturnable<Boolean> ci) {
        Chunk chunk = this.worldObj.getChunkFromBlockCoords((int) this.posX, (int) this.posZ);
        if (chunk == null || ((IMixinChunk)chunk).isMarkedToUnload()) {
            ci.setReturnValue(false);
        }
    }

}
