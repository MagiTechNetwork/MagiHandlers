package net.heyzeer0.mgh.mixins.enderstorage;

import codechicken.enderstorage.common.TileFrequencyOwner;
import net.heyzeer0.mgh.hacks.enderstorage.ITileFrequencyOwner;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.reflect.Field;

/**
 * Created by Frani on 02/12/2017.
 */
@Pseudo
@Mixin(value = TileFrequencyOwner.class, remap = false)
public abstract class MixinTileFrequencyOwner extends TileEntity implements ITileFrequencyOwner {

    @Shadow public String owner;

    @Shadow public abstract void reloadStorage();

    private Field field;

    @Overwrite
    public void setOwner(String s) {
        try {
            if (field == null) {
                field = Class.forName("net.minecraft.tileentity.TileEntity").getDeclaredField("tileOwner");
                field.setAccessible(true);
            }
            field.set(this, s);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {}
    }

    @Override
    public void setFrequencyOwner(String username) {
        owner = username;
        reloadStorage();
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

}
