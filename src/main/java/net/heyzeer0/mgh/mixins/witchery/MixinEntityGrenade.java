package net.heyzeer0.mgh.mixins.witchery;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.EntityThrowableBase;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityFollower;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.TimeUtil;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

/**
 * Created by Frani on 14/06/2017.
 */

@Pseudo
@Mixin(targets = "com/emoniph/witchery/entity/EntityGrenade", remap = false)
public abstract class MixinEntityGrenade extends EntityThrowableBase {

    @Shadow public abstract int getMode();

    public MixinEntityGrenade(World w) {
        super(w);
    }

    @Shadow
    boolean blockPlaced;

    @Shadow
    public int blockX;

    @Shadow
    public int blockY;

    @Shadow
    public int blockZ;

    @Shadow private String owner;

    @Overwrite
    protected void onSetDead() {
        if (!this.worldObj.isRemote) {
            this.entityDropItem(Witchery.Items.GENERIC.itemQuartzSphere.createStack(), 0.5f);
            int mode = this.getMode();
            if (mode == 0) {
                if (this.blockPlaced) {
                    this.blockPlaced = false;
                    if (this.worldObj.getBlock(this.blockX, this.blockY, this.blockZ) == Witchery.Blocks.LIGHT) {
                        this.worldObj.setBlockToAir(this.blockX, this.blockY, this.blockZ);
                    }
                }
                Object entity = null;
                List list = this.worldObj.getEntitiesWithinAABB((Class)EntityLivingBase.class, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(3.0, 2.0, 3.0));
                double d0 = 0.0;
                for (int j = 0; j < list.size(); ++j) {
                    EntityLivingBase entity1 = (EntityLivingBase)list.get(j);
                    if (!entity1.canBeCollidedWith()) continue;
                    if (CreatureUtil.isUndead((Entity)entity1)) {
                        EntityPlayer player;
                        ExtendedPlayer playerEx;
                        entity1.setFire(5);
                        if (entity1 instanceof EntityPlayer && (playerEx = ExtendedPlayer.get(player = (EntityPlayer)entity1)).getVampireLevel() == 4 && playerEx.canIncreaseVampireLevel()) {
                            if (playerEx.getVampireQuestCounter() >= 9) {
                                playerEx.increaseVampireLevel();
                            } else {
                                playerEx.increaseVampireQuestCounter();
                            }
                        }
                    }
                    entity1.addPotionEffect(new PotionEffect(Potion.blindness.id, TimeUtil.secsToTicks(this.worldObj.rand.nextInt(3) + 10), 0, true));
                }
            } else if (mode == 1) {
                EntityFollower entity = new EntityFollower(this.worldObj);
                entity.setFollowerType(5);
                entity.setSkin(this.owner != null ? this.owner : "");
                entity.setCustomNameTag(this.owner != null ? this.owner : "Steve");
                entity.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0f, 0.0f);
                entity.setTTL(TimeUtil.secsToTicks(10));

                List<EntityFollower> e = entity.worldObj.getEntitiesWithinAABB(EntityFollower.class, AxisAlignedBB.getBoundingBox(entity.posX - 5, entity.posY - 5, entity.posZ - 5, entity.posX + 5, entity.posY + 5, entity.posZ + 5));
                if(!(e.size() >= 3)) {
                    if(getThrower() instanceof EntityPlayer) {
                        BlockEvent.BreakEvent event = MixinManager.generateBlockEvent((int)posX, (int)posY, (int)posZ, worldObj, (EntityPlayer)getThrower());
                        MinecraftForge.EVENT_BUS.post(event);
                        if(!event.isCanceled()) {
                            this.worldObj.spawnEntityInWorld((Entity)entity);
                            entity.attractAttention();
                        }
                    }
                }
            }
        }
    }
}
