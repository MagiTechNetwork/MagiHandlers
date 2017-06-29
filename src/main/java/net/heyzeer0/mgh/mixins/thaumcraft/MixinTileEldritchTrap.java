package net.heyzeer0.mgh.mixins.thaumcraft;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockZap;

/**
 * Created by Frani on 29/06/2017.
 */

@Pseudo
@Mixin(targets = "thaumcraft/common/tiles/TileEldritchTrap", remap = false)
public abstract class MixinTileEldritchTrap extends TileEntity {

    @Shadow
    int count;

    @Overwrite
    public void func_145845_h() {
        super.updateEntity();
        if (!this.worldObj.isRemote && this.count-- <= 0) {
            this.count = 10 + this.worldObj.rand.nextInt(25);
            EntityPlayer p = this.worldObj.getClosestPlayer((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D, 3.0D);
            if (p != null) {
                if(p.getHealth() >= 3) {
                    p.setHealth(p.getHealth() - 2);
                }
                if (this.worldObj.rand.nextBoolean()) {
                    Thaumcraft.addWarpToPlayer(p, 1 + this.worldObj.rand.nextInt(2), true);
                }
                PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockZap((float) this.xCoord + 0.5F, (float) this.yCoord + 0.5F, (float) this.zCoord + 0.5F, (float) p.posX, (float) p.boundingBox.minY + p.eyeHeight, (float) p.posZ), new NetworkRegistry.TargetPoint(this.worldObj.provider.dimensionId, (double) this.xCoord, (double) this.yCoord, (double) this.zCoord, 32.0D));
            }
        }

    }
}
