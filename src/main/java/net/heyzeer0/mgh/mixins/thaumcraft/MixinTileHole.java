package net.heyzeer0.mgh.mixins.thaumcraft;

import net.heyzeer0.mgh.hacks.thaumcraft.IMixinTileHole;
import net.heyzeer0.mgh.hacks.thaumcraft.ThaumcraftHelper;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileMemory;

/**
 * Created by HeyZeer0 on 10/06/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "thaumcraft/common/tiles/TileHole", remap = false)
public abstract class MixinTileHole extends TileMemory implements IMixinTileHole {

    private EntityPlayer plr;

    @Shadow
    public short countdown = 0;

    @Shadow
    public short countdownmax = 120;

    @Shadow
    public byte count = 0;

    @Shadow
    public byte direction = 0;


    @Override
    public void setOwner(EntityPlayer x) {
        plr = x;
    }

    @Override
    public EntityPlayer getOwner() {
        return plr;
    }

    @Overwrite
    public void func_145845_h()
    {
        super.updateEntity();

        if (this.worldObj.isRemote)
        {
            surroundwithsparkles();
        }

        if ((this.countdown == 0) && (this.count > 1) && (this.direction != -1)) {
            int ii = this.xCoord;
            int jj = this.yCoord;
            int kk = this.zCoord;

            switch (this.direction) {
                case 0: case 1:
                    for (int a = 0; a < 9; a++) if ((a / 3 != 1) || (a % 3 != 1))
                        ThaumcraftHelper.createHole(getOwner(), this.worldObj, ii - 1 + a / 3, jj, kk - 1 + a % 3, -1, (byte)1, this.countdownmax);
                    break;
                case 2: case 3:
                    for (int a = 0; a < 9; a++) if ((a / 3 != 1) || (a % 3 != 1))
                        ThaumcraftHelper.createHole(getOwner(), this.worldObj, ii - 1 + a / 3, jj - 1 + a % 3, kk, -1, (byte)1, this.countdownmax);
                    break;
                case 4: case 5:
                    for (int a = 0; a < 9; a++) { if ((a / 3 != 1) || (a % 3 != 1)) {
                        ThaumcraftHelper.createHole(getOwner(), this.worldObj, ii, jj - 1 + a / 3, kk - 1 + a % 3, -1, (byte)1, this.countdownmax);
                    }
                    }
            }
            switch (this.direction) {
                case 0:  jj++; break;
                case 1:  jj--; break;
                case 2:  kk++; break;
                case 3:  kk--; break;
                case 4:  ii++; break;
                case 5:  ii--;
            }

            if (!ThaumcraftHelper.createHole(getOwner(), this.worldObj, ii, jj, kk, this.direction, (byte)(this.count - 1), this.countdownmax))
            {
                this.count = 0;
            }
        }

        this.countdown = ((short)(this.countdown + 1));

        if (this.countdown >= this.countdownmax) {
            if (this.worldObj.isRemote) {
                Thaumcraft.proxy.blockSparkle(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 4194368, 1);
            } else {
                this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.oldblock, this.oldmeta, 0);
                recreateTileEntity();
            }
            this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, this.oldblock, 2);
        }
    }

    @Shadow
    private void surroundwithsparkles() {}


}
