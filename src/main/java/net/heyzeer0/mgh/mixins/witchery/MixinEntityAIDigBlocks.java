package net.heyzeer0.mgh.mixins.witchery;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.ai.EntityAIDigBlocks;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Frani on 29/04/2018.
 */
@Mixin(value = EntityAIDigBlocks.class, remap = false)
public abstract class MixinEntityAIDigBlocks {

    @Shadow
    @Final
    public static GameProfile KOBOLDITE_MINER_PROFILE;
    @Shadow
    @Final
    public static GameProfile NORMAL_MINER_PROFILE;

    @Shadow
    private static boolean isEqual(GameProfile a, GameProfile b) {
        return false;
    }

    @Overwrite
    public static void onHarvestDrops(EntityPlayer harvester, BlockEvent.HarvestDropsEvent event) {
        if (harvester != null && !harvester.worldObj.isRemote && !event.isCanceled() && (isEqual(harvester.getGameProfile(), KOBOLDITE_MINER_PROFILE) || isEqual(harvester.getGameProfile(), NORMAL_MINER_PROFILE))) {
            boolean hasKobolditePick = isEqual(harvester.getGameProfile(), KOBOLDITE_MINER_PROFILE);
            ArrayList<ItemStack> newDrops = new ArrayList();
            double kobolditeChance = hasKobolditePick ? 0.02D : 0.01D;
            Iterator i$ = event.drops.iterator();

            ItemStack drop;
            while (i$.hasNext()) {
                drop = (ItemStack) i$.next();
                int[] oreIDs = OreDictionary.getOreIDs(drop);
                boolean addOriginal = true;
                if (oreIDs.length > 0) {
                    String oreName = OreDictionary.getOreName(oreIDs[0]);
                    if (oreName != null && oreName.startsWith("ore")) {
                        ItemStack smeltedDrop = FurnaceRecipes.smelting().getSmeltingResult(drop);
                        if (smeltedDrop != null && hasKobolditePick && harvester.worldObj.rand.nextDouble() < 0.5D) {
                            addOriginal = false;
                            newDrops.add(smeltedDrop.copy());
                            newDrops.add(smeltedDrop.copy());
                            if (harvester.worldObj.rand.nextDouble() < 0.25D) {
                                newDrops.add(smeltedDrop.copy());
                            }
                        }

                        kobolditeChance = hasKobolditePick ? 0.08D : 0.05D;
                    }
                }

                if (addOriginal) {
                    newDrops.add(drop);
                }
            }

            event.drops.clear();
            i$ = newDrops.iterator();

            while (i$.hasNext()) {
                drop = (ItemStack) i$.next();
                event.drops.add(drop);
            }

            // MagiHandlers start - double the chance
            if (kobolditeChance > 0.0D && harvester.worldObj.rand.nextDouble() < (kobolditeChance * 2)) {
                // MagiHandlers end
                event.drops.add(Witchery.Items.GENERIC.itemKobolditeDust.createStack());
            }
        }

    }

}
