package net.heyzeer0.mgh.mixins.thaumcraft;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.api.research.ScanResult;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Frani on 29/06/2017.
 */

@Pseudo
@Mixin(targets = "thaumcraft/common/tiles/TileNode", remap = false)
public abstract class MixinTileNode extends TileThaumcraft implements INode, IWandable {

    @Shadow public abstract NodeType getNodeType();

    @Shadow public abstract double getDistanceTo(double par1, double par3, double par5);

    @Shadow private AspectList aspectsBase;

    @Overwrite
    private boolean handleHungryNodeFirst(boolean change) {
        if(this.getNodeType() == NodeType.HUNGRY) {
            if (Config.hardNode) {
                List var16 = this.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox((double) this.xCoord, (double) this.yCoord, (double) this.zCoord, (double) (this.xCoord + 1), (double) (this.yCoord + 1), (double) (this.zCoord + 1)).expand(15.0D, 15.0D, 15.0D));
                if (var16 != null && var16.size() > 0) {
                    Iterator var17 = var16.iterator();

                    while (true) {
                        Entity var19;
                        do {
                            if (!var17.hasNext()) {
                                return change;
                            }

                            Object var18 = var17.next();
                            var19 = (Entity) var18;
                        } while (var19 instanceof EntityPlayer && ((EntityPlayer) var19).capabilities.disableDamage);

                        double var20;

                        EntityPlayer who = null;
                        boolean hitCanceled = false;

                        if (var19 instanceof EntityPlayer) {
                            who = (EntityPlayer) var19;
                            BlockEvent.BreakEvent evt = MixinManager.generateBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.worldObj, (EntityPlayer) var19);
                            MinecraftForge.EVENT_BUS.post(evt);
                            if (evt.isCanceled()) {
                                break;
                            }

                            if (!var17.hasNext()) {
                                return change;
                            }

                            Object var18 = var17.next();
                            var19 = (Entity) var18;
                        }
                        if (var19.isEntityAlive() && !var19.isEntityInvulnerable()) {
                            var20 = this.getDistanceTo(var19.posX, var19.posY, var19.posZ);
                            if (var20 < 2.0D) {

                                if (who != null) {
                                    AttackEntityEvent event = new AttackEntityEvent(FakePlayerFactory.getMinecraft((WorldServer) worldObj), who);
                                    MinecraftForge.EVENT_BUS.post(event);
                                    if (event.isCanceled()) {
                                        hitCanceled = true;
                                    }
                                }
                                if (!hitCanceled) {
                                    var19.attackEntityFrom(DamageSource.outOfWorld, 1.0F);
                                }

                                if (!var19.isEntityAlive() && !this.worldObj.isRemote) {
                                    ScanResult var21 = new ScanResult((byte) 2, 0, 0, var19, "");
                                    AspectList var23 = ScanManager.getScanAspects(var21, this.worldObj);
                                    if (var23 != null && var23.size() > 0) {
                                        var23 = ResearchManager.reduceToPrimals(var23.copy());
                                        if (var23 != null && var23.size() > 0) {
                                            Aspect var24 = var23.getAspects()[this.worldObj.rand.nextInt(var23.size())];
                                            if (this.getAspects().getAmount(var24) < this.getNodeVisBase(var24)) {
                                                this.addToContainer(var24, 1);
                                                change = true;
                                            } else if (this.worldObj.rand.nextInt(1 + this.getNodeVisBase(var24) * 2) < var23.getAmount(var24)) {
                                                this.aspectsBase.add(var24, 1);
                                                    change = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        var20 = ((double) this.xCoord + 0.5D - var19.posX) / 15.0D;
                        double var22 = ((double) this.yCoord + 0.5D - var19.posY) / 15.0D;
                        double var25 = ((double) this.zCoord + 0.5D - var19.posZ) / 15.0D;
                        double var9 = Math.sqrt(var20 * var20 + var22 * var22 + var25 * var25);
                        double var11 = 1.0D - var9;
                        if (var11 > 0.0D) {
                            var11 *= var11;
                            var19.motionX += var20 / var9 * var11 * 0.15D;
                            var19.motionY += var22 / var9 * var11 * 0.25D;
                            var19.motionZ += var25 / var9 * var11 * 0.15D;
                        }
                    }
                }
            }
        }
        return change;
    }
}
