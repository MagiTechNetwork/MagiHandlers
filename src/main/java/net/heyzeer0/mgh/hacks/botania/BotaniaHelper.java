package net.heyzeer0.mgh.hacks.botania;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import vazkii.botania.api.item.IGrassHornExcempt;
import vazkii.botania.api.item.IHornHarvestable;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Frani on 11/06/2017.
 */
public abstract class BotaniaHelper {
    public static void breakGrass(World world, ItemStack stack, int stackDmg, int srcx, int srcy, int srcz, EntityPlayer player) {
        IHornHarvestable.EnumHornType type = IHornHarvestable.EnumHornType.getTypeForMeta(stackDmg);
        Random rand = new Random(srcx ^ srcy ^ srcz);
        int range = 12 - stackDmg * 3;
        int rangeY = 3 + stackDmg * 4;
        List<ChunkCoordinates> coords = new ArrayList<>();

        for(int i = -range; i < range + 1; i++)
            for(int j = -range; j < range + 1; j++)
                for(int k = -rangeY; k < rangeY + 1; k++) {
                    int x = srcx + i;
                    int y = srcy + k;
                    int z = srcz + j;

                    Block block = world.getBlock(x, y, z);
                    if(block instanceof IHornHarvestable ? ((IHornHarvestable) block).canHornHarvest(world, x, y, z, stack, type) : stackDmg == 0 && block instanceof BlockBush && !(block instanceof ISpecialFlower) && (!(block instanceof IGrassHornExcempt) || ((IGrassHornExcempt) block).canUproot(world, x, y, z)) || stackDmg == 1 && block.isLeaves(world, x, y, z) || stackDmg == 2 && block == Blocks.snow_layer)
                        coords.add(new ChunkCoordinates(x, y, z));
                }

        Collections.shuffle(coords, rand);

        int count = Math.min(coords.size(), 32 + stackDmg * 16);
        for(int i = 0; i < count; i++) {
            ChunkCoordinates currCoords = coords.get(i);
            List<ItemStack> items = new ArrayList();
            Block block = world.getBlock(currCoords.posX, currCoords.posY, currCoords.posZ);
            int meta = world.getBlockMetadata(currCoords.posX, currCoords.posY, currCoords.posZ);
            items.addAll(block.getDrops(world, currCoords.posX, currCoords.posY, currCoords.posZ, meta, 0));

            System.out.println("O EVENTO ESTA SENDO CHAMADO");
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(currCoords.posX, currCoords.posY, currCoords.posZ, world, block, meta, player);
            MinecraftForge.EVENT_BUS.post(event);
            if(event.isCanceled()) {
                return;
            }

            if(block instanceof IHornHarvestable && ((IHornHarvestable) block).hasSpecialHornHarvest(world, currCoords.posX, currCoords.posY, currCoords.posZ, stack, type))
                ((IHornHarvestable) block).harvestByHorn(world, currCoords.posX, currCoords.posY, currCoords.posZ, stack, type);
            else if(!world.isRemote) {
                world.setBlockToAir(currCoords.posX, currCoords.posY, currCoords.posZ);
                if(ConfigHandler.blockBreakParticles)
                    world.playAuxSFX(2001, currCoords.posX, currCoords.posY, currCoords.posZ, Block.getIdFromBlock(block) + (meta << 12));

                for(ItemStack stack_ : items)
                    world.spawnEntityInWorld(new EntityItem(world, currCoords.posX + 0.5, currCoords.posY + 0.5, currCoords.posZ + 0.5, stack_));
            }
        }
    }
}