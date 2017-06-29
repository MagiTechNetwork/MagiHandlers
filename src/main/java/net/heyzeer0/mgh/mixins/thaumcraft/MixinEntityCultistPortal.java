package net.heyzeer0.mgh.mixins.thaumcraft;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(targets = "thaumcraft/common/entities/monster/boss/EntityCultistLeader", remap = false)
public abstract class MixinEntityCultistPortal extends EntityMob {

    public MixinEntityCultistPortal(World world) {
        super(world);
    }

    @Overwrite
    protected void func_110147_ax() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(900.0D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D);
    }

}