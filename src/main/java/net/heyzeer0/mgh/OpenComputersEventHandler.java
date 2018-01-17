package net.heyzeer0.mgh;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import li.cil.oc.api.event.RobotAttackEntityEvent;
import li.cil.oc.api.event.RobotBreakBlockEvent;
import li.cil.oc.api.event.RobotMoveEvent;
import li.cil.oc.api.event.RobotPlaceBlockEvent;
import li.cil.oc.util.BlockPosition;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.tileentity.TileEntity;
import scala.Option;

/**
 * Created by Frani on 13/01/2018.
 */
public class OpenComputersEventHandler {

    @SubscribeEvent
    public void onMove(RobotMoveEvent.Pre e) {
        TileEntity t = e.agent.world().getTileEntity((int) e.agent.xPosition(), (int) e.agent.yPosition(), (int) e.agent.zPosition());
        IForgeTileEntity tile = (IForgeTileEntity) t;
        BlockPosition pos = new BlockPosition(t.xCoord, t.yCoord, t.zCoord, Option.apply(t.getWorldObj()));
        BlockPosition newPos = pos.offset(e.direction);
        if (!MixinManager.canBuild(newPos.x(), newPos.y(), newPos.z(), newPos.world().get(), tile.getMHPlayer())) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMovePost(RobotMoveEvent.Post e) {
        MagiHandlers.scheduleTileCheck(
                e.agent.ownerName(),
                e.agent.ownerUUID().toString(),
                e.agent.world(),
                (int) e.agent.xPosition(),
                (int) e.agent.yPosition(),
                (int) e.agent.zPosition()
        );
    }

    @SubscribeEvent
    public void onAttack(RobotAttackEntityEvent.Pre e) {
        IForgeTileEntity tile = (IForgeTileEntity) e.agent.world().getTileEntity((int) e.agent.xPosition(), (int) e.agent.yPosition(), (int) e.agent.zPosition());
        if (!MixinManager.canAttack(tile.getMHPlayer(), e.target)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBreak(RobotBreakBlockEvent.Pre e) {
        IForgeTileEntity tile = (IForgeTileEntity) e.agent.world().getTileEntity((int) e.agent.xPosition(), (int) e.agent.yPosition(), (int) e.agent.zPosition());
        if (!MixinManager.canBuild(e.x, e.y, e.z, e.world, tile.getMHPlayer())) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlace(RobotPlaceBlockEvent.Pre e) {
        IForgeTileEntity tile = (IForgeTileEntity) e.agent.world().getTileEntity((int) e.agent.xPosition(), (int) e.agent.yPosition(), (int) e.agent.zPosition());
        if (!MixinManager.canBuild(e.x, e.y, e.z, e.world, tile.getMHPlayer())) {
            e.setCanceled(true);
        }
    }

}
