package net.heyzeer0.mgh.mixins.botania;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

/**
 * Created by Frani on 13/06/2017.
 */

@Pseudo
@Mixin(targets = "vazkii/botania/common/entity/EntityEnderAirBottle", remap = false)
public abstract class MixinEntityEnderAirBottle extends EntityThrowable {

    @Shadow
    public abstract List<ChunkCoordinates> getCoordsToPut(int xCoord, int yCoord, int zCoord);

    public MixinEntityEnderAirBottle(World world) {
        super(world);
    }

    @Overwrite
    protected void func_70184_a(MovingObjectPosition pos) {
        if (pos.entityHit == null && !worldObj.isRemote) {
            List<ChunkCoordinates> coordsList = getCoordsToPut(pos.blockX, pos.blockY, pos.blockZ);
            worldObj.playAuxSFX(2002, (int) Math.round(posX), (int) Math.round(posY), (int) Math.round(posZ), 8);

            for (ChunkCoordinates coords : coordsList) {
                BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(coords.posX, coords.posY, coords.posZ, worldObj, worldObj.getBlock(coords.posX, coords.posY, coords.posZ), worldObj.getBlockMetadata(coords.posX, coords.posY, coords.posZ), (EntityPlayer) getThrower());
                MinecraftForge.EVENT_BUS.post(event);
                if (!event.isCanceled()) {
                    worldObj.setBlock(coords.posX, coords.posY, coords.posZ, Blocks.end_stone);
                    if (Math.random() < 0.1)
                        worldObj.playAuxSFX(2001, coords.posX, coords.posY, coords.posZ, Block.getIdFromBlock(Blocks.end_stone));
                }
                setDead();
            }
        }
    }

}
