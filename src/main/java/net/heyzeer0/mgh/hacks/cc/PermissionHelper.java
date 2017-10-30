package net.heyzeer0.mgh.hacks.cc;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.shared.turtle.core.TurtlePlaceCommand;
import dan200.computercraft.shared.turtle.core.TurtlePlayer;
import dan200.computercraft.shared.util.WorldUtil;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.FakePlayer;

/**
 * Created by Frani on 16/10/2017.
 */
public class PermissionHelper {

    public static boolean canTurtleBreak(ITurtleAccess turtle, int dir) {
        ChunkCoordinates pos = WorldUtil.moveCoords(turtle.getPosition(), dir);
        ITileEntityOwnable tile = (ITileEntityOwnable) turtle.getWorld().getTileEntity(turtle.getPosition().posX, turtle.getPosition().posY, turtle.getPosition().posZ);
        return MixinManager.canBuild(pos.posX, pos.posY, pos.posZ, turtle.getWorld(), tile.getFakePlayer());
    }

    public static boolean canTurtleAttack(ITurtleAccess turtle, int dir) {
        TurtlePlayer turtlePlayer = TurtlePlaceCommand.createPlayer(turtle, turtle.getPosition(), dir);
        Vec3 turtlePos = Vec3.createVectorHelper(turtlePlayer.posX, turtlePlayer.posY, turtlePlayer.posZ);
        Vec3 rayDir = turtlePlayer.getLook(1.0F);
        Vec3 rayStart = turtlePos.addVector(rayDir.xCoord * 0.4D, rayDir.yCoord * 0.4D, rayDir.zCoord * 0.4D);
        Entity hitEntity = WorldUtil.rayTraceEntities(turtle.getWorld(), rayStart, rayDir, 1.1D);
        return MixinManager.canAttack(((ITileEntityOwnable) turtle.getWorld().getTileEntity(turtle.getPosition().posX, turtle.getPosition().posY, turtle.getPosition().posZ)).getFakePlayer(), hitEntity);
    }

}
