package net.heyzeer0.mgh.mixins.thaumcraft;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

/**
 * Created by Frani on 28/06/2017.
 */

@Pseudo
@Mixin(targets = "thaumcraft/common/entities/monster/boss/EntityThaumcraftBoss", remap = false)
public abstract class MixinEntityThaumcraftBoss extends EntityMob {

    public MixinEntityThaumcraftBoss(World world) {
        super(world);
    }

    @Shadow
    HashMap aggro = new HashMap();

    @Shadow
    public abstract int getAnger();

    @Shadow public abstract void setAnger(int par1);

    @Overwrite
    public boolean func_70097_a(DamageSource source, float damage) {
        if(!this.worldObj.isRemote) {
            if(source.getEntity() != null && source.getEntity() instanceof EntityLivingBase) {
                int e = source.getEntity().getEntityId();
                int ad = (int)damage;
                if(this.aggro.containsKey(Integer.valueOf(e))) {
                    ad += ((Integer)this.aggro.get(Integer.valueOf(e))).intValue();
                }

                this.aggro.put(Integer.valueOf(e), Integer.valueOf(ad));
            }

            damage = source.isProjectile() ? 0.1F : damage;

            if(damage > 13.0F) {
                if(this.getAnger() == 0) {
                    try {
                        this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, (int)(damage / 15.0F)));
                        this.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 200, (int)(damage / 40.0F)));
                        this.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 200, (int)(damage / 40.0F)));
                        this.setAnger(200);
                    } catch (Exception var5) {

                    }

                    if(source.getEntity() != null && source.getEntity() instanceof EntityPlayer) {
                        ((EntityPlayer)source.getEntity()).addChatMessage(new ChatComponentText(this.getCommandSenderName() + " " + StatCollector.translateToLocal("tc.boss.enrage")));
                        source = DamageSource.causePlayerDamage((EntityPlayer)source.getEntity());
                    }
                }
            }
        }
        return super.attackEntityFrom(source, damage);
    }

}
