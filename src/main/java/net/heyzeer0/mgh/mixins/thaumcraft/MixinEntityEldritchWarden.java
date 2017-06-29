package net.heyzeer0.mgh.mixins.thaumcraft;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Created by Frani on 29/06/2017.
 */

@Pseudo
@Mixin(targets = "thaumcraft/common/entities/monster/boss/EntityEldritchWarden", remap = false)
public abstract class MixinEntityEldritchWarden extends EntityMob {

    public MixinEntityEldritchWarden(World world) {
        super(world);
    }

    @Overwrite
    protected void func_110147_ax() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.33D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(500.0D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(15.0D);
    }

}
