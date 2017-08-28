package net.heyzeer0.mgh.mixins.chisel;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 27/08/2017.
 */

@Pseudo
@Mixin(targets = "team/chisel/entity/EntityBallOMoss", remap = false)
public abstract class MixinEntityBallOMoss extends EntityThrowable {

    @Shadow public static void turnToMoss(World world, int x, int y, int z) {}

    public MixinEntityBallOMoss(World w) {
        super(w);
    }

    @Redirect(method = "func_70184_a", at = @At(value = "INVOKE", target = "Lteam/chisel/entity/EntityBallOMoss;turnToMoss(Lnet/minecraft/world/World;III)V"))
    public void onImpact(World world, int x, int y, int z) {
        if(this.getThrower() instanceof EntityPlayer) {
            BlockEvent.BreakEvent e = MixinManager.generateBlockEvent(x, y, z, world, (EntityPlayer) this.getThrower());
            MinecraftForge.EVENT_BUS.post(e);
            if(!e.isCanceled()) {
                turnToMoss(world, x, y, z);
            }
        }
    }

}
