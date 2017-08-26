package net.heyzeer0.mgh.mixins.botania;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.api.subtile.SubTileGenerating;

import java.util.List;

/**
 * Created by HeyZeer0 on 08/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "vazkii/botania/common/block/subtile/generating/SubTileGourmaryllis", remap = false)
public abstract class MixinSubTileGourmaryllis extends SubTileGenerating {

    @Shadow
    int cooldown = 0;

    @Shadow
    int storedMana = 0;
    
    @Final
    @Shadow
    private static final int RANGE = 1;

    @Overwrite
    public void onUpdate() {
        super.onUpdate();

        if(cooldown > 0)
            cooldown--;
        if(cooldown == 0 && !supertile.getWorldObj().isRemote && storedMana != 0) {
            mana = Math.min(getMaxMana(), mana + storedMana);
            storedMana = 0;
            sync();
        }

        int slowdown = getSlowdownFactor();

        boolean remote = supertile.getWorldObj().isRemote;
        List<EntityItem> items = supertile.getWorldObj().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - RANGE, supertile.yCoord - RANGE, supertile.zCoord - RANGE, supertile.xCoord + RANGE + 1, supertile.yCoord + RANGE + 1, supertile.zCoord + RANGE + 1));
        for(EntityItem item : items) {
            ItemStack stack = item.getEntityItem();
            if(stack != null && stack.getItem() instanceof ItemFood && !item.isDead && item.age >= slowdown) {
                if(cooldown == 0) {
                    if(!remote) {
                        int val = ((ItemFood) stack.getItem()).func_150905_g(stack);
                        storedMana = val * val * 64;
                        cooldown = val * 10;
                        supertile.getWorldObj().playSoundEffect(supertile.xCoord, supertile.yCoord, supertile.zCoord, "random.eat", 0.2F, 0.5F + (float) Math.random() * 0.5F);
                        sync();
                    } else
                        for(int i = 0; i < 10; i++) {
                            float m = 0.2F;
                            float mx = (float) (Math.random() - 0.5) * m;
                            float my = (float) (Math.random() - 0.5) * m;
                            float mz = (float) (Math.random() - 0.5) * m;
                            supertile.getWorldObj().spawnParticle("iconcrack_" + Item.getIdFromItem(stack.getItem()), item.posX, item.posY, item.posZ, mx, my, mz);
                        }
                }

                if(!remote)
                    item.setDead();
            }
        }
    }

}
