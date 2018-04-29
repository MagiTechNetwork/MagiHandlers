package net.heyzeer0.mgh.mixins.botania;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.item.ItemWorldSeed;

/**
 * Created by Frani on 28/04/2018.
 */
@Mixin(ItemWorldSeed.class)
public abstract class MixinItemWorldSeed {

    @Overwrite
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        ChunkCoordinates coords = world.getSpawnPoint();
        if (MathHelper.pointDistanceSpace((double) coords.posX + 0.5D, (double) coords.posY + 0.5D, (double) coords.posZ + 0.5D, player.posX, player.posY, player.posZ) > 24.0F) {
            player.rotationPitch = 0.0F;
            player.rotationYaw = 0.0F;
            player.setPositionAndUpdate((double) coords.posX + 0.5D, (double) coords.posY + 1.6D, (double) coords.posZ + 0.5D);

            // MagiHandlers start - skip FakePlayers, they don't move
            if (!(player instanceof FakePlayer)) {
                // MagiHandlers end
                while (!world.getCollidingBoundingBoxes(player, player.boundingBox).isEmpty()) {
                    player.setPositionAndUpdate(player.posX, player.posY + 1.0D, player.posZ);
                }
            }

            world.playSoundAtEntity(player, "mob.endermen.portal", 1.0F, 1.0F);

            for (int i = 0; i < 50; ++i) {
                Botania.proxy.sparkleFX(world, player.posX + Math.random() * (double) player.width, player.posY - 1.6D + Math.random() * (double) player.height, player.posZ + Math.random() * (double) player.width, 0.25F, 1.0F, 0.25F, 1.0F, 10);
            }

            --stack.stackSize;
        }

        return stack;
    }

}
